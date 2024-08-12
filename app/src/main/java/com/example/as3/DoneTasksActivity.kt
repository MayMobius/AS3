package com.example.as3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class DoneTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done_tasks)

        val doneTasks = intent.getStringArrayListExtra("doneTasks") ?: ArrayList()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, doneTasks)
        val listView: ListView = findViewById(R.id.listViewDoneTasks)
        listView.adapter = adapter
    }
}
