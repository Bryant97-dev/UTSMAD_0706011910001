package com.uc.myfirebaseapss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uc.myfirebaseapss.model.Student;

public class LoginStudent extends AppCompatActivity implements TextWatcher {

    Toolbar bar;
    Dialog dialog;
    TextInputLayout input_email,input_password;
    Button btn_login;
    String email,password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
        bar = findViewById(R.id.tb_login);
        mAuth= FirebaseAuth.getInstance();
        input_email = findViewById(R.id.input_email_login);
        input_password = findViewById(R.id.input_password_login);
        btn_login=findViewById(R.id.btn_login);
        input_email.getEditText().addTextChangedListener(this);
        input_password.getEditText().addTextChangedListener(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFromValue();
                signinstudent();
            }
        });
    }

    public void signinstudent (){
       //dialog.show();
       mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   // dialog.cancel();
                   mUser = mAuth.getCurrentUser();
                   Toast.makeText(LoginStudent.this, "Login succesfully", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(), MainFragment.class);
                   intent.putExtra("login", mUser);
                   startActivity(intent);
               }else {
                   Toast.makeText(LoginStudent.this, "Login failed",Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(LoginStudent.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginStudent.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetFromValue(){
        email = input_email.getEditText().getText().toString().trim();
        password= input_password.getEditText().getText().toString().trim();
        btn_login.setEnabled(!email.isEmpty() && ! password.isEmpty());
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(LoginStudent.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginStudent.this);
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