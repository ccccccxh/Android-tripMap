package cxh.com.tripmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import cxh.com.tripmap.adapter.ViewPagerAdapter;
import cxh.com.tripmap.fragment.BaseFragment;
import cxh.com.tripmap.fragment.MapFragment;
import cxh.com.tripmap.fragment.TimelineFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //检查是否读写SD卡的权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果没有，则提示用户
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);

        viewPager =findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_news:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_lib:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                break;
//                            case R.id.item_more:
//                                viewPager.setCurrentItem(3);
//                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
    }

    //    创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = new MenuInflater(this);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isCheckable()) {
            item.setChecked(true);
        }
        //菜单项+号的事件处理
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MapFragment.newInstance("map"));
        adapter.addFragment(TimelineFragment.newInstance("list"));
        adapter.addFragment(BaseFragment.newInstance("more"));
        viewPager.setAdapter(adapter);
    }
}
