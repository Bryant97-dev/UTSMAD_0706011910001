package com.uc.myfirebaseapss.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.AddCourse;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.fragment.ScheduleFragment;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CardViewViewHolder>{

    Dialog dialog;
    DatabaseReference dbCourse,dbCourses,mdatabase;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ArrayList<Student> listStudent = new ArrayList<>();
    boolean conflict = false;
    MutableLiveData <Course> cekCourse = new MutableLiveData<>();
    public MutableLiveData <Course> getCekCourse() {return cekCourse;}
    //Course course;


    private Context context;
    private ArrayList<Course> listCourses;
    private ArrayList<Course> getListCourses() {
        return listCourses;
    }
    public void setListCourses(ArrayList<Course> listCourses) {
        this.listCourses = listCourses;
    }
    public CoursesAdapter(Context context) {
        this.context = context;
    }
    //private OnItemCLickListener mlistener;

    //Membuat Interfece
   // public interface OnItemCLickListener{
    //    void onDeleteData(Student student, int position);
  //  }

   // //Deklarasi objek dari Interfece
   // public void setOnItemClickListener (OnItemCLickListener listener){
    //    mlistener = listener;
   // }


    @NonNull
    @Override
    public CoursesAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_adapter, parent, false);
        return new CoursesAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final CoursesAdapter.CardViewViewHolder holder, int position) {
     //dbCourses = FirebaseDatabase.getInstance().getReference("course");
      //dbCourse =  FirebaseDatabase.getInstance().getReference("student");
        mdatabase =FirebaseDatabase.getInstance().getReference();


        final Course course = getListCourses().get(position);
        holder.cs_subject.setText(course.getSubject());
        holder.cs_day.setText(course.getDay());
        holder.cs_start.setText(course.getStart());
        holder.cs_end.setText(course.getEnd());
        holder.cs_lecturer.setText(course.getLecturer());
        /*dbCourses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    course = childSnapshot.getValue(Course.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        holder.btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                conflictChecker(course);
            }
        });
    }

    private void conflictChecker (final Course course){
        final int courseStartChoice = Integer.parseInt(course.getStart().replace(":",""));
        final int courseEndChoice = Integer.parseInt(course.getEnd().replace(":",""));

        Log.d ("choosenDay", course.getDay());
        Log.d ("courseStartChoice", courseStartChoice+"");
        Log.d ("courseEndChoice", courseEndChoice+"");

        dbCourse = FirebaseDatabase.getInstance().getReference("student");
        dbCourse.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("course").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    conflict= false;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Course coursetest = childSnapshot.getValue(Course.class);
                        int courseStart = Integer.parseInt(coursetest.getStart().replace(":", ""));
                        int courseEnd = Integer.parseInt(coursetest.getEnd().replace(":", ""));

                        Log.d("CourseDay", coursetest.getDay());
                        Log.d("CourseStart", courseStart + "");
                        Log.d("CourseEnd", courseEnd + "");

                        if (course.getDay().equalsIgnoreCase(coursetest.getDay())) {
                            if (courseStartChoice >= courseStart && courseStartChoice < courseEnd) {
                                conflict = true;
                                Log.d("check2", "true");
                                break;
                            }
                            if (courseEndChoice >= courseStart && courseEndChoice < courseEnd) {
                                conflict = true;
                                Log.d("check3", "true");
                                break;
                            }
                            Log.d("check1", "true");
                        } else {
                            conflict = false;
                            Log.d("CheckFalse", "false");
                        }

                    }
                Log.d("check",conflict+"");

                if (conflict ){
                    new AlertDialog.Builder(context)
                            .setTitle("Warning")
                            .setMessage("Course conflict schedule with other !")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }else{
                    cekCourse.setValue(course);
                    // Toast.makeText(context, "add course successfully !", Toast.LENGTH_SHORT).show();
                }

               // Log.d("check","false");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    @Override
    public int getItemCount() {
        return getListCourses().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView cs_subject, cs_day, cs_start, cs_end, cs_lecturer;
        Button btn_enroll;
       // DatabaseReference dbStudent;
       // Student student;

        CardViewViewHolder(View itemView) {
            super(itemView);
            cs_subject = itemView.findViewById(R.id.courses_name);
            cs_day = itemView.findViewById(R.id.courses_day);
            cs_start = itemView.findViewById(R.id.courses_start);
            cs_end = itemView.findViewById(R.id.courses_end);
            cs_lecturer = itemView.findViewById(R.id.courses_lecturer);
            btn_enroll = itemView.findViewById(R.id.courses_enroll);
           // dbStudent= FirebaseDatabase.getInstance().getReference("student");


        }
    }
}
