package com.example.countyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView texte;
    private EditText mail;
    private EditText pass;
    private Button login;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texte =findViewById(R.id.signup_text);
        login=findViewById(R.id.btn_login);
        mail=findViewById(R.id.email_login);
        pass=findViewById(R.id.password_login);
        mDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        texte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail= mail.getText().toString().trim();
                String mPass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail)){
                    mail.setError("Required field");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    pass.setError("Required field");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                 mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

if(task.isSuccessful()){
    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    Toast.makeText(getApplicationContext(),"successiful",Toast.LENGTH_SHORT).show();
    mDialog.dismiss();

                     }else{
    Toast.makeText(getApplicationContext(),"failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
    mDialog.dismiss();

                     }


                     }
                 });
            }
        });




















    }


}
