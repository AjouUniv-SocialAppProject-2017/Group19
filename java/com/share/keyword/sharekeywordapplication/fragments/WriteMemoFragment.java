package com.share.keyword.sharekeywordapplication.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.share.keyword.sharekeywordapplication.R;
import com.share.keyword.sharekeywordapplication.datas.Memo;

public class WriteMemoFragment extends Fragment {
    // 파이어베이스
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    private TextView mTvKeyword;
    private EditText mEtTitle;
    private EditText mEtContent;
    private Button mBtnSave;

    // 유저 아이디
    private String mUid;

    // 오늘의 키워드, 설명
    private String mTodayKeyword;

    public WriteMemoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_memo, container, false);

        // 파이어베이스 데이터베이스
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("keyword");

        // 유저 아이디 가져오기
        getUserData();

        mTvKeyword = view.findViewById(R.id.tv_today_keyword);
        mEtTitle = view.findViewById(R.id.et_title);
        mEtContent = view.findViewById(R.id.et_content);
        mBtnSave = view.findViewById(R.id.btn_save);

        getTodayKeyword();

        // 저장버튼 클릭
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(mTodayKeyword).child("memo").push().setValue(new Memo(mUid, mEtTitle.getText().toString(), mEtContent.getText().toString()));
                Toast.makeText(getActivity(), "저장되었습니다 :)", Toast.LENGTH_SHORT).show();

                // 키워드 화면으로 이동
                Fragment mFragment = new KeywordFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, mFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void getTodayKeyword() {
        ValueEventListener keywordListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int count = 0;
                for (DataSnapshot keywordSnapshot : dataSnapshot.getChildren()) {
                    // keyword, description
                    if (count == 0) {
                        // 타이틀에 키워드 저장
                        mTodayKeyword = keywordSnapshot.getKey();
                        mTvKeyword.setText(mTodayKeyword);
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        DatabaseReference keywordReference = mFirebaseDatabase.getReference("keyword");
        keywordReference.addValueEventListener(keywordListener);
    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            // uid(primary key)
            mUid = user.getUid();
        }
    }
}
