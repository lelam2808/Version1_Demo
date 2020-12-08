package com.example.project_final.Add_Task;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.project_final.DialogCloseListener;
import com.example.project_final.R;
import com.example.project_final.Utils.Database;
import com.example.project_final.model.Todo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AddNewTask extends DialogFragment {
    public static final String TAG = "ActionBottomDialog";
    //for add new task
    private EditText newTaskText;
    private ImageButton newTaskSaveButton;
    private TextView btnTime, btnDate;

    private ImageButton mVoiceBtn;
    private ImageButton mCameraBtn;
    private static final String TAG2 = "ScanVoucherFragment";



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
        newTaskText = requireView().findViewById(R.id.edtNewTask);
        newTaskSaveButton = view.findViewById(R.id.btnNewTask);
        //  newTaskSaveButton.setText("SAVE");
        //newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.maubtn));
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
            String dead=bundle.getString("dead");
            newTaskText.setText(task);
            String[] words=dead.split("\\s");
            btnTime.setText(words[0]);
            btnDate.setText(words[1]);

        }
        db=new Database(getActivity());
        db.openDatabase();


        //thêm hoặc cập nhật
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=newTaskText.getText().toString();
                String deadline = btnTime.getText().toString()+" "+btnDate.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text,deadline);//cập nhật
                }
                else{//thêm
                    Todo task=new Todo();
                    task.setTask(text);
                    task.setStatus(0);
                    task.setDeadline(deadline);
                    Toast.makeText(getContext(), "Hạn cuối công việc "+deadline, Toast.LENGTH_LONG).show();
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
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mVoiceBtn = view.findViewById(R.id.voice_btn);
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        mCameraBtn = view.findViewById(R.id.btn_cam);
        mCameraBtn.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, 1001);
            } catch (ActivityNotFoundException e) {
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

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, speak something");

        try {
            startActivityForResult(intent, 1000);
        }
        catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==1000) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            newTaskText.setText(result.get(0));
        }
        else if (requestCode == 1001 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            InputImage image = InputImage.fromBitmap(imageBitmap, 0);
            startRec(image);
        }
    }

    private void startRec(InputImage image) {
        TextRecognizer textRecognizer = TextRecognition.getClient();
        textRecognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text result) {
                Log.e(TAG, "onSuccess: result size is "+result.getTextBlocks().size() );
                String resultText = result.getText();
                StringBuilder stringBuilder = new StringBuilder();
                for (Text.TextBlock block : result.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            Point[] elementCornerPoints = element.getCornerPoints();
                            Rect elementFrame = element.getBoundingBox();
                            Log.e(TAG2, "onSuccess:  "+elementText );

                            stringBuilder.append(elementText + " ");
                        }
                    }
                }
                newTaskText.setText(stringBuilder.toString());


            }
        });
    }

}
