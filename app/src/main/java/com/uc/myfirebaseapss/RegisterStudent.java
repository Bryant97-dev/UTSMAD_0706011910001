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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uc.myfirebaseapss.model.Student;

import java.util.HashMap;
import java.util.Map;

public class RegisterStudent extends AppCompatActivity implements TextWatcher {

    TextInputLayout input_email, input_pass,input_name,input_nim,input_age, input_address;
    String email, password, name, nim, age, address,gender;
    String uid="";
    Button btn_register;
    RadioGroup rg_gender;
    RadioButton radioButton;
    Toolbar bar;
    Dialog dialog;
    Student student;
    private DatabaseReference mdatabase,mdatabase2;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String action,login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        bar = findViewById(R.id.tb_reg_student);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog= Glovar.loadingDialog(RegisterStudent.this);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference("student");
        mdatabase2 = FirebaseDatabase.getInstance().getReference();

        input_email = findViewById(R.id.input_email_reg_student);
        input_pass = findViewById(R.id.input_password_reg);
        input_name = findViewById(R.id.input_name_reg_student);
        input_nim = findViewById(R.id.input_nim_reg_student);
        input_age = findViewById(R.id.input_age_reg_student);
        input_address = findViewById(R.id.input_address_reg_student);

        input_email.getEditText().addTextChangedListener(this);
        input_pass.getEditText().addTextChangedListener(this);
        input_name.getEditText().addTextChangedListener(this);
        input_nim.getEditText().addTextChangedListener(this);
        input_age.getEditText().addTextChangedListener(this);
        input_address.getEditText().addTextChangedListener(this);

        btn_register = findViewById(R.id.btn_reg_student);
        rg_gender = findViewById(R.id.radg_gender_reg_student);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });

        Intent intent = getIntent();
        //login = intent.getStringExtra("login");
        action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("add")){
            getSupportActionBar().setTitle(R.string.regstudent);
            btn_register.setText(R.string.regstudent);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 GetFromValue();
                   addStudent();

                }
            });
        }else if (action.equalsIgnoreCase("edit")){
            getSupportActionBar().setTitle("Edit student");
            student = intent.getParcelableExtra("edit_data_student");
            input_email.getEditText().setText(student.getEmail());
            input_email.getEditText().setEnabled(false);
            input_pass.getEditText().setText(student.getPassword());
            input_pass.getEditText().setEnabled(false);
            input_name.getEditText().setText(student.getName());
            input_nim.getEditText().setText(student.getNim());
            if(student.getGender().equalsIgnoreCase("male")){
              rg_gender.check(R.id.rad_male_reg_student);
            }else{
               rg_gender.check(R.id.rad_female_reg_student);
           }
            input_age.getEditText().setText(student.getAge());
            input_address.getEditText().setText(student.getAddresss());
            btn_register.setText("Edit Student");
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    email = input_email.getEditText().getText().toString().trim();
                    password = input_pass.getEditText().getText().toString().trim();
                    name = input_name.getEditText().getText().toString().trim();
                    nim= input_nim.getEditText().getText().toString().trim();
                    age = input_age.getEditText().getText().toString().trim();
                    address = input_address.getEditText().getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                   params.put("email", email);
                    params.put("password", password);
                    params.put("name", name);
                    params.put("nim", nim);
                    params.put("gender", gender);
                    params.put("age", age);
                    params.put("addresss", address);
                    //mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      //  @Override
                     //   public void onComplete(@NonNull Task<AuthResult> task) {
                            mdatabase2.child("student").child(student.getUId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.cancel();
                                    Intent intent;
                                    intent = getIntent();
                                    login = intent.getStringExtra("login");
                                    Log.d("checklogin", login+"");
                                    if(login == null){
                                        login = "main2";
                                        Log.d("checkloginmain", login+"");
                                    }
                                    if (login.equalsIgnoreCase("exit")) {
                                        intent = new Intent(RegisterStudent.this, MainFragment.class);
                                    }else if(login.equalsIgnoreCase("main2")) {
                                        intent = new Intent(RegisterStudent.this, StudentData.class);
                                    }
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterStudent.this);
                                    startActivity(intent, options.toBundle());
                                    finish();
                                //}
                           // });
                        }
                    });
                }
            });
        }
    }

    public void  addStudent(){
        GetFromValue();
       // dialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterStudent.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   // dialog.cancel();
                    uid=mAuth.getCurrentUser().getUid();
                    student = new Student (uid,email,password,name,nim,gender,age,address);
                    mdatabase.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterStudent.this, "student register susccessful",Toast.LENGTH_SHORT).show();
                        }
                    });
                    mAuth.signOut();
                }else {
                    Toast.makeText(RegisterStudent.this, "student register failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void GetFromValue(){
        name= input_name.getEditText().getText().toString().trim();
        email = input_email.getEditText().getText().toString().trim();
        nim= input_nim.getEditText().getText().toString().trim();
        age= input_age.getEditText().getText().toString().trim();
        address= input_address.getEditText().getText().toString().trim();
        password= input_pass.getEditText().getText().toString().trim();
        btn_register.setEnabled(!name.isEmpty() && ! nim.isEmpty()&& ! age.isEmpty()&& ! address.isEmpty()&& ! password.isEmpty()&& ! email.isEmpty());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent intent;
            intent = getIntent();
           login = intent.getStringExtra("login");
           Log.d("checklogin", login+"");
           if(login == null){
              login = "main";
               Log.d("checkloginmain", login+"");
           }
             if (login.equalsIgnoreCase("exit")) {
           intent = new Intent(RegisterStudent.this, MainFragment.class);
             }else if(login.equalsIgnoreCase("main")){
            intent = new Intent(RegisterStudent.this, MainActivity.class);
           }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterStudent.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }else if(id == R.id.student_list){
            Intent intent;
            intent = new Intent(RegisterStudent.this, StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterStudent.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
       // Log.d("checklogin", login);
      // if (login.equalsIgnoreCase("exit")) {
         // intent = new Intent(RegisterStudent.this, MainFragment.class);
       //    Log.d("checklogin", "berhasil yesssssss");
       // }else {
            intent = new Intent(RegisterStudent.this, MainActivity.class);
      // }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterStudent.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        GetFromValue();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}