package com.example.nirvansharma.ttsecure;

        import android.content.Context;
        import android.graphics.Color;
        import android.support.v7.widget.Toolbar;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.text.InputType;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.view.animation.AlphaAnimation;
        import android.view.animation.Animation;
        import android.view.animation.LinearInterpolator;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.QuickContactBadge;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.iid.FirebaseInstanceId;
        import com.google.firebase.messaging.FirebaseMessaging;


        import java.util.ArrayList;
        import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView image;
    Boolean optionSelected = false;                                                 //Used to track if the user selects an option
    final FirebaseDatabase database = FirebaseDatabase.getInstance();               //Firebase instance
    DatabaseReference ref = database.getReference("list");                          //Main child node
    private String url = "this is null";
    private String m_Text = "";
    Data newData = null;
    private ArrayList<String> addressList = new ArrayList<String>();                    //the phone's instance of the email address lists



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("news");                   //subscribing to the notification topic when the app comes online on your phone.
        pushInstanceID(FirebaseInstanceId.getInstance());                           //store the instance ID for notifications later on.

        image = (ImageView) findViewById(R.id.uploadImage);                         //If no new images, creates a placeholder to load at first.
        Glide.with(MainActivity.this)  //placeholder
                .load(R.drawable.cast_ic_notification_connecting)
                .into(image);

        //setting animation for warning button
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        final Button btn = (Button) findViewById(R.id.btnAlarm);
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        //animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatCount(20);
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        btn.startAnimation(animation);
        btn.setBackgroundColor(Color.RED);

        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn.setVisibility(View.VISIBLE);
                btn.setBackgroundColor(Color.WHITE);
                btn.setText("Warn");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        


        //This query , using the reference of the list parent node , will return the last child node added in the list.
        // That is, it will return the latest image url in firebase.
        ref.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChild) {
                newData = dataSnapshot.child("Data").getValue(Data.class);
                if(newData != null){
                    url = newData.getURL();
                    Log.d(TAG, "URL: " + newData.getURL());
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

        //this reference and value event listener loads the list of EmailRecipients from firebase.
        //It loads each child of the EmailRecipients parent node into a String arraylist.
        final DatabaseReference emailRef = database.getReference("EmailRecipients");
        emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    EmailRecipient curr =  snapshot.getValue(EmailRecipient.class);
                    addressList.add(curr.getEmailAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });



        //Click listener for the warning button. When clicked, the email will be sent to the
        //recipients in the recipient list.

        Button alarmBtn = (Button) findViewById(R.id.btnAlarm);
        alarmBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendEmail(v,url,addressList);
                optionSelected = true;
                v.clearAnimation();


            }
        });

        //Click listener for the ignore button, if ignored, the image that was ignored
        //will be sent to another firebase node, that saves all the images that
        //the system supposedly analyzed wrong.

        Button cancelBtn = (Button) findViewById(R.id.btnfalseAlarm);

        cancelBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(newData!=null){
                    optionSelected = true;
                    DatabaseReference falseAlarmRef = database.getReference("CancelledImages");
                    falseAlarmRef.push().setValue(newData);
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect image noted for machine correction";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context,text,duration).show();
                }

            }
        });

        //Button that will open an input form which allows the user to add another email address.
        //If the email address is already in the list, it does not re-add it, but simply
        //alerts the user that the email address is already in the list.

        QuickContactBadge addEmailBtn = (QuickContactBadge) findViewById(R.id.quickContactBadge);
        addEmailBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Email");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        addEmail(m_Text,addressList,emailRef);

                    }
                });

                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
    }



    //This function primarily takes in the url of the image and uses it as the String email content.
    //It also takes in the view and the current list of email recipients.
    //The function will open an intent, which allows the user to choose which email service on their
    //device to use to send the email. The email intent will then be filled with the automatic
    //content (url) and the senders. The user can now just confirm to send the email.

    public void sendEmail(View view, String message,List emailList){

        if(view.getId() == R.id.btnAlarm){
            String[] toList = convertListToStringArray(emailList);
            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_EMAIL,toList);
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,"Security Alert");
            mailIntent.putExtra(Intent.EXTRA_TEXT,"Security Alert, Image URL : "+ message);
            mailIntent.setType("message/rfc822");
            Intent chooser = mailIntent.createChooser(mailIntent,"Send Email");
            startActivity(chooser);
        }


    }

    //Function that takes in the current Arraylist of emails, and changes in into a String array.
    //This is needed because the email intent needed the email list to be in the String array form.
    public String[] convertListToStringArray(List emailList){

        String[] stringArray = (String[]) emailList.toArray(new String[0]);
        return stringArray;
    }

    //We also keep track of the Firebase Instance IDs that use the application. However, notifications
    //are mainly sent by using Topics in Firebase Cloud Messaging.
    public void pushInstanceID(FirebaseInstanceId ID){
        final String idString = ID.getId().toString();
        final ArrayList<String> idList = new ArrayList<String>();


        DatabaseReference idRef = database.getReference("RegIDs");
        ValueEventListener valueEventListener = idRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String curr = snapshot.getValue().toString();
                    Log.d(TAG, "Id curr is " + curr);
                    idList.add(curr);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Boolean duplicateIDFlag = false;
        Log.d(TAG,"List has size " + idList.size());
        for(String s: idList){
            if(ID!=null && idString!=null && s.equalsIgnoreCase(idString)) duplicateIDFlag = true;
        }

        if(!duplicateIDFlag){
            idRef.push().setValue(idString);
        }


    }

    //This function is run when the user clicks on the Add Email badge. It takes in the email
    //string input from the user, the current email Arraylist and the firebase database reference.
    //It will check if the input email address supplied is already in the list, if it's not,
    // the email will be added to the EmailRecipients parent node in firebase.
    public void addEmail(String inputEmailAddress, ArrayList<String> list, DatabaseReference ref){
        Boolean duplicateFlag = false;
        for(String s: list){
            if(inputEmailAddress!=null && s.equalsIgnoreCase(inputEmailAddress)){
                duplicateFlag = true;
                Context context = getApplicationContext();
                CharSequence text = "Email already in the list!";
                int duration = Toast.LENGTH_SHORT;
                Toast addedToast = Toast.makeText(context,text,duration);
                addedToast.setGravity(Gravity.TOP | Gravity.CENTER,0,0);
                addedToast.show();
            }

        }

        if(!duplicateFlag){
            EmailRecipient newEmail = new EmailRecipient(inputEmailAddress);
            ref.push().setValue(newEmail);
            Context context = getApplicationContext();
            CharSequence text = "Email Added";
            int duration = Toast.LENGTH_SHORT;
            Toast addedToast = Toast.makeText(context,text,duration);
            addedToast.setGravity(Gravity.TOP | Gravity.CENTER,0,0);
            addedToast.show();
        }
    }

}
