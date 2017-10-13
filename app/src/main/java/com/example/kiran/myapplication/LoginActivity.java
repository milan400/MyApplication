package com.example.kiran.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mloginemail;

    private EditText mloginpassword;

    private Button mloginbtn;

    private FirebaseAuth mauth;

    private DatabaseReference mdatabaseuser;

    private ProgressDialog mprogress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mauth = FirebaseAuth.getInstance();

        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabaseuser.keepSynced(true);

        mprogress = new ProgressDialog(this);

        mloginemail = (EditText)findViewById(R.id.loginemailfield);
        mloginpassword = (EditText)findViewById(R.id.loginpasswordfield);
        mloginbtn = (Button) findViewById(R.id.loginbtn);

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLogin();
            }
        });


    }

    private void checkLogin() {

        String email = mloginemail.getText().toString().trim();
        String password = mloginpassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mprogress.setMessage("Checking Login...");
            mprogress.show();

            mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        mprogress.dismiss();
                        CheckUserExist();


                    }else{
                        mprogress.dismiss();
                        Toast.makeText(LoginActivity.this,"Error during Login",Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

    private void CheckUserExist() {

        final String user_id = mauth.getCurrentUser().getUid();

        mdatabaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent mainIntent = new Intent(LoginActivity.this , MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);




                }else{

                    Intent setupIntent = new Intent(LoginActivity.this , SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
