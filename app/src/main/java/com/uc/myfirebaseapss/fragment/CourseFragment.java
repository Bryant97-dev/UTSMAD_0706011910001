package com.uc.myfirebaseapss.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.CourseData;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.adapter.CourseAdapter;
import com.uc.myfirebaseapss.adapter.CoursesAdapter;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;



public class CourseFragment extends Fragment {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbCourse;
    ArrayList<Course> listCourses = new ArrayList<>();
    RecyclerView rv_course_data;

    public CourseFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        bar = view.findViewById(R.id.tooblar_courses);
        //((AppCompatActivity)getActivity()).setSupportActionBar(bar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbCourse = FirebaseDatabase.getInstance().getReference("course");
        rv_course_data = view.findViewById(R.id.rv_courses_data);

        fetchCoursesData();
    }

    public void fetchCoursesData(){
        dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourses.clear();
                rv_course_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listCourses.add(course);

                }
                showCoursesData(listCourses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCoursesData(final ArrayList<Course> list){
        rv_course_data.setLayoutManager(new LinearLayoutManager(getActivity()));
        CoursesAdapter coursesAdapter = new CoursesAdapter(getActivity());
        coursesAdapter.setListCourses(list);
        rv_course_data.setAdapter(coursesAdapter);

        final Observer <Course> courseObserver = (course) -> {
            FirebaseDatabase.getInstance().getReference().child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("course").child(course.getId())
                    .setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "add course successfully !", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "add course failure !", Toast.LENGTH_SHORT).show();
                }
            });
        };
    coursesAdapter.getCekCourse().observe(this, courseObserver);
    }
}