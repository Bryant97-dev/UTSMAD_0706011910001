package com.uc.myfirebaseapss.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uc.myfirebaseapss.AddLecturer;
import com.uc.myfirebaseapss.MainActivity;
import com.uc.myfirebaseapss.MainFragment;
import com.uc.myfirebaseapss.R;
import com.uc.myfirebaseapss.RegisterStudent;
import com.uc.myfirebaseapss.model.Student;


public class AccountFragment extends Fragment {

    Dialog dialog;
    DatabaseReference dbStudent;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    TextView out_email, out_name,out_nim,out_age, out_address,out_gender;
    String email, password, name, nim, age, address,gender;
    Student student;
    Button button_logout,edit_account;

    public AccountFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        out_email= view.findViewById(R.id.out_account_email);
        out_name= view.findViewById(R.id.out_acc_name);
        out_nim= view.findViewById(R.id.out_acc_nim);
        out_gender= view.findViewById(R.id.out_acc_gender);
        out_age= view.findViewById(R.id.out_acc_age);
        out_address= view.findViewById(R.id.out_acc_address);
        button_logout= view.findViewById(R.id.button_log_out);
        edit_account= view.findViewById(R.id.button_edit_account);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        dbStudent= FirebaseDatabase.getInstance().getReference("student").child(mUser.getUid());

        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student =snapshot.getValue(Student.class);
                if(student != null){
                    out_email.setText(student.getEmail());
                    out_name.setText(student.getName());
                    out_nim.setText(student.getNim());
                    out_gender.setText(student.getGender());
                    out_age.setText(student.getAge());
                    out_address.setText(student.getAddresss());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(getActivity(), RegisterStudent.class);
                intent.putExtra("action", "edit");
                intent.putExtra("login", "exit");
                intent.putExtra("edit_data_student", student);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_android_goldtrans_24dp)
                        .setMessage("are you sure want to log out  ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                // dialog.show();
                                mAuth.signOut();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        v.startAnimation(klik);
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                                        startActivity(intent, options.toBundle());

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
}