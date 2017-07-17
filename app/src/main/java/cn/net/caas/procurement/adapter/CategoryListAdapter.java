package cn.net.caas.procurement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.net.caas.procurement.R;

/**
 * Created by wjj on 2017/3/17.
 */
public class CategoryListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int selectIndex=0;

    public void setSelectItem(int selectIndex){
        if (this.selectIndex != selectIndex){
            this.selectIndex=selectIndex;
            notifyDataSetChanged();
            Log.i("123","刷新选中状态");
        }
    }

    public CategoryListAdapter(Context context, List<String> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listitem_category,null);
            viewHolder.item_category= (TextView) view.findViewById(R.id.item_category);
            view.setTag(viewHolder);
        } else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.item_category.setText(list.get(i));
        if (selectIndex==i){
            view.setBackgroundColor(0xFFF0F0F0);
            ((TextView)view.findViewById(R.id.item_category)).setTextColor(Color.RED);
        } else{
            view.setBackgroundColor(0xFFFFFFFF);
            ((TextView)view.findViewById(R.id.item_category)).setTextColor(Color.BLACK);
        }
        return view;
    }

    static class ViewHolder{
        private static TextView item_category;
    }

}
