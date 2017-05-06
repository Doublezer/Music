package com.example.yday_15_04.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yday_15_04.entity.Music;
import com.example.yday_15_04.service.MusicService;
import com.example.yday_15_04.R;
import com.example.yday_15_04.connect.RecyclerAdapter;
import com.example.yday_15_04.connect.WorkAsync;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        NavigationView.OnNavigationItemSelectedListener,
        AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener{

    private List<Music> list;
    private static final int REQUEST_CODE=100;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private RecyclerView.LayoutManager linearManager;
    private RecyclerAdapter recyclerAdapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private boolean isChange=false;
    private RecyclerView.LayoutManager gridManager;
    private TextView musicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        setToolBar();
        setSwipe();
        setRecyclerView();
        setNavDraw();
        setAppBar();
        setMediaButton();
    }

    private void setMediaButton() {
        ImageButton btn_play= (ImageButton) findViewById(R.id.page_ib_play);
        ImageButton btn_pause= (ImageButton) findViewById(R.id.page_ib_pause);
        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
    }

    private void loadMusic() {
        WorkAsync workAsync=new WorkAsync(swipeRefreshLayout,getApplicationContext());
        workAsync.executeOnExecutor(Executors.newCachedThreadPool());
    }

    private void setAppBar() {
        relativeLayout= (RelativeLayout) findViewById(R.id.page_bottombar);
        appBarLayout = (AppBarLayout) findViewById(R.id.page_appbar);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }else {
            loadMusic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadMusic();
                }
                return;
        }
    }

    private void setNavDraw() {
        navigationView = (NavigationView) findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
    }

    private void setSwipe() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.page_swipe);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setDistanceToTriggerSync(300);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.BLUE,Color.RED);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.page_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setRecyclerView() {
        list=new LinkedList<>();
        musicName= (TextView) findViewById(R.id.page_tv_musicname);
        recyclerView = (RecyclerView) findViewById(R.id.page_recyclerview);
        recyclerView.setHasFixedSize(true);
        linearManager = new LinearLayoutManager(MainActivity.this);
        gridManager=new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(linearManager);
        recyclerAdapter = new RecyclerAdapter(this,list,musicName);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_change:
                if (!isChange){
                    isChange=true;
                    recyclerView.setLayoutManager(gridManager);
                }else {
                    isChange=false;
                    recyclerView.setLayoutManager(linearManager);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadMusic();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START,true);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float srcheight=appBarLayout.getHeight();
        float desheight=relativeLayout.getHeight();
        relativeLayout.setTranslationY(-desheight/srcheight*verticalOffset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.page_ib_play:
                Intent play=new Intent(this,MusicService.class);
                play.putExtra("actionKey","play");
                this.startService(play);
                break;
            case R.id.page_ib_pause:
                Intent pause=new Intent(this,MusicService.class);
                pause.putExtra("actionKey","pause");
                this.startService(pause);
                break;
        }
    }
}
