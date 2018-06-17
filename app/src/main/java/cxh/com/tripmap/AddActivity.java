package cxh.com.tripmap;

import cxh.com.tripmap.dao.SiteDao;
import cxh.com.tripmap.dto.LineList;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 三级联动添加足迹
 * */
public class AddActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener{

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private GeoCoder mSearch = null;
    private Button mBtnConfirm;
    private android.support.v7.app.ActionBar actionBar;
    private SiteDao siteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setUpViews();
        setUpListener();
        setUpData();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    Toast.makeText(AddActivity.this, "没有找到结果", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(AddActivity.this, ""+result.getLocation().latitude+","+result.getLocation().longitude, Toast.LENGTH_SHORT).show();
                siteDao = new SiteDao(AddActivity.this);
                siteDao.add(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName,new Date(),result.
                        getLocation().longitude,result.getLocation().latitude);

                AddActivity.this.finish();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            }
        });
    }
    private void setUpViews() {
        mViewProvince = findViewById(R.id.id_province);
        mViewCity = findViewById(R.id.id_city);
        mViewDistrict = findViewById(R.id.id_district);
        mBtnConfirm = findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AddActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(15);
        mViewCity.setVisibleItems(15);
        mViewDistrict.setVisibleItems(15);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = areas[0];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                showSelectedResult();
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
//        Toast.makeText(AddActivity.this, "当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
//                +mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();
        mSearch.geocode(new GeoCodeOption().city(mCurrentProviceName).address(mCurrentDistrictName));
    }

    //设置回退箭头的回退事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isCheckable()) {
            item.setChecked(true);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
