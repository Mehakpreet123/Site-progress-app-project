package com.example.siteprogress

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.adapters.UserAdapter
import com.example.siteprogress.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class UserManagementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<User>()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_management, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        val btnAddUser = view.findViewById<Button>(R.id.btnAddUser)

        firestore = FirebaseFirestore.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        btnAddUser.setOnClickListener { showAddUserDialog() }

        fetchUsersFromFirebase()

        return view
    }

    private fun fetchUsersFromFirebase() {
        firestore.collection("users").get()
            .addOnSuccessListener { result ->
                userList.clear()
                for (document in result) {
                    val user = document.toObject(User::class.java)
                    userList.add(user)
                }
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error fetching users", e)
            }
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val etUserName = dialogView.findViewById<EditText>(R.id.etUserName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val spinnerRole = dialogView.findViewById<Spinner>(R.id.spinnerRole)
        val etProject = dialogView.findViewById<EditText>(R.id.etProject)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAddUser)

        val roles = arrayOf("Admin", "Site Engineer", "Worker")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRole.adapter = adapter

        btnAdd.setOnClickListener {
            val name = etUserName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val role = spinnerRole.selectedItem.toString()
            val project = etProject.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && project.isNotEmpty()) {
                val newUser = User(UUID.randomUUID().toString(), name, email, role, phone, project)
                saveUserToFirebase(newUser)
                alertDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun saveUserToFirebase(user: User) {
        val userId = user.id ?: firestore.collection("users").document().id  // Generate ID if null

        user.id = userId // Assign generated ID to the user object

        firestore.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User added successfully", Toast.LENGTH_SHORT).show()
                fetchUsersFromFirebase()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error adding user", e)
            }
    }

}
