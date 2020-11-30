package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.final_project.Adapter.TodoAdapter;
import com.example.final_project.Model.Todo;
import com.example.final_project.Utils.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView recyclerView;
    private TodoAdapter tasksAdapter;
    private ArrayList<Todo> taskList;
    private Database dbDatabase;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //open databse
        dbDatabase = new Database(this);
        dbDatabase.openDatabase();

        //new mảng
        taskList = new ArrayList<>();
        taskList.addAll(dbDatabase.getAllTasks());
        Collections.reverse(taskList);

        //tạo reyclerview
        recyclerView = findViewById(R.id.taskRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        tasksAdapter = new TodoAdapter(taskList, this, dbDatabase);
        recyclerView.setAdapter(tasksAdapter);
        //add csdl vào recylerview


        fab = findViewById(R.id.fab_home);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //thêm công việc
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


    }

    //xử lí sự kiện đóng hộp thoại
    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        taskList = dbDatabase.getAllTasks();
        Collections.reverse(taskList);
        ArrayList<Todo> task = dbDatabase.getAllTasks();
        if (task.size() != taskList.size())
            startActivity(getIntent());
        else {
            tasksAdapter.setTasks(taskList);
        }
    }
}