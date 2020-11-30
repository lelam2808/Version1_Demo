package com.example.final_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.final_project.Adapter.TodoAdapter;
import com.example.final_project.Model.Todo;
import com.example.final_project.Utils.Database;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Objects;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

public class AddNewTask extends DialogFragment {
    public static final String TAG = "ActionBottomDialog";
    //for add new task
    private EditText newTaskText;
    private Button newTaskSaveButton, btnTime, btnDate;

    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    private int lastSelectedYear = -1;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private Database db;
    public static AddNewTask newInstance(){
        return new AddNewTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.edtNewTask);
        newTaskSaveButton = view.findViewById(R.id.btnNewTask);
        newTaskSaveButton.setText("SAVE");
        newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));

        btnDate = view.findViewById(R.id.btn_date);
        btnTime = view.findViewById(R.id.btn_time);
        //sự kiện cho giờ
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectTime();
            }
        });
        //sự kiện cho ngày
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectDate();
            }
        });

        boolean isUpdate=false;
        //bundle nhận từ sự kiện kéo chuột, ok thì set biến isUpdate =true
        final Bundle bundle=getArguments();
        if(bundle!=null){
            isUpdate=true;
            String task=bundle.getString("task");
            newTaskText.setText(task);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        }
        db=new Database(getActivity());
        db.openDatabase();


        //thêm hoặc cập nhật
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=newTaskText.getText().toString();
                String deadline = btnDate.getText().toString() + " " + btnTime.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text);//cập nhật
                }
                else{//thêm
                    Todo task=new Todo();
                    task.setTask(text);
                    task.setStatus(0);
                    task.setDeadline(deadline);
                    Toast.makeText(getContext(), deadline, Toast.LENGTH_LONG).show();
                    db.insertTask(task);
                }
                dismiss();
            }
        });
        newTaskSaveButton.setEnabled(false);
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

    private void buttonSelectTime()  {
        if(this.lastSelectedHour == -1)  {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }

        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btnTime.setText(hourOfDay + ":" + minute );
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        // TimePicker in Clock Mode (Default):

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                timeSetListener, lastSelectedHour, lastSelectedMinute, true);

        // Show
        timePickerDialog.show();
    }

    private void buttonSelectDate() {
        if (lastSelectedYear == -1){
            final Calendar c = Calendar.getInstance();
            this.lastSelectedYear = c.get(Calendar.YEAR);
            this.lastSelectedMonth = c.get(Calendar.MONTH);
            this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        }

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                btnDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

        datePickerDialog.show();
        }

}
