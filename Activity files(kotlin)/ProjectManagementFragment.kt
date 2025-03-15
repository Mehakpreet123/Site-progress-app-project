package com.example.siteprogress

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.adapters.ProjectsAdapter
import com.example.siteprogress.data.model.Project
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.text.SimpleDateFormat
import java.util.*

class ProjectManagementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectsAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_project_management, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewProjects)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        projectAdapter = ProjectsAdapter(emptyList(), ::showEditProjectDialog, ::deleteProject)
        recyclerView.adapter = projectAdapter

        fetchProjects()

        view.findViewById<Button>(R.id.btnAddProject).setOnClickListener {
            showAddProjectDialog()
        }

        return view
    }

    // Fetch projects from Firebase
    private fun fetchProjects() {
        firestore.collection("projects")
            .get(Source.SERVER)
            .addOnSuccessListener { documents ->
                val projectList = documents.map { doc ->
                    Project(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        deadline = doc.getString("deadline") ?: "",
                        status = doc.getString("status") ?: "Not Started",
                        progress = (doc.getLong("progress") ?: 0).toInt()
                    )
                }
                projectAdapter.updateProjects(projectList)
            }
    }

    // Show Add Project Dialog with Date Picker
    private fun showAddProjectDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_projects, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val nameInput = dialogView.findViewById<EditText>(R.id.etProjectName)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.etProjectDescription)
        val deadlineInput = dialogView.findViewById<EditText>(R.id.etProjectDeadline)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveProject)

        // Set up Date Picker Dialog
        deadlineInput.setOnClickListener {
            showDatePicker(deadlineInput)
        }

        btnSave.setOnClickListener {
            val newProject = hashMapOf(
                "name" to nameInput.text.toString(),
                "description" to descriptionInput.text.toString(),
                "deadline" to deadlineInput.text.toString(),
                "status" to "Not Started",
                "progress" to 0
            )

            firestore.collection("projects").add(newProject)
                .addOnSuccessListener {
                    fetchProjects()
                    dialog.dismiss()
                }
        }
        dialog.show()
    }

    // Show Edit Project Dialog with Date Picker
    private fun showEditProjectDialog(project: Project) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_projects, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val nameInput = dialogView.findViewById<EditText>(R.id.etProjectName)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.etProjectDescription)
        val deadlineInput = dialogView.findViewById<EditText>(R.id.etProjectDeadline)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveProject)

        nameInput.setText(project.name)
        descriptionInput.setText(project.description)
        deadlineInput.setText(project.deadline)

        // Set up Date Picker Dialog for Editing Deadline
        deadlineInput.setOnClickListener {
            showDatePicker(deadlineInput)
        }

        btnSave.setOnClickListener {
            firestore.collection("projects").document(project.id)
                .update(
                    "name", nameInput.text.toString(),
                    "description", descriptionInput.text.toString(),
                    "deadline", deadlineInput.text.toString()
                )
                .addOnSuccessListener {
                    fetchProjects()
                    dialog.dismiss()
                }
        }
        dialog.show()
    }

    // Show Date Picker Dialog
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Delete Project
    private fun deleteProject(project: Project) {
        firestore.collection("projects").document(project.id)
            .delete()
            .addOnSuccessListener {
                val updatedList = projectAdapter.getProjects().filter { it.id != project.id }
                projectAdapter.updateProjects(updatedList)  // Refresh UI
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error deleting project: ${e.message}")
            }
    }
}
