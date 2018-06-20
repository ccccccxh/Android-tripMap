package cxh.com.tripmap.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cxh.com.tripmap.R;
import cxh.com.tripmap.adapter.TimelineAdapter;
import cxh.com.tripmap.dao.SiteDao;
import cxh.com.tripmap.dto.LineList;
import cxh.com.tripmap.entity.SiteBean;

/**
 * 时光轴fragment
 * */

public class TimelineFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView recyclerView;
    private TimelineAdapter mAdapter;
    private SiteDao siteDao;
//    private LineList lineList;
    private List<SiteBean> siteBeanList;

    public static TimelineFragment newInstance(String info) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString("info",info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initData();

        //删除操作
        mAdapter.setOnItemClickListener(new TimelineAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(),"长按可删除该条足迹", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(final int position) {
                //实例化一个对话框
                AlertDialog.Builder msg = new AlertDialog.Builder(getContext());
                msg.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        siteDao = new SiteDao(getContext());
                        SiteBean siteBean = siteBeanList.get(position);
                        siteDao.delete(String.valueOf(siteBean.getId()));
                        Toast.makeText(getContext(),"您已删除该条足迹",Toast.LENGTH_SHORT).show();
                        initData();
                    }
                });
                msg.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                msg.setMessage("您是否要删除该条足迹");
                msg.setTitle("提示");
                msg.show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        Log.e("TimelineFragment","onStart");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData(){
        List<LineList> list=new ArrayList<>();
        siteDao = new SiteDao(this.getContext());
        siteBeanList = siteDao.getList();
        for (int i = 0; i < siteBeanList.size(); i++) {
            SiteBean tmp = siteBeanList.get(i);
//            list.add(new LineList(FORMAT.format(tmp.getTime()),tmp.getSite()));
            list.add(new LineList(tmp.getTime(),tmp.getSite()));
        }
//        list.sort(new Comparator<LineList>() {
//            @Override
//            public int compare(LineList lineList, LineList t1) {
////                try {
//                    return lineList.getTime().getTime() > t1.getTime().getTime() ? -1 : 1;
////                   return FORMAT.parse(lineList.getTime()).getTime() > FORMAT.parse(t1.getTime()).getTime() ? -1 : 1;
////                } catch (ParseException e) {
////                    e.printStackTrace();
////                }
////                return 1;
//            }
//        });
        if(mAdapter == null) {
            mAdapter = new TimelineAdapter(this.getContext(), list);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }

    }

}
