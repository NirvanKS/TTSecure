package com.example.nirvansharma.ttsecure;

        import android.media.Image;
        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.widget.ImageView;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.storage.images.FirebaseImageLoader;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView image;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private String url = "this is null";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data newD = new Data("0.9","https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/frame237.jpg?alt=media");

        ref.push().setValue(newD);

        ref.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChild) {
                Data newData = dataSnapshot.getValue(Data.class);
                Log.d(TAG, "URL: " + newData.getUrl());
                //set url variable to call in the glide here.
                url = newData.getUrl();
                if(url !=null){
                    Glide.with(MainActivity.this)
                            //.load("https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/main-qimg-190dd04279f58a1b8906597e71e25d0a.png?alt=media&token=5542047c-bc15-4e36-a59f-f02110dcc6d1")
                            .load(url)
                            .into(image);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        image = (ImageView) findViewById(R.id.uploadImage);
        Log.d(TAG, "URL is: " + url);
       /* Glide.with(this)
                //.load("https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/main-qimg-190dd04279f58a1b8906597e71e25d0a.png?alt=media&token=5542047c-bc15-4e36-a59f-f02110dcc6d1")
                .load(url)
                .into(image);*/
    }







}
// Points to the root reference
//storageRef = storage.getReference();

// Points to "images"
  //      imagesRef = storageRef.child("images");

// Points to "images/space.jpg"
// Note that you can use variables to create child values
    //    String fileName = "space.jpg";
      //  spaceRef = imagesRef.child(fileName);

// File path is "images/space.jpg"
//        String path = spaceRef.getPath();

// File name is "space.jpg"
  //      String name = spaceRef.getName();

// Points to "images"
    //    imagesRef = spaceRef.getParent();
