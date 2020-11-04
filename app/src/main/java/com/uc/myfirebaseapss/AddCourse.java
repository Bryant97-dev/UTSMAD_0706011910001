package com.uc.myfirebaseapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Lecturer;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCourse extends AppCompatActivity {

    Toolbar bar;
    Dialog dialog;
    Spinner spinner_day, spinner_start, spinner_end, spinner_lecturer;
    TextInputLayout input_subject;
    String subject = "", day = "", start="" , end = "", idlecturer = "";
    String action="";
    Button btn_course;
    DatabaseReference dbLecturer;
    DatabaseReference dbCourse;
    private ArrayList <String> arraylecturer = new ArrayList<>();
    Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        bar = findViewById(R.id.tb_course);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_course =findViewById(R.id.btn_add_course);
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        dbCourse = FirebaseDatabase.getInstance().getReference();
        spinner_lecturer = findViewById(R.id.spinner_lecturer_course);
        showLecturerSpinner();
        input_subject = findViewById(R.id.input_subject_course);
        input_subject.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subject= input_subject.getEditText().getText().toString().trim();
                if (!subject.isEmpty()){
                    btn_course.setEnabled(true);
                }else{
                    btn_course.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equals("add")){
            getSupportActionBar().setTitle("Add course");
            btn_course.setText("Add course");
            btn_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCourse(subject, day , start, end, idlecturer);
                }
            });
        }else{ //saat activity dari lecturer detail & mau mengupdate data
            getSupportActionBar().setTitle("Edit Course");
            course = intent.getParcelableExtra("edit_data_course");
            input_subject.getEditText().setText(course.getSubject());
            //String[] timeStartArr = getResources().getStringArray(R.array.timeStart);
           // String[] timeFinArr = getResources().getStringArray(R.array.timeFin);

           // spinner_day.setAdapter(dayPos);

            
            btn_course.setText("Edit Course");
            btn_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // dialog.show();
                    subject = input_subject.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("subject", subject);
                    params.put("day", day);
                    params.put("start", start);
                    params.put("end", end);
                    params.put("lecturer", idlecturer);
                    dbCourse.child("course").child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // dialog.cancel();
                            Intent intent;
                            intent = new Intent(AddCourse.this, CourseData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }



        /*btn_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse(subject, day , start, end, idlecturer);
            }
        });*/

        spinner_day = findViewById(R.id.spinner_day_course);
        ArrayAdapter<CharSequence> adapterday = ArrayAdapter.createFromResource(AddCourse.this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adapterday);
        if (action.equalsIgnoreCase("edit")) {
            int indexd = adapterday.getPosition(course.getDay());
            spinner_day.setSelection(indexd);
        }
        //spinner_day.setSelection(dayPos);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
               /* if(action.equalsIgnoreCase("edit")){

                    String[] dayArr = getResources().getStringArray(R.array.day_array);
                    int dayPos = 0;
                for (int i = 0; i < dayArr.length; i++) {
                    Log.d("CheckFalse", "jalan gak");
                    Log.d("check", dayArr[i] + "");
                    Log.d("checkwoi", day + "");
                    if (day.equals(dayArr[i])) {
                        dayPos = i;
                        Log.d("CheckFalse", "trueaf");
                        break;
                    }
                    spinner_day.setSelection(dayPos);
                }
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_end = findViewById(R.id.spinner_end_course);
        spinner_start = findViewById(R.id.spinner_start_course);
        ArrayAdapter<CharSequence> adapterstart = ArrayAdapter.createFromResource(AddCourse.this,
                R.array.jam_start_array, android.R.layout.simple_spinner_item);
        adapterstart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adapterstart);
        if (action.equalsIgnoreCase("edit")) {
            int indexs = adapterstart.getPosition(course.getStart());
            spinner_start.setSelection(indexs);
        }
       /* spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start = parent.getItemAtPosition(position).toString();
                ArrayAdapter<CharSequence> adapterend = null;
                if(position==0){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
                }else if(position==1){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
                }else if(position==2){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
                }else if(position==3){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
                }else if(position==4){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
                }else if(position==5){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
                }else if(position==6){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
                }else if(position==7){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
                }else if(position==8){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
                }else if(position==9){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
                }else if(position==10){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
                }else if(position==11){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
                }else if(position==12){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
                }else if(position==13){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
                }else if(position==14){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
                }else if(position==15){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
                }else if(position==16){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
                }else if(position==17){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
                }else if(position==18){
                    adapterend = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
                }

                adapterend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_end.setAdapter(adapterend);
                if (action.equalsIgnoreCase("edit")) {
                    int indexe = adapterend.getPosition(course.getEnd());
                    spinner_end.setSelection(indexe);
                }
                spinner_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        end = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void addCourse (String csubject, String cday, String cstart, String cend, String clecturer){
        //dialog.show();
        String cid = dbCourse.child("course").push().getKey();
        Course course = new Course(cid,csubject,cday ,cstart, cend, clecturer);
        dbCourse.child("course").child(cid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //dialog.cancel();
                Toast.makeText(AddCourse.this, "Add Course Successfully", Toast.LENGTH_SHORT).show();
                /*input_subject.getEditText().setText("");
                spinner_start.check
                input_expertise.getEditText().setText("");
                rg_gender.check(R.id.rad_male_lecturer);*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.cancel();
                Toast.makeText(AddCourse.this, "Add Lecturer Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showLecturerSpinner () {
        dbLecturer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylecturer.clear();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    arraylecturer.add(childSnapshot.child("name").getValue(String.class));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddCourse.this,R.layout.support_simple_spinner_dropdown_item,arraylecturer);
                spinner_lecturer.setAdapter(arrayAdapter);
                if (action.equalsIgnoreCase("edit")) {
                    int indexl = arrayAdapter.getPosition(course.getLecturer());
                    spinner_lecturer.setSelection(indexl);
                }
                spinner_lecturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        idlecturer = spinner_lecturer.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(AddCourse.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.course_list){
            Intent intent;
            intent = new Intent(AddCourse.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddCourse.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddCourse.this);
        startActivity(intent, options.toBundle());
        finish();
    }

}