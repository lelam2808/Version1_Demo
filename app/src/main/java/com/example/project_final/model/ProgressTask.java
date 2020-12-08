package com.example.project_final.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Filterable;

import com.example.project_final.Adapter.TodoAdapter;
import com.example.project_final.R;
import com.example.project_final.Utils.Database;
import com.example.project_final.model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class ProgressTask extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private TodoAdapter tasksAdapter;
    private ArrayList<Todo> taskList;
    private Database dbDatabase;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_task);
        dbDatabase = new Database(this);
        dbDatabase.openDatabase();

        //new máº£ng
        taskList = new ArrayList<>();
        taskList.addAll(dbDatabase.getStatus());
        Collections.reverse(taskList);

        //táº¡o reyclerview
        recyclerView = findViewById(R.id.taskRecycleViewSuceess);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        tasksAdapter = new TodoAdapter(taskList, this, dbDatabase);
        recyclerView.setAdapter(tasksAdapter);
        //add csdl vÃ o recylerview


        //khá»Ÿi táº¡o search view
        searchView=findViewById(R.id.searchViewSuccess);
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        tasksAdapter.getFilter().filter(newText);
        return false;
    }
}