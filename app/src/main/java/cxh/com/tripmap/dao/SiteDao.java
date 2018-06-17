package cxh.com.tripmap.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cxh.com.tripmap.entity.SiteBean;
import cxh.com.tripmap.source.DataSource;

/**
 * Created by cxh on 2018/6/11.
 */

public class SiteDao {
    private DataSource dataSource;

    public SiteDao(Context context) {
        this.dataSource = new DataSource(context, "TripMap.db3", null, 1);
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return dataSource.getWritableDatabase();
    }

    public List<SiteBean> getList(){
        List<SiteBean> list = new ArrayList<>();
        Cursor cursor = getSQLiteDatabase().query("trip_map",
                new String[]{"id", "site", "time", "longitude", "latitude"},
                null,null,
                null, null,
                "time asc");

        while (cursor.moveToNext()) {
            SiteBean siteBean = new SiteBean();
            siteBean.setId(cursor.getInt(0));
            siteBean.setSite(cursor.getString(1));
            siteBean.setTime(new Date(cursor.getString(2)));
            siteBean.setLongitude(cursor.getDouble(3));
            siteBean.setLatitude(cursor.getDouble(4));
            list.add(siteBean);
        }
        return list;
    }

    public void add(String site,Date time,double longitude,double latitude){
        ContentValues values = new ContentValues();
        values.put("site",site);
        values.put("time", time.toString());
        values.put("longitude", longitude);
        values.put("latitude", latitude);
        getSQLiteDatabase().insert("trip_map", null, values);
    }

    public void delete(String id){
        getSQLiteDatabase().delete("trip_map","id=?",new String[]{id});
    }

    public void close() {
        dataSource.close();
    }

}
