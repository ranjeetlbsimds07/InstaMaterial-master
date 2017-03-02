package com.universe.blog.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.universe.blog.R;
import com.universe.blog.UserSharePreference;
import com.universe.blog.ui.utils.CommonUtils;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by ranjeet on 24/2/17.
 */

public class LogInActivity extends BaseActivity implements View.OnClickListener {

    private Button btnLogin;
    private FirebaseAuth auth;
    private EditText EtUserName;
    private EditText EtPassWord;
    private String strUserName;
    private String strPassWord;
    private ProgressDialog myDialog;
    private TextView txtSignUp;
    private DatabaseReference mDatabase;
    private UserSharePreference userSharePreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        EtUserName= (EditText)findViewById(R.id.EtUserName);
        EtPassWord= (EditText)findViewById(R.id.EtPassWord);
        txtSignUp= (TextView) findViewById(R.id.txtSignUp);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userSharePreference = new UserSharePreference(this);


        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnLogin :{

                //get User Name and Password
                strUserName = EtUserName.getText().toString().trim();
                strPassWord = EtPassWord.getText().toString().trim();



                if(TextUtils.isEmpty(strUserName) && TextUtils.isEmpty(strPassWord)){
                    Toast.makeText(this,"Please Enter User Name and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strUserName)){
                    Toast.makeText(this,"Please Enter User Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strPassWord)){
                    Toast.makeText(this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                myDialog= CommonUtils.showProgressDialog(this,"Please Wait...");

                auth.signInWithEmailAndPassword(strUserName, strPassWord).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        myDialog.dismiss();
                        if(task.isSuccessful()){
                            String getUid = task.getResult().getUser().getUid();
                            mDatabase = mDatabase.child("users").child(getUid);

                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        for (DataSnapshot userDetails : child.getChildren()){
                                            if(userDetails.getKey().equalsIgnoreCase("firstName")){
                                                userSharePreference.setFirstName(userDetails.getValue().toString());
                                            }

                                            if(userDetails.getKey().equalsIgnoreCase("lastName")){
                                                userSharePreference.setFirstName(userDetails.getValue().toString());
                                            }


                                            if(userDetails.getKey().equalsIgnoreCase("uid")){
                                                userSharePreference.setFirstName(userDetails.getValue().toString());
                                            }
                                            //Log.d(userDetails.getKey()+"==",userDetails.getValue()+"");
                                             //Toast.makeText(LogInActivity.this,"User Name"+userDetails.getValue(), Toast.LENGTH_SHORT).show();

                                         }
                                    }
                                    Toast.makeText(LogInActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    EtUserName.setText("");
                                    EtPassWord.setText("");

                                    Toast.makeText(LogInActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            });



                        }else{
                            EtUserName.setText("");
                            EtPassWord.setText("");

                            Toast.makeText(LogInActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
            break;
            case  R.id.txtSignUp:{
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
            break;

            default:
                break;
        }
    }
}
