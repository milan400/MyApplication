package com.example.kiran.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mbloglist;

    private DatabaseReference mdatabase;

    private DatabaseReference mdatabaseuser;

    private FirebaseAuth mauth;

    private FirebaseAuth.AuthStateListener mauthlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(getApplicationContext());

        mauth = FirebaseAuth.getInstance();

        mauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null){

                    Intent loginIntent = new Intent(MainActivity.this , LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);


                }

            }
        };

        mdatabase = FirebaseDatabase.getInstance().getReference().child("blog");

        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");

        mdatabaseuser.keepSynced(true);

        mdatabase.keepSynced(true);

        mbloglist = (RecyclerView)findViewById(R.id.blog_list);



        mbloglist.setHasFixedSize(true);

        mbloglist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {

        super.onStart();

        CheckUserExist();





        mauth.addAuthStateListener(mauthlistener);

        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());



            }
        };

        mbloglist.setAdapter(firebaseRecyclerAdapter);
    }


    private void CheckUserExist() {

        final String user_id = mauth.getCurrentUser().getUid();

        mdatabaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(user_id)){

                    Intent setupIntent = new Intent(MainActivity.this , SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View nview;


        public BlogViewHolder(View itemView) {
            super(itemView);

            nview = itemView;
        }

        public void setTitle(String title){

            TextView post_title = (TextView) nview.findViewById(R.id.post_title);
            post_title.setText(title);


        }

        public void setDesc(String description){

            TextView post_desc = (TextView) nview.findViewById(R.id.post_desc) ;
            post_desc.setText(description);
        }


        public void setImage(Context ctx , String image) {

            ImageView post_image = (ImageView) nview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add){

            startActivity(new Intent(MainActivity.this,postactivity.class));

        }

        if(item.getItemId() == R.id.action_logout){

            logout();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mauth.signOut();
    }
}
















































































































