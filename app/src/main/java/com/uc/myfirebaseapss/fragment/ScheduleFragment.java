package com.uc.myfirebaseapss.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.adapter.CoursesAdapter;
import com.uc.myfirebaseapss.adapter.ScheduleAdapter;
import com.uc.myfirebaseapss.model.Course;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar bar;
    DatabaseReference dbSchedule;
    ArrayList<Course> listSchedule = new ArrayList<>();
    RecyclerView rv_schedule_data;


    public ScheduleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        bar = view.findViewById(R.id.toolbar_schedule);
        //((AppCompatActivity)getActivity()).setSupportActionBar(bar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbSchedule = FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("course");
        rv_schedule_data = view.findViewById(R.id.rv_schedule);

       fetchScheduleData();
    }

    public void fetchScheduleData(){
        dbSchedule.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSchedule.clear();
                rv_schedule_data.setAdapter(null);
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Course course = childSnapshot.getValue(Course.class);
                    listSchedule.add(course);

                }
                showCoursesData(listSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showCoursesData(final ArrayList<Course> list){
        rv_schedule_data.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity());
        scheduleAdapter.setListSchedule(list);
        rv_schedule_data.setAdapter(scheduleAdapter);


    }
}