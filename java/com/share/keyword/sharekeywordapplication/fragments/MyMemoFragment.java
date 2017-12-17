package com.share.keyword.sharekeywordapplication.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.share.keyword.sharekeywordapplication.R;
import com.share.keyword.sharekeywordapplication.adapters.MemoAdapter;
import com.share.keyword.sharekeywordapplication.datas.Memo;

import java.util.ArrayList;
import java.util.List;

public class MyMemoFragment extends Fragment {

    private static final String TAG = MyMemoFragment.class.getSimpleName();
    // 오늘의 키워드, 설명
    private String mTodayKeyword;
    private String mTodayDescription;

    // 유저 정보
    private String mUserEmail;
    private String mUid;

    // 파이어베이스
    private FirebaseDatabase mFirebaseDatabase;

    // 키워드 리스트
    private ArrayList<String> mKeywordList = new ArrayList<>();

    // 메모 리스트
    ArrayList<Memo> mMemoList = new ArrayList<>();

    // 리스트뷰
    ListView mListView;

    // MemoAdapter
    MemoAdapter mAdapter;

    public MyMemoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_memo, container, false);


        mListView = view.findViewById(R.id.listView);

        // 유저 데이터 가져오기
        getUserData();

        // 파이어베이스 데이터베이스
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        getTodayKeyword();

        return view;
    }

    private void getTodayKeyword() {
        ValueEventListener keywordListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot keywordSnapshot : dataSnapshot.getChildren()) {
                    // keyword, description
                    mTodayKeyword = keywordSnapshot.getKey();
                    mTodayDescription = keywordSnapshot.child("description").getValue().toString();
                    // 키워드 리스트 저장
                    mKeywordList.add(mTodayKeyword);
                }

                for (int i = 0; i < mKeywordList.size(); i++) {
                    getMemoList(mKeywordList.get(i));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        DatabaseReference keywordReference = mFirebaseDatabase.getReference("keyword");
        keywordReference.addListenerForSingleValueEvent(keywordListener);
    }

    private void getMemoList(String keyword) {
        ValueEventListener memoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                mMemoList.clear();
                for (DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                    Memo memo = memoSnapshot.getValue(Memo.class);
                    // 내가 쓴 글일경우 저장
                    if (memo.getId().equals(mUid)) {
                        mMemoList.add(memo);
                    }
                }
                Log.d(TAG, "onDataChange: " + mMemoList.size());
                mAdapter = new MemoAdapter(getActivity(), mMemoList);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        DatabaseReference MemoReference = mFirebaseDatabase.getReference("keyword").child(keyword).child("memo");
        MemoReference.addListenerForSingleValueEvent(memoListener);
    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            mUserEmail = user.getEmail();
            Log.d(TAG, "onCreate: " + mUserEmail);
            // uid(primary key)
            mUid = user.getUid();
        }
    }
}
