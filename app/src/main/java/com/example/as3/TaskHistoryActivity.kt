package com.example.as3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TaskHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_history)

        val allTasks = intent.getStringArrayListExtra("allTasks") ?: ArrayList()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, allTasks)
        val listView: ListView = findViewById(R.id.listViewTaskHistory)
        listView.adapter = adapter
    }
}
