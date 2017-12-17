package com.share.keyword.sharekeywordapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnSignin;
    private Button mBtnSignup;

    private FirebaseAuth mAuth;

    private String mEmail;
    private String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이어베이스 로그인 인스턴스 생성
        mAuth = FirebaseAuth.getInstance();

        // 뷰 바인드
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mBtnSignin = findViewById(R.id.btn_signin);
        mBtnSignup = findViewById(R.id.btn_signup);

        mBtnSignin.setOnClickListener(this);
        mBtnSignup.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:

                mEmail = mEtEmail.getText().toString();
                mPassword = mEtPassword.getText().toString();

                if (!mEmail.equals("") && !mPassword.equals("")) {
                    // 회원가입
                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(this, "빈칸 없이 작성하여주세요 :)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_signin:
                mEmail = mEtEmail.getText().toString();
                mPassword = mEtPassword.getText().toString();


                // user list
//                android@test.com androidtest
//                test@test.com testtest

                if (!mEmail.equals("") && !mPassword.equals("")) {
                    // 로그인
                    mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // 로그인 성공시 메인 화면으로 이동
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        Toast.makeText(LoginActivity.this, "Login Success :)",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(this, "빈칸 없이 작성하여주세요 :)", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
