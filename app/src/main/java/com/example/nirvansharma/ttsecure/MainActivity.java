package com.example.nirvansharma.ttsecure;

        import android.content.Intent;
        import android.media.Image;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;

        import com.bumptech.glide.Glide;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView image;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("list");
    private String url = "this is null";
    private String urlText=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
    ValueEventListener newImageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Data newData = dataSnapshot.getValue(Data.class);
                Log.d(TAG, "URL class is: " + newData.getURL()  );
                //url = (String) dataSnapshot.getValue().toString();
                //StringTokenizer st = new StringTokenizer(url,",");
                //Log.d(TAG,st.nextToken());  //flushing out unwanted tokens
                //Log.d(TAG,st.nextToken());  //flushing out unwanted tokens
            }


            Log.d(TAG, "URL: " + url);
            //set url variable to call in the glide here.
            image = (ImageView) findViewById(R.id.uploadImage);
            if(url !=null){
                Glide.with(MainActivity.this)
                        //.load("https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/main-qimg-190dd04279f58a1b8906597e71e25d0a.png?alt=media&token=5542047c-bc15-4e36-a59f-f02110dcc6d1")
                        .load(urlText)
                        .into(image);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
    ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(newImageListener);*/
       //Data newD = new Data("0.9","https://firebasestorage.googleapis.com/v0/b/ttsecure-e1f8e.appspot.com/o/frame237.jpg?alt=media");

        //ref.push().setValue(newD);


        ref.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChild) {
                Data newData = dataSnapshot.child("Data").getValue(Data.class);
                Log.d(TAG, "URL: " + newData.getURL());
                //set url variable to call in the glide here.
                url = newData.getURL();
                image = (ImageView) findViewById(R.id.uploadImage);
                if(url !=null){
                    Glide.with(MainActivity.this)
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

    Button alarmBtn = (Button) findViewById(R.id.btnAlarm);
    alarmBtn.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            sendEmail(v,url);
        }
    });

    }



    public void sendEmail(View view, String message){

        if(view.getId() == R.id.btnAlarm){
            Log.d(TAG,"Created Intent");
            Log.i("Sending Email","");

            //eventually add option to add recipient strings
            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.setData(Uri.parse("mailto:"));
            String[] to = {"nirvan.sharma17@gmail.com"};
            mailIntent.putExtra(Intent.EXTRA_EMAIL,to);
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,"Security Alert");
            mailIntent.putExtra(Intent.EXTRA_TEXT,"Security Alert, Image URL : "+ message);
            mailIntent.setType("message/rfc822");
            Intent chooser = mailIntent.createChooser(mailIntent,"Send Email");
            startActivity(chooser);
        }


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
