package cxh.com.tripmap.dto;

import java.util.Date;

/**
 * Created by cxh on 2018/6/7.
 */

public class LineList {
//    private int id;
//    private String time;
    private Date time;
    private String site;

    public LineList(){

    }

    public LineList(Date time, String site) {
        this.time = time;
        this.site = site;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
