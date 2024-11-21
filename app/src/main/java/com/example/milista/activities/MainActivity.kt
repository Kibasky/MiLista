package com.example.milista.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.milista.R
import com.example.milista.adapters.TaskAdapter
import com.example.milista.data.entities.Task
import com.example.milista.data.providers.TaskDAO
import com.example.milista.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: TaskAdapter

    lateinit var taskDAO: TaskDAO

    var taskList: MutableList<Task> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = TaskDAO(this)

        /* ejemplos de tareas:
        taskDAO.insert(Task(-1, "Comprar leche"))
        taskDAO.insert(Task(-1, "Pagar el alquiler"))
        taskDAO.insert(Task(-1, "Pasear al perro"))*/

        adapter = TaskAdapter(taskList, {
            // Editar tarea
            val task = taskList[it]
            showTask(task)
        }, {
            // Marcar tarea
            val task = taskList[it]
            checkTask(task)
        }, {
            // Borrar tarea
            val task = taskList[it]
            deleteTask(task)
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Crear tarea
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Cargamos la lista por si se hubiera añadido una tarea nueva
        taskList = taskDAO.findAll().toMutableList()

        adapter.updateItems(taskList)
    }

    // Funcion para cuando marcamos una tarea (finalizada/pendiente)
    fun checkTask(task: Task) {
        task.done = !task.done
        taskDAO.update(task)
        adapter.updateItems(taskList)
    }

    // Funciona para mostrar un dialogo para borrar la tarea
    fun deleteTask(task: Task) {
        // Mostramos un dialogo para asegurarnos de que el usuario quiere borrar la tarea
        AlertDialog.Builder(this)
            .setTitle("Borrar tarea")
            .setMessage("Estas seguro de que quieres borrar la tarea?")
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                // Borramos la tarea en caso de pulsar el boton OK
                taskDAO.delete(task)
                taskList.remove(task)
                adapter.updateItems(taskList)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    // Mostramos la tarea para editarla
    fun showTask(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.EXTRA_TASK_ID, task.id)
        startActivity(intent)
    }
}