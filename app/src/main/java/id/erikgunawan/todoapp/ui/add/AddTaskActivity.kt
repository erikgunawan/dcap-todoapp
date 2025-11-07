package id.erikgunawan.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.ui.ViewModelFactory
import id.erikgunawan.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var addTaskViewModel: AddTaskViewModel
    private lateinit var edTitle: TextInputEditText
    private lateinit var edDescription: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edTitle = findViewById(R.id.add_ed_title)
        edDescription = findViewById(R.id.add_ed_description)

        val factory = ViewModelFactory.getInstance(this)
        addTaskViewModel = ViewModelProvider(this, factory).get(AddTaskViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun saveTask() {
        // Hide keyboard first
        val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val currentFocus = currentFocus
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
        
        // Clear focus to ensure text is committed
        edTitle.clearFocus()
        edDescription.clearFocus()
        
        // Get text - try editableText first, then fallback to text
        val title = when {
            edTitle.editableText != null -> edTitle.editableText.toString().trim()
            edTitle.text != null -> edTitle.text.toString().trim()
            else -> ""
        }
        
        val description = when {
            edDescription.editableText != null -> edDescription.editableText.toString().trim()
            edDescription.text != null -> edDescription.text.toString().trim()
            else -> ""
        }

        if (title.isEmpty()) {
            edTitle.error = getString(R.string.empty_task_message)
            Toast.makeText(this, getString(R.string.empty_task_message), Toast.LENGTH_SHORT).show()
        } else {
            addTaskViewModel.insertTask(title, description, dueDateMillis)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}