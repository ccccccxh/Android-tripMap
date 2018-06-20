package cxh.com.tripmap.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import cxh.com.tripmap.R;
import cxh.com.tripmap.dao.SiteDao;
import cxh.com.tripmap.entity.SiteBean;

/**
 * 显示地图
 */
public class MapFragment extends Fragment {

    private GeoCoder mSearch = null;
    private SiteDao siteDao;
    private List<SiteBean> siteBeanList;

    public static MapFragment newInstance(String info) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("info",info);
        fragment.setArguments(args);
        return fragment;
    }

    private MapView mMapView = null;
    //地图控制器对象
    private BaiduMap mBaiduMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        mMapView = view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为普通地图
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(new LatLng(40.271276F,104.592054F));
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(5);
        mBaiduMap.animateMapStatus(update);
        initData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        Log.e("MapFragment","onStart");
    }

    private void initData(){
        siteDao = new SiteDao(this.getContext());
        siteBeanList = siteDao.getList();
        for(int i = 0;i<siteBeanList.size();i++){
            SiteBean tmp = siteBeanList.get(i);
            //定义Maker坐标点
            LatLng point = new LatLng((Float.valueOf(tmp.getLatitude()
                    .toString())), (Float.valueOf(tmp.getLongitude().toString())));
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_small);
            //构建MarkerOption，用于在地图上添加Marker
            MarkerOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            Log.i("", "initData: addmarker"+point.latitude+","+point.longitude);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        MapView.setMapCustomEnable(false);
        mMapView = null;
    }

}
