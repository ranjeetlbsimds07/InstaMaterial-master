package com.universe.blog.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.universe.blog.R;
import com.universe.blog.ui.utils.CommonUtils;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText EtEmailId;
    private EditText EtPassWord;
    private Button btnSignUp;

    private String strEmailId;
    private String strPassword;
    private ProgressDialog myDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EtEmailId = (EditText)findViewById(R.id.EtEmailId);
        EtPassWord = (EditText)findViewById(R.id.EtPassWord);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


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

                myDialog= CommonUtils.showProgressDialog(this,"Please Wait...");

                auth.createUserWithEmailAndPassword(strEmailId, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        myDialog.dismiss();
                        if(task.isSuccessful()){
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
}
