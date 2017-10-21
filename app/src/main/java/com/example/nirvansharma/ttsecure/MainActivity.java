package com.example.nirvansharma.ttsecure;

        import android.media.Image;
        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.storage.images.FirebaseImageLoader;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private ImageView image;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String imageName = "main-qimg-190dd04279f58a1b8906597e71e25d0a.png";
        StorageReference spaceRef = storageRef.child(imageName);

        image = (ImageView) findViewById(R.id.uploadImage);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/main-qimg-190dd04279f58a1b8906597e71e25d0a.png?alt=media&token=5542047c-bc15-4e36-a59f-f02110dcc6d1")
                .into(image);
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
