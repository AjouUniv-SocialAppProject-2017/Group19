package com.share.keyword.sharekeywordapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.share.keyword.sharekeywordapplication.datas.Memo;
import com.share.keyword.sharekeywordapplication.fragments.KeywordFragment;
import com.share.keyword.sharekeywordapplication.fragments.MyMemoFragment;
import com.share.keyword.sharekeywordapplication.fragments.NotReadyFragment;
import com.share.keyword.sharekeywordapplication.fragments.TodayMusicFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // 유저 정보
    private String mUserEmail;
    private String mUid;

    // 네비게이션 드로워
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    // 파이어베이스
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    // 오늘의 키워드, 설명
    private String mTodayKeyword;
    private String mTodayDescription;

    // 메모 리스트
    List<Memo> mMemoList = new ArrayList<>();

    private BackPressCloseHandler backPressCloseHandler;

    // 프레그먼트
    private Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.drawer);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 네비게이션의 메뉴 클릭
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // 유저 정보 가져오기
        getUserData();

        // 초기화면은 키워드 화면과 동일
        mFragment = new KeywordFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, mFragment);
        fragmentTransaction.commit();

        // 파이어베이스 데이터베이스
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("keyword");

        // 관리자가 키워드와 설명 저장
//        mDatabaseReference.child("헌신").child("description").setValue("this is description of 헌신");

        // 유저가 키워드에 대한 메모 작성
//        mDatabaseReference.child("사랑").child("memo").push().setValue(new Memo(mUid, "사랑 Two", "윤도현"));


        // Read from the database
        // 데이터베이스에 변경사항이 있으면 실행


        // 키워드 및 설명 가져오기
//        getTodayKeyword();

        // 키워드에 대한 메모 리스트 가져오기
        getMemoList("헌신");

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void getMemoList(String keyword) {
        ValueEventListener memoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot memoSnapshot : dataSnapshot.getChildren()) {
                    Memo memo = memoSnapshot.getValue(Memo.class);
                    mMemoList.add(memo);
                }

                Log.d(TAG, "onDataChange: " + mMemoList.size());
                Log.d(TAG, "onDataChange: " + mMemoList.get(0).getId());
                Log.d(TAG, "onDataChange: " + mMemoList.get(0).getTitle());
                Log.d(TAG, "onDataChange: " + mMemoList.get(0).getContent());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        DatabaseReference MemoReference = mFirebaseDatabase.getReference("keyword").child(keyword).child("memo");
        MemoReference.addValueEventListener(memoListener);
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
                    Log.d(TAG, "onDataChange: " + mTodayKeyword + " / " + mTodayDescription);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        DatabaseReference keywordReference = mFirebaseDatabase.getReference("keyword");
        keywordReference.addValueEventListener(keywordListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    // 네비게이션 드로워의 메뉴 클릭 이벤트
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//
        Fragment fragment = null;
        Log.d(TAG, "onNavigationItemSelected: " + id);
        if (id == R.id.my_memo) {
            fragment = new MyMemoFragment();
            Toast.makeText(this, "clicked!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.keyword) {
            fragment = new KeywordFragment();
        } else if (id == R.id.today_picture) {
            fragment = new NotReadyFragment();
        } else if (id == R.id.today_music) {
            fragment = new TodayMusicFragment();
        } else if (id == R.id.setting) {
            fragment = new NotReadyFragment();
        } else if (id == R.id.logout) {
            // 로그아웃
            FirebaseAuth.getInstance().signOut();
            // 로그인 화면으로 이동
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "로그아웃 되었습니다 :)", Toast.LENGTH_SHORT).show();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
