package id.erikgunawan.todoapp.ui.list

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.data.Task
import id.erikgunawan.todoapp.ui.detail.DetailTaskActivity
import id.erikgunawan.todoapp.utils.DateConverter
import id.erikgunawan.todoapp.utils.TASK_ID

class TaskAdapter(
    private val onCheckedChange: (Task, Boolean) -> Unit
) : PagingDataAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    //TODO 8 : Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) ?: return
        //TODO 9 : Bind data to ViewHolder (You can run app to check)
        //TODO 10 : Display title based on status using TitleTextView
        when {
            task.isCompleted -> {
                //DONE
                holder.tvTitle.state = TaskTitleView.DONE
            }
            task.dueDateMillis < System.currentTimeMillis() -> {
                //OVERDUE
                holder.tvTitle.state = TaskTitleView.OVERDUE
            }
            else -> {
                //NORMAL
                holder.tvTitle.state = TaskTitleView.NORMAL
            }
        }
        holder.bind(task)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TaskTitleView = itemView.findViewById(R.id.item_tv_title)
        val cbComplete: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val tvDueDate: TextView = itemView.findViewById(R.id.item_tv_date)

        lateinit var getTask: Task

        fun bind(task: Task) {
            getTask = task
            tvTitle.text = task.title
            tvDueDate.text = DateConverter.convertMillisToString(task.dueDateMillis)
            cbComplete.setOnCheckedChangeListener(null) // Remove listener to avoid triggering during binding
            cbComplete.isChecked = task.isCompleted
            cbComplete.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(task, isChecked)
            }
            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailTaskActivity::class.java)
                detailIntent.putExtra(TASK_ID, task.id)
                itemView.context.startActivity(detailIntent)
            }
        }

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }

    }

}