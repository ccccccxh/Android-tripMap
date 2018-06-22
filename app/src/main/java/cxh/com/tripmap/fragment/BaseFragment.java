package cxh.com.tripmap.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cxh.com.tripmap.R;
import cxh.com.tripmap.dao.SiteDao;
import cxh.com.tripmap.entity.SiteBean;

/**
 * Created by cxh on 2018/6/6.
 */

public class BaseFragment extends Fragment {

    private Button button;
    private TextView textView;
    private SiteDao siteDao;
    private List<SiteBean> siteBeanList;
//    private boolean isGetData = false;

    public static BaseFragment newInstance(String info) {
        Log.e("BaseFragment","newInstance");
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("BaseFragment","onCreateView");
        View view = inflater.inflate(R.layout.fragment_base, null);
        button = view.findViewById(R.id.button);
        textView = view.findViewById(R.id.all);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        initData();
        return view;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (enter && ! isGetData) {
//            isGetData = true;
//            initData();
//        } else {
//            isGetData = false;
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }

    @Override
    public void onStart() {
        super.onStart();
//        if (!isGetData) {
            initData();
//            isGetData = true;
//        }
        Log.e("BaseFragment","onStart");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        initData();
//        Log.e("BaseFragment","onResume");
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        isGetData = false;
//    }

    private void initData(){
        siteDao = new SiteDao(this.getContext());
        siteBeanList = siteDao.getList();
        textView.setText("你在"+siteBeanList.size()+"个地方留下了你的足迹");
    }

}
