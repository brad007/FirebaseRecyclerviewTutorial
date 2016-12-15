package com.software.fire.firebaserecyclerviewtutorial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.software.fire.firebaserecyclerviewtutorial.model.Post;
import com.software.fire.firebaserecyclerviewtutorial.utils.Constants;
import com.software.fire.firebaserecyclerviewtutorial.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseScreen();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostToFirebase();
            }
        });
    }

    private void sendPostToFirebase() {
        Post post = new Post();
        String UID = Utils.getUID();

        post.setUID(UID);
        post.setNumLikes(0);
        post.setImageUrl("gs://fir-recyclerviewtutorial.appspot.com/286816_fa15c44dfd734c4a157571d79b0c6b49.jpg");

        mPostRef.child(UID).setValue(post);
    }

    private void initialiseScreen() {
        mPostRV = (RecyclerView) findViewById(R.id.post_rv);
        mPostRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.POSTS);
        setupAdapter();
        mPostRV.setAdapter(mPostAdapter);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.item_layout_post,
                PostViewHolder.class,
                mPostRef
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(viewHolder.postIV);

                viewHolder.setNumLikes(model.getNumLikes());
                viewHolder.postLikeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateNumLikes(model.getUID());
                    }
                });
            }
        };
    }

    private void updateNumLikes(String uid) {
        mPostRef.child(uid).child(Constants.NUM_LIKES)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long num = (long) mutableData.getValue();
                        num++;
                        mutableData.setValue(num);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView postIV;
        public ImageView postLikeIV;
        public TextView numLikesTV;


        public PostViewHolder(View itemView) {
            super(itemView);

            postIV = (ImageView) itemView.findViewById(R.id.post_iv);
            postLikeIV = (ImageView) itemView.findViewById(R.id.like_iv);
            numLikesTV = (TextView) itemView.findViewById(R.id.num_likes_tv);
        }


        public void setNumLikes(long num) {
            numLikesTV.setText(String.valueOf(num));
        }
    }
}
