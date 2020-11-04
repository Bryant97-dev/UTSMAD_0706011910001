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
import android.widget.Button;
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
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.fragment.ScheduleFragment;
import com.uc.myfirebaseapss.model.Course;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CardViewViewHolder>{

    Dialog dialog;
    DatabaseReference dbCourse, mdatabase;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);


    private Context context;
    private ArrayList<Course> listSchedule;
    private ArrayList<Course> getListCourses() { return listSchedule; }
    public void setListSchedule(ArrayList<Course> listSchedule) { this.listSchedule = listSchedule; }
    public ScheduleAdapter(Context context) {
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
    public ScheduleAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_adapter, parent, false);
        return new ScheduleAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ScheduleAdapter.CardViewViewHolder holder, int position) {
    //  dbCourse = FirebaseDatabase.getInstance().getReference("course");
        mdatabase = FirebaseDatabase.getInstance().getReference();


        final Course course = getListCourses().get(position);
        holder.cs_subject.setText(course.getSubject());
        holder.cs_day.setText(course.getDay());
        holder.cs_start.setText(course.getStart());
        holder.cs_end.setText(course.getEnd());
        holder.cs_lecturer.setText(course.getLecturer());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
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
                                        mdatabase.child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("course").child(course.getId()).removeValue(new DatabaseReference.CompletionListener() {
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
        return getListCourses().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView cs_subject, cs_day, cs_start, cs_end, cs_lecturer;
        ImageButton btn_delete;
       // DatabaseReference dbStudent;
       // Student student;

        CardViewViewHolder(View itemView) {
            super(itemView);
            cs_subject = itemView.findViewById(R.id.schedule_name);
            cs_day = itemView.findViewById(R.id.schedule_day);
            cs_start = itemView.findViewById(R.id.schedule_start);
            cs_end = itemView.findViewById(R.id.schedule_end);
            cs_lecturer = itemView.findViewById(R.id.schedule_lecturer);
            btn_delete = itemView.findViewById(R.id.button_schedule_delete);
           // dbStudent= FirebaseDatabase.getInstance().getReference("student");


        }
    }
}
