package com.example.kiran.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class postactivity extends AppCompatActivity {

private ImageButton  mselectimage;

    private EditText mposttitle;

    private EditText mpostdescription;

    private Button msubmitbutton;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mstorage;

    private DatabaseReference mdatabaase;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postactivity);

       mstorage = FirebaseStorage.getInstance().getReference();

        mdatabaase = FirebaseDatabase.getInstance().getReference().child("blog");

        mselectimage = (ImageButton)findViewById(R.id.mselectimage);

        mposttitle = (EditText)findViewById(R.id.posttitle);

        mpostdescription = (EditText)findViewById(R.id.postdescription);

        mProgress = new ProgressDialog(this);



        msubmitbutton = (Button)findViewById(R.id.submitbutton);

        mselectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        msubmitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();
            }
        });


    }

    private void startPosting() {

        mProgress.setMessage("Posting to blog...");

        mProgress.show();

        final String title_val = mposttitle.getText().toString().trim();
        final String des_val = mpostdescription.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(des_val) && mImageUri!=null){

            StorageReference filepath = mstorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrL = taskSnapshot.getDownloadUrl();

                    DatabaseReference newpost = mdatabaase.push();

                    newpost.child("title").setValue(title_val);

                    newpost.child("description").setValue(des_val);

                    newpost.child("image").setValue(downloadUrL.toString());

                    mProgress.dismiss();

                    startActivity(new Intent(postactivity.this,MainActivity.class));
                }

            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

             mImageUri = data.getData();

            mselectimage.setImageURI(mImageUri);

        }
    }
}
