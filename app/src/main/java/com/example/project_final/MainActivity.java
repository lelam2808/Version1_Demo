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
import com.example.project_final.model.Todo;
import com.example.project_final.model.login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        searchView = findViewById(R.id.searchView);
       // searchView.setOnQueryTextListener(this);
        try {
            set_email = findViewById(R.id.dc_eml);
        } catch (Exception e) {
            Log.d("ktemail", e.getMessage());
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
        if (currentUser != null) {
            try {
                Toast.makeText(this, "" + currentUser.getEmail(), Toast.LENGTH_LONG).show();
                set_email.setText(currentUser.getEmail().toString());
            } catch (Exception e) {
                Log.d("tenemail", e.getMessage());
            }

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
                break;
            }
            case R.id.nav_home1:
            {
                Toast.makeText(this,"MAN HINH NOI DUNG",Toast.LENGTH_SHORT).show();

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


}
