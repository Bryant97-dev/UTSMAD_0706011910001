package com.uc.myfirebaseapss.adapter;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.AddCourse;
import com.uc.myfirebaseapss.AddLecturer;
import com.uc.myfirebaseapss.ItemClickSupport;
import com.uc.myfirebaseapss.LecturerData;
import com.uc.myfirebaseapss.LecturerDetail;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.RegisterStudent;
import com.uc.myfirebaseapss.StudentData;
import com.uc.myfirebaseapss.model.Lecturer;
import com.uc.myfirebaseapss.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder>{

    Dialog dialog;
    DatabaseReference dbStudent;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Context context;
    private ArrayList<Student> listStudent;
    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }
    public StudentAdapter(Context context) {
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
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, int position) {
      dbStudent = FirebaseDatabase.getInstance().getReference("student");


        final Student student = getListStudent().get(position);
        holder.sbl_email.setText(student.getEmail());
        holder.sbl_name.setText(student.getName());
        holder.sbl_nim.setText(student.getNim());
        holder.sbl_gender.setText(student.getGender());
        holder.sbl_age.setText(student.getAge());
        holder.sbl_address.setText(student.getAddresss());

        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(context, RegisterStudent.class);
                intent.putExtra("action", "edit");
                intent.putExtra("edit_data_student", student);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

       holder.button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                mAuth = FirebaseAuth.getInstance();
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_android_goldtrans_24dp)
                        .setMessage("Are you sure to delete "+student.getName()+" data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                               // dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //dialog.cancel();
                                       mAuth.signInWithEmailAndPassword(student.getEmail(),student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               mUser  = mAuth.getCurrentUser();
                                                dbStudent.child(student.getUId()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                                                      //  dialogInterface.cancel();
                                                    }
                                                });
                                               mAuth.getCurrentUser().delete();

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
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView sbl_name, sbl_age, sbl_email, sbl_address,sbl_password, sbl_nim, sbl_gender;
        ImageButton button_del,button_edit;
       // DatabaseReference dbStudent;
       // Student student;

        CardViewViewHolder(View itemView) {
            super(itemView);
            sbl_name = itemView.findViewById(R.id.student_name);
            sbl_gender = itemView.findViewById(R.id.student_gender);
            sbl_age = itemView.findViewById(R.id.student_age);
            sbl_email = itemView.findViewById(R.id.student_email);
            sbl_address = itemView.findViewById(R.id.student_address);
            sbl_nim = itemView.findViewById(R.id.student_nim);
            button_del = itemView.findViewById(R.id.button_student_delete);
           button_edit = itemView.findViewById(R.id.button_student_edit);
           // dbStudent= FirebaseDatabase.getInstance().getReference("student");


        }
    }
}
