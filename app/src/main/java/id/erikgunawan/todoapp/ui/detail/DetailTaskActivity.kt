package id.erikgunawan.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.ui.ViewModelFactory
import id.erikgunawan.todoapp.utils.DateConverter
import id.erikgunawan.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var edTitle: TextInputEditText
    private lateinit var edDescription: TextInputEditText
    private lateinit var edDueDate: TextInputEditText
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edTitle = findViewById(R.id.detail_ed_title)
        edDescription = findViewById(R.id.detail_ed_description)
        edDueDate = findViewById(R.id.detail_ed_due_date)
        btnDelete = findViewById(R.id.btn_delete_task)

        val taskId = intent.getIntExtra(TASK_ID, 0)

        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        detailTaskViewModel.setTaskId(taskId)

        //TODO 11 : Show detail task and implement delete action
        detailTaskViewModel.task.observe(this, Observer { task ->
            task?.let {
                edTitle.setText(it.title)
                edDescription.setText(it.description)
                edDueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
            }
        })

        btnDelete.setOnClickListener {
            detailTaskViewModel.deleteTask()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}