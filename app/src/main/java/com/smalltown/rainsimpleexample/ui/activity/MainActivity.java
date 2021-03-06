package com.smalltown.rainsimpleexample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.OnClick;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.util.SnackUtil;
import com.smalltown.rainsimpleexample.util.ToastUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFab;//浮动按钮
    @SuppressWarnings("FieldCanBeLocal") private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal") private DrawerLayout mDrawer;
    @SuppressWarnings("FieldCanBeLocal") private NavigationView mNavigationView;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 浮动按钮点击
     */
    @OnClick(R.id.fab)
    void fabClick(){
        SnackUtil.showMessage(mFab, "点击浮动按钮");
    }

    /**
     * 点击跳转RecycleView
     */
    @OnClick(R.id.main_btn_to_recycle)
    void toRecycleActivity(){
        Intent intent = new Intent(MainActivity.this,RecycleActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 点击跳转JS交互
     */
    @OnClick(R.id.main_btn_to_JsBridge)
    void jPush(){
        Intent intent = new Intent(MainActivity.this,JsBridgeActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 点击跳转流布局界面
     */
    @OnClick(R.id.main_btn_to_randomLayout)
    void toRandomLayout(){
        Intent intent = new Intent(MainActivity.this,RandomActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }
    /**
     * 跳转动画界面
     */
    @OnClick(R.id.main_btn_to_animator)
    void toAnimatorActivity(){
        Intent intent = new Intent(MainActivity.this,AnimatorActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 跳转FragmentDialog
     */
    @OnClick(R.id.main_btn_to_dialogFragment)
    void toDialogFragmentActivity(){
        Intent intent = new Intent(MainActivity.this,DialogFragmentActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 跳转Mvp框架测试页面
     */
    @OnClick(R.id.main_btn_to_mvpsimple)
    void toMvpSimple(){
        Intent intent = new Intent(MainActivity.this,MvpSimpleActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }
    /**
     * 跳转高德地图
     */
    @OnClick(R.id.main_btn_to_GdMap)
    void toGdMap(){
        Intent intent = new Intent(MainActivity.this,GdMapActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 跳转Retrofit网络加载
     */
    @OnClick(R.id.main_btn_to_GdMap)
    void toRetrofit(){
        Intent intent = new Intent(MainActivity.this,RetrofitActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 跳转Retrofit网络加载
     */
    @OnClick(R.id.main_btn_to_job_queue)
    void toJobQueue(){
        Intent intent = new Intent(MainActivity.this,JobQueueActivity.class);
        startActivity(intent,TRANS_TYPE_TRANSLATE);
    }

    /**
     * 跳转Rxandroid
     */
    @OnClick(R.id.main_btn_to_rx_android)
    void toRxAndroid(){
        Intent intent = new Intent(MainActivity.this,RxAndroidActivity.class);
        startActivity(intent,TRANS_TYPE_ZOOM);
    }

    /**
     * 吹一吹
     */
    @OnClick(R.id.main_btn_to_chui)
    void toChui(){
        Intent intent = new Intent(MainActivity.this,BlowActivity.class);
        startActivity(intent,TRANS_TYPE_ZOOM);
    }




/*****************************整体界面设置************************************/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ToastUtil.showToast("点击设置");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
