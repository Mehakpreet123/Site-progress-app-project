package com.example.siteprogress.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.adapters.TasksAdapter
import com.example.siteprogress.data.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class TaskSchedulerFragment : Fragment(), TasksAdapter.OnTaskActionListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var addButton: Button
    private lateinit var spinnerProjects: Spinner

    private var selectedProjectId: String? = null
    private val projectList = mutableListOf<Pair<String, String>>() // Pair<ProjectID, ProjectName>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_scheduler, container, false)

        db = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.tasksRecyclerView)
        addButton = view.findViewById(R.id.btnAddTask)
        spinnerProjects = view.findViewById(R.id.spinnerProjects)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksAdapter = TasksAdapter(emptyList(), this)
        recyclerView.adapter = tasksAdapter

        addButton.setOnClickListener {
            if (selectedProjectId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Select a project first", Toast.LENGTH_SHORT).show()
            } else {
                showAddTaskDialog()
            }
        }

        loadProjects()

        return view
    }

    private fun loadProjects() {
        db.collection("projects")
            .get()
            .addOnSuccessListener { documents ->
                projectList.clear()
                for (doc in documents) {
                    val projectId = doc.id
                    val projectName = doc.getString("name") ?: "Unnamed Project"
                    projectList.add(Pair(projectId, projectName))
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    projectList.map { it.second }
                )
                spinnerProjects.adapter = adapter

                spinnerProjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        selectedProjectId = projectList[position].first
                        fetchTasks(selectedProjectId!!)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        selectedProjectId = null
                    }
                }

                if (projectList.isNotEmpty()) {
                    spinnerProjects.setSelection(0)
                    selectedProjectId = projectList[0].first
                    fetchTasks(selectedProjectId!!)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load projects", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchTasks(projectId: String) {
        db.collection("tasks")
            .whereEqualTo("projectId", projectId)
            .get()
            .addOnSuccessListener { documents ->
                val tasks = documents.map { doc ->
                    doc.toObject(Task::class.java).copy(id = doc.id)
                }
                tasksAdapter.updateTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch tasks", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTaskTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.editTaskDescription)
        val editDeadline = dialogView.findViewById<EditText>(R.id.editTaskDeadline)

        if (selectedProjectId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No project selected", Toast.LENGTH_SHORT).show()
            return
        }

        editDeadline.setOnClickListener {
            showDatePickerDialog(editDeadline)
        }

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = editTitle.text.toString().trim()
                val description = editDescription.text.toString().trim()
                val deadline = editDeadline.text.toString().trim()

                if (title.isEmpty() || deadline.isEmpty()) {
                    Toast.makeText(requireContext(), "Title and Deadline are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val newTask = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "deadline" to deadline,
                    "projectId" to selectedProjectId!!,
                    "isCompleted" to false
                )

                db.collection("tasks")
                    .add(newTask)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show()
                        fetchTasks(selectedProjectId!!)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to add task", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    override fun onEditTask(task: Task) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTaskTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.editTaskDescription)
        val editDeadline = dialogView.findViewById<EditText>(R.id.editTaskDeadline)

        editTitle.setText(task.title)
        editDescription.setText(task.description)
        editDeadline.setText(task.deadline)

        editDeadline.setOnClickListener {
            showDatePickerDialog(editDeadline)
        }

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedTitle = editTitle.text.toString().trim()
                val updatedDescription = editDescription.text.toString().trim()
                val updatedDeadline = editDeadline.text.toString().trim()

                if (updatedTitle.isEmpty() || updatedDeadline.isEmpty()) {
                    Toast.makeText(requireContext(), "Title and Deadline are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                db.collection("tasks").document(task.id)
                    .update("title", updatedTitle, "description", updatedDescription, "deadline", updatedDeadline)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Task Updated", Toast.LENGTH_SHORT).show()
                        fetchTasks(selectedProjectId!!)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to update task", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onMarkComplete(task: Task) {
        db.collection("tasks").document(task.id)
            .update("isCompleted", true)
            .addOnSuccessListener {
                fetchTasks(selectedProjectId!!)
            }
    }

    override fun onDeleteTask(task: Task) {
        db.collection("tasks").document(task.id)
            .delete()
            .addOnSuccessListener {
                fetchTasks(selectedProjectId!!)
            }
    }
}
