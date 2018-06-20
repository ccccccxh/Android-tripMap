package cxh.com.tripmap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cxh.com.tripmap.R;
import cxh.com.tripmap.dto.LineList;

/**
 * Created by cxh on 2018/6/7.
 */

public class TimelineAdapter extends  RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private List<LineList> list=null;
    private static final int TYPE_TOP = 0;
    private static final int TYPE_BOTTOM = 1;
    private static final int TYPE_NORMAL= 2;
    private OnItemClickListener mOnItemClickListener;

    public List<LineList> getList() {
        return list;
    }

    public void setList(List<LineList> list) {
        this.list = list;
    }

    private Context context;
    public TimelineAdapter(Context context,List<LineList> list) {
        this.list=list;
        this.context=context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        ViewHolder itemHolder = holder;
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行的上半部分的竖线不显示
            holder.topLine.setVisibility(View.INVISIBLE);
        }
        if(getItemViewType(position) == TYPE_BOTTOM){
            holder.bottomLine.setVisibility(View.INVISIBLE);
        }

        holder.bindHolder(list.get(position));
//        holder.itemView.setTag(list.get(position).getId());
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        if(position == list.size()-1){
            return TYPE_BOTTOM;
        }
        return TYPE_NORMAL;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, site, topLine, bottomLine;
        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.line_time);
            site = itemView.findViewById(R.id.line_site);
            topLine = itemView.findViewById(R.id.line_topLine);
            bottomLine = itemView.findViewById(R.id.line_bottomLine);
        }

        public void bindHolder(LineList lineList) {
            time.setText(FORMAT.format(lineList.getTime()));
            site.setText(lineList.getSite());
        }

    }

    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }

}
