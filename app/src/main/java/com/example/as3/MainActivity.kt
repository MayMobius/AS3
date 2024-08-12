package com.example.as3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskList: MutableList<Task>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>
    private val CHANNEL_ID = "todo_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList.map { it.name })

        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter

        // Light click to mark a task as done
        listView.setOnItemClickListener { _, _, position, _ ->
            val task = taskList[position]
            task.status = TaskStatus.DONE
            updateListView()
            Toast.makeText(this, "Task marked as done", Toast.LENGTH_SHORT).show()
        }

        // Long click to delete a task
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val task = taskList[position]
            task.status = TaskStatus.DELETED
            updateListView()
            Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
            true
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }

        addTaskLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val newTaskName = data?.getStringExtra("task")
                if (!newTaskName.isNullOrEmpty()) {
                    val newTask = Task(newTaskName)
                    taskList.add(newTask)
                    updateListView()
                    sendTaskAddedNotification(newTaskName)
                }
            }
        }

        createNotificationChannel()
    }

    private fun updateListView() {
        adapter.clear()
        adapter.addAll(taskList.filter { it.status == TaskStatus.ACTIVE }.map { it.name })
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_task_done -> {
                showDoneTasks()
                true
            }
            R.id.action_task_history -> {
                showTaskHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDoneTasks() {
        val doneTasks = taskList.filter { it.status == TaskStatus.DONE }.map { it.name }
        if (doneTasks.isEmpty()) {
            Toast.makeText(this, "No tasks have been marked as done.", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, DoneTasksActivity::class.java)
            intent.putStringArrayListExtra("doneTasks", ArrayList(doneTasks))
            startActivity(intent)
        }
    }

    private fun showTaskHistory() {
        val taskHistory = taskList.map { task ->
            when (task.status) {
                TaskStatus.DONE -> "${task.name} (Done)"
                TaskStatus.DELETED -> "${task.name} (Deleted)"
                TaskStatus.ACTIVE -> task.name
            }
        }

        if (taskHistory.isEmpty()) {
            Toast.makeText(this, "No task history available.", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, TaskHistoryActivity::class.java)
            intent.putStringArrayListExtra("allTasks", ArrayList(taskHistory))
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "To-Do List Channel"
            val descriptionText = "Channel for To-Do List notifications"
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: android.app.NotificationManager =
                getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendTaskAddedNotification(task: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Task Added")
            .setContentText(task)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(101, builder.build())
        }
    }
}
