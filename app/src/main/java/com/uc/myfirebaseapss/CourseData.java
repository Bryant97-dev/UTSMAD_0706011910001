package com.uc.myfirebaseapss;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.adapter.CourseAdapter;
import com.uc.myfirebaseapss.adapter.StudentAdapter;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class CourseData extends AppCompatActivity {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbCourse;
    ArrayList<Course> listCourse = new ArrayList<>();
    RecyclerView rv_course_data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_data);
        bar = findViewById(R.id.tb_course_data);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        rv_course_data = findViewById(R.id.rv_course_data);

        fetchCourseData();

    }


    public void fetchCourseData(){
        dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              listCourse.clear();
                rv_course_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                   Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);

                }
                showCourseData(listCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCourseData(final ArrayList<Course> list){
        rv_course_data.setLayoutManager(new LinearLayoutManager(CourseData.this));
        CourseAdapter courseAdapter = new CourseAdapter(CourseData.this);
        courseAdapter.setListCourse(list);
        rv_course_data.setAdapter(courseAdapter);





    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(CourseData.this, AddCourse.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(CourseData.this, AddCourse.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CourseData.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
