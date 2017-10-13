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
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mnamefield;

    private  EditText memailfied;

    private EditText mpasswordfield;

    private Button mregisterbtn;

    private FirebaseAuth mauth;

    private ProgressDialog mprogress;

    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mauth = FirebaseAuth.getInstance();

        mprogress = new ProgressDialog(this);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        mnamefield = (EditText)findViewById(R.id.namefield);

        memailfied = (EditText)findViewById(R.id.emailfield);

        mpasswordfield = (EditText)findViewById(R.id.passwordfield);

        mregisterbtn = (Button)findViewById(R.id.registerbtn);

        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startRegister();

            }
        });

    }

    private void startRegister() {

        final String name = mnamefield.getText().toString().trim();

        final String email = memailfied.getText().toString().trim();

        String password = mpasswordfield.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mprogress.setMessage("Signing Up.....");
            mprogress.show();

            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String user_id = mauth.getCurrentUser().getUid();

                       DatabaseReference current_user_db =  mdatabase.child(user_id);

                        current_user_db.child("name").setValue(name);
                        current_user_db.child("image").setValue("default");

                        mprogress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);

                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }


                }
            });


        }

    }
}
