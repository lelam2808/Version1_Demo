package com.example.project_final.Utils;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.project_final.model.Todo;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DEADLINE = "deadline";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER,"+DEADLINE + " TEXT)";

    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    public void insertTask(Todo task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(DEADLINE,task.getDeadline());
        db.insert(TODO_TABLE, null, cv);

    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task,String deadline) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DEADLINE,deadline);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public ArrayList<Todo> getAllTasks(){
        ArrayList<Todo> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        Todo task = new Todo();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDeadline(cur.getString(cur.getColumnIndex(DEADLINE)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }
    public ArrayList<Todo> getStatus() {
        ArrayList<Todo> taskList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM todo where status =1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Todo task = new Todo();
            task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
            task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            task.setDeadline(cursor.getString(cursor.getColumnIndex(DEADLINE)));
            taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }
    public ArrayList<Todo> getStatus1() {
        ArrayList<Todo> taskList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM todo where status =0", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Todo task = new Todo();
            task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
            task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            task.setDeadline(cursor.getString(cursor.getColumnIndex(DEADLINE)));
            taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }
}
