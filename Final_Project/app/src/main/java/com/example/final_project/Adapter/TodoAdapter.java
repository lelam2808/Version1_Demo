package com.example.final_project.Adapter;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.AddNewTask;
import com.example.final_project.Model.Todo;
import com.example.final_project.R;
import com.example.final_project.Utils.Database;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Todo>todoList ;
    private static ArrayList<Todo> todoListFull;
    private Database db;
    private static Context mContext;
    public TodoAdapter(ArrayList<Todo> todoList, Context mContext,Database db) {
        this.todoList = todoList;
        todoListFull = new ArrayList<>(todoList);
        this.mContext = mContext;
        this.db=db;
    }
    @Override
    public int getItemCount() {
        return todoList.size();
    }
    public void deleteItem(int position) {
        Todo item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    // truyền  dữ liệu fragment update
    public void editItem(int position){
        Bundle bundle=new Bundle();
        bundle.putInt("id",todoList.get(position).getId());
        bundle.putString("task",todoList.get(position).getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), AddNewTask.TAG);
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public TodoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    public Context getContext() {
        return mContext;
    }
    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.MyViewHolder holder, int position) {
        db.openDatabase();
        holder.task.setText(todoList.get(position).getTask());
        holder.task.setChecked(toBoolean(todoList.get(position).getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(todoList.get(position).getId(), 1);
                } else {
                    db.updateStatus(todoList.get(position).getId(), 0);
                }
            }
        });

        holder.deadline.setText(todoList.get(position).getDeadline());
    }
    public void setTasks(ArrayList<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    private boolean toBoolean(int n) {
        return n != 0;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox task;
        public TextView deadline;
        public View view;
        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            task = view.findViewById(R.id.chkTodo);
            deadline = view.findViewById(R.id.tv_deadline);
        }

    }
}
