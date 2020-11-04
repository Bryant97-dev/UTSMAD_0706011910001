package com.uc.myfirebaseapss.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.myfirebaseapss.AddCourse;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.RegisterStudent;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CardViewViewHolder>{

    Dialog dialog;
    DatabaseReference dbCourse;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private Context context;
    private ArrayList<Course> listCourse;
    private ArrayList<Course> getListCourse() {
        return listCourse;
    }
    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }
    public CourseAdapter(Context context) {
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
    public CourseAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter, parent, false);
        return new CourseAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final CourseAdapter.CardViewViewHolder holder, int position) {
      dbCourse = FirebaseDatabase.getInstance().getReference("course");


        final Course course = getListCourse().get(position);
        holder.cs_subject.setText(course.getSubject());
        holder.cs_day.setText(course.getDay());
        holder.cs_start.setText(course.getStart());
        holder.cs_end.setText(course.getEnd());
        holder.cs_lecturer.setText(course.getLecturer());

        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(context, AddCourse.class);
                intent.putExtra("action", "edit");
                intent.putExtra("edit_data_course", course);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

       holder.button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_android_goldtrans_24dp)
                        .setMessage("Are you sure to delete "+course.getSubject()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                               // dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //dialog.cancel();
                                                dbCourse.child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                      //  dialogInterface.cancel();
                                                    }
                                                });
                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();

            }
        });


    }




    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView cs_subject, cs_day, cs_start, cs_end, cs_lecturer;
        ImageButton button_del,button_edit;
       // DatabaseReference dbStudent;
       // Student student;

        CardViewViewHolder(View itemView) {
            super(itemView);
            cs_subject = itemView.findViewById(R.id.course_name);
            cs_day = itemView.findViewById(R.id.course_day);
            cs_start = itemView.findViewById(R.id.course_start);
            cs_end = itemView.findViewById(R.id.course_end);
            cs_lecturer = itemView.findViewById(R.id.course_lecturer);
            button_del = itemView.findViewById(R.id.button_course_delete);
            button_edit = itemView.findViewById(R.id.button_course_edit);
           // dbStudent= FirebaseDatabase.getInstance().getReference("student");


        }
    }
}
