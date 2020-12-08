package com.example.project_final;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.project_final.Adapter.TodoAdapter;
import com.example.project_final.Add_Task.AddNewTask;
import com.example.project_final.Utils.Database;
import com.example.project_final.model.Calendar_Ac;
import com.example.project_final.model.ProgressTask;
import com.example.project_final.model.Todo;
import com.example.project_final.model.Unfinish_Progress;
import com.example.project_final.model.login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogCloseListener, SearchView.OnQueryTextListener {
    // lam
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView set_email;
    FirebaseAuth mAuth;

    // hung
    private RecyclerView recyclerView;
    private TodoAdapter tasksAdapter;
    private ArrayList<Todo> taskList;
    private Database dbDatabase;
    private FloatingActionButton fab;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // lam

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_toolbar);
        searchView=findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
       try {
           set_email=findViewById(R.id.dc_eml);
       }catch (Exception e)
       {
           Log.d("ktemail",e.getMessage());
       }
        // toolbar

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        // moi them

        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // hien thi email khi dang nhap vao
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            try {
                Toast.makeText(this,""+currentUser.getEmail(),Toast.LENGTH_LONG).show();
                set_email.setText(currentUser.getEmail().toString());
            }catch (Exception e)
            {
                Log.d("tenemail",e.getMessage());
            }

        }

        // hung
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
    // keo de mo thanh nav
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    // chon trong nav
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home: {
                Toast.makeText(this,"MAN HINH HOME",Toast.LENGTH_SHORT).show();
                Intent itMain=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(itMain);
                break;
            }
            case R.id.nav_comple:
            {
                Toast.makeText(this,"MAN HINH CONG VIEC HOAN THANH",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProgressTask.class));
                break;

            }
            case R.id.nav_alarm:
            {
                Toast.makeText(this,"MAN HINH CONG VIEC HOAN THANH",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Unfinish_Progress.class));
                break;

            }
            case R.id.nav_calendar:
            {
                Toast.makeText(this,"MAN HINH CALENDAR",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Calendar_Ac.class));
                break;
            }
            case R.id.logout:
            {
                Toast.makeText(this,"LOGOUT",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();;
                Intent itLogin=new Intent(getApplicationContext(), login.class);
                startActivity(itLogin);
            }
            default:
                break;
        }
        return true;
    }
    // chu y phan nay!!!
    @Override
    public boolean onQueryTextSubmit(String query) {
        tasksAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        tasksAdapter.getFilter().filter(newText);
        return false;

    }
}
