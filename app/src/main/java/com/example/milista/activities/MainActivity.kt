package com.example.milista.activities

import android.os.Bundle
import android.telephony.ims.RcsUceAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.milista.R
import com.example.milista.adapters.TaskAdapter
import com.example.milista.data.providers.TaskDAO
import com.example.milista.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        /*val taskDAO = TaskDAO(this)
        //taskDAO.insert(Task(-1, "Limpiar el coche", false))
        val task = taskDAO.findById(1)!!
        task.done = true
        taskDAO.delete(task)
        val taskList = taskDAO.findAll()
        for (task in taskList) {
            println(task)
        }*/
    }
}