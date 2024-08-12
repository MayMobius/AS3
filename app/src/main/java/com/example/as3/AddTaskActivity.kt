package com.example.as3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val taskEditText: EditText = findViewById(R.id.taskEditText)
        val addButton: Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            val task = taskEditText.text.toString().trim()
            if (task.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("task", task)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                taskEditText.error = "Task cannot be empty"
            }
        }
    }
}
