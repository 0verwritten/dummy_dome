package com.example.scroll_next

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_1.*
import java.io.FileNotFoundException

class Fragment1 : Fragment() {
    private var title = ""
    private var time = ""
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          return inflater.inflate(R.layout.fragment_1, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todoAdapter = TodoAdapter(mutableListOf())
        rvTodoItems.adapter = todoAdapter
        try{
            Log.w("restoring", " dsf");
            todoAdapter.loadFromFile(requireActivity());
            println(todoAdapter.toString())
        }catch(e: FileNotFoundException){
            Log.e("file", e.toString());}
        rvTodoItems.layoutManager = LinearLayoutManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("Basement1"){key, bundle ->
            title = bundle.getString("task")!!
        }
        setFragmentResultListener("Basement2"){key, bundle ->
            time = bundle.getString("dt")!!
            if(title.isNotEmpty()){
                val todo = Todo(title,time,false)
                todoAdapter.addTodo(todo,requireContext())
                title=""
            }
        }
        btnDeleteDoneTodo.setOnClickListener {
            todoAdapter.deleteDoneTodos(requireContext())
        }
        btnSend.setOnClickListener {

            todoAdapter.sendNotification(requireContext());
        };
    }
}