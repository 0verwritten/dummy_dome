package com.example.scroll_next

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(private val todos: MutableList<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false))
    }

    fun saveToFile(context: Context){ // doesn't need to have file name because it will be constant


//        context.openFileInput("data.dat").use { Toast.makeText(context, it.bufferedReader().readLine(), Toast.LENGTH_SHORT ).show(); }
        context.openFileOutput("data.dat", Context.MODE_PRIVATE).use {
            it.write(todos.toString().toByteArray())
        }

//        Toast.makeText(context, "saved changes", Toast.LENGTH_SHORT).show();
//        context.openFileInput("data.dat").use { Toast.makeText(context, it.bufferedReader().readLine(), Toast.LENGTH_SHORT ).show(); }

    }

    fun loadFromFile(context: Context){ // doesn't need to have file name because it will be constant
        todos.addAll(Todo.fromString(context.openFileInput("data.dat")));
    }

    fun addTodo(todo: Todo, context: Context? = null){
        todos.add(todo)
        println(todo.toString());
        notifyItemInserted(todos.size-1)
        if (context != null)
            saveToFile(context);
    }
    fun deleteDoneTodos(context: Context? = null){
        todos.removeAll { todo -> todo.isChecked }
        notifyDataSetChanged()
        if (context != null)
            saveToFile(context);
    }
    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean){
        if(isChecked){
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }else{
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            tvTodoTitle.text = curTodo.title
            tvDateTime.text = curTodo.DateTimer
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
            cbDone.setOnCheckedChangeListener{_, isChecked -> toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun sendNotification(context: Context){
        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel

        notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                "noto_channel",
                "Important deadlines",
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "descriptio"
            notificationManager.createNotificationChannel(notificationChannel);
        }
        todos.forEach {
            if (!it.DateTimer.contains(".")) { return@forEach }
            notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            // checking if android version is greater than oreo(API 26) or not

            notificationManager.sendNotification(
                it.title,
                it.DateTimer,
                "Important deadlines",
                context
            )
        }
    }
}