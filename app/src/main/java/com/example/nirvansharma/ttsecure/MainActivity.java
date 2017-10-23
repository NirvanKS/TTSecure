package com.example.nirvansharma.ttsecure;

        import android.content.Context;
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


        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView image;

    Boolean optionSelected = false;      //for tracking if the email is sent either after countdown or before
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("list");
    private String url = "this is null";
    private String m_Text = "";
    Data newData = null;
    private ArrayList<String> addressList = new ArrayList<String>(); //the phone's instance of the email address lists



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"Instance ID : "+ FirebaseInstanceId.getInstance().getId());
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        pushInstanceID(FirebaseInstanceId.getInstance());
        //function pushID()

        image = (ImageView) findViewById(R.id.uploadImage);
        Glide.with(MainActivity.this)  //placeholder
                .load(R.drawable.cast_ic_notification_connecting)
                .into(image);


        new CountDownTimer(11000,1000){
            TextView mTextField = findViewById(R.id.textViewCounter);
            @Override
            public void onTick(long millisUntilFinished) {

               mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mTextField.setText("done!");
                if(!optionSelected){
                    //can we send emails still?
                }
            }
        }.start();



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
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });




        Button alarmBtn = (Button) findViewById(R.id.btnAlarm);
        alarmBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendEmail(v,url,addressList);
                optionSelected = true;


            }
        });

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
                        Context context = getApplicationContext();
                        CharSequence text = "Email Added";
                        int duration = Toast.LENGTH_SHORT;
                        Toast addedToast = Toast.makeText(context,text,duration);
                        addedToast.setGravity(Gravity.TOP | Gravity.CENTER,0,0);
                        addedToast.show();

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




    public void sendEmail(View view, String message,List emailList){

        if(view.getId() == R.id.btnAlarm){
            String[] toList = convertListToStringArray(emailList);
            //eventually add option to add recipient strings
            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.setData(Uri.parse("mailto:"));
            String[] to = {"nirvan.sharma17@gmail.com"};
            mailIntent.putExtra(Intent.EXTRA_EMAIL,toList);
            mailIntent.putExtra(Intent.EXTRA_SUBJECT,"Security Alert");
            mailIntent.putExtra(Intent.EXTRA_TEXT,"Security Alert, Image URL : "+ message);
            mailIntent.setType("message/rfc822");
            Intent chooser = mailIntent.createChooser(mailIntent,"Send Email");
            startActivity(chooser);
        }


    }

    public String[] convertListToStringArray(List emailList){

        String[] stringArray = (String[]) emailList.toArray(new String[0]);
        return stringArray;
    }

    public void pushInstanceID(FirebaseInstanceId ID){
        String idString = ID.getId().toString();
        DatabaseReference idRef = database.getReference("RegIDs");
        ref.push().setValue(idString);
    }


    public void addEmail(String inputEmailAddress, ArrayList<String> list, DatabaseReference ref){
        Boolean duplicateFlag = false;
        for(String s: list){
            if(inputEmailAddress!=null && s.equalsIgnoreCase(inputEmailAddress)) duplicateFlag = true;
        }

        if(!duplicateFlag){
            EmailRecipient newEmail = new EmailRecipient(inputEmailAddress);
            ref.push().setValue(newEmail);
        }
    }

}
