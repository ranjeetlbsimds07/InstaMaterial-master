package com.universe.blog.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.universe.blog.R;
import com.universe.blog.ui.Model.UsersModel;
import com.universe.blog.ui.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText EtEmailId;
    private EditText EtPassWord;
    private EditText EtFirstName;
    private EditText EtLastName;
    private Button btnSignUp;

    private String strEmailId;
    private String strPassword;
    private ProgressDialog myDialog;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EtEmailId = (EditText)findViewById(R.id.EtEmailId);
        EtPassWord = (EditText)findViewById(R.id.EtPassWord);
        EtFirstName = (EditText)findViewById(R.id.EtFirstName);
        EtLastName = (EditText)findViewById(R.id.EtLastName);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnSignUp :{

                //get User Name and Password
                strEmailId = EtEmailId.getText().toString().trim();
                strPassword = EtPassWord.getText().toString().trim();



                if(TextUtils.isEmpty(strEmailId) && TextUtils.isEmpty(strPassword)){
                    Toast.makeText(this,"Please Enter Email Id and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strEmailId)){
                    Toast.makeText(this,"Please Enter Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strPassword)){
                    Toast.makeText(this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(EtFirstName.getText().toString().trim())){
                    Toast.makeText(this,"Please Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(EtLastName.getText().toString().trim())){
                    Toast.makeText(this,"Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                myDialog= CommonUtils.showProgressDialog(this,"Please Wait...");

                auth.createUserWithEmailAndPassword(strEmailId, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        myDialog.dismiss();
                        if(task.isSuccessful()){
                            String getUid = task.getResult().getUser().getUid();
                            UsersModel usersModel = new UsersModel(getUid, EtFirstName.getText().toString().trim(),EtLastName.getText().toString().trim(),true);

                            addNewTodoItem(usersModel, getUid);
                            //Log.d("get UID",task.getResult().getUser().getUid()+"");
                            Toast.makeText(SignUpActivity.this,"User Registration Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            EtEmailId.setText("");
                            EtPassWord.setText("");

                            Toast.makeText(SignUpActivity.this,"User Registration Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
            break;
            default:
                break;
        }
    }

    private void addNewTodoItem(UsersModel users, String getUid) {
        String key = mDatabase.child("users").child(getUid).push().getKey();
        Map<String, Object> todoValues = users.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + getUid + "/" + key, todoValues);
        mDatabase.updateChildren(childUpdates);
    }
}
