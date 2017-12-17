package com.share.keyword.sharekeywordapplication.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.share.keyword.sharekeywordapplication.R;

public class KeywordFragment extends Fragment {
    private static final String TAG = KeywordFragment.class.getSimpleName();
    // 오늘의 키워드, 설명
    private String mTodayKeyword;
    private String mTodayDescription;

    private TextView mTvTitle;
    private TextView mTvDescription;

    private Fragment mFragment;

    // 파이어베이스
    private FirebaseDatabase mFirebaseDatabase;

    private FloatingActionButton mFloatingActionButton;

    public KeywordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);

        // 파이어베이스 데이터베이스
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFloatingActionButton = view.findViewById(R.id.floatingActionButton);

        getTodayKeyword();

        mTvTitle = view.findViewById(R.id.tv_keyword_title);
        mTvDescription = view.findViewById(R.id.tv_keyword_description);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment = new WriteMemoFragment();
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
                        mTodayKeyword = keywordSnapshot.getKey();
                        mTodayDescription = keywordSnapshot.child("description").getValue().toString();
                        mTvTitle.setText(mTodayKeyword);
                        mTvDescription.setText(mTodayDescription);
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
}
