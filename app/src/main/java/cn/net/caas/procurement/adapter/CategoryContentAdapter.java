package cn.net.caas.procurement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.net.caas.procurement.R;
import cn.net.caas.procurement.entity.MyCategory;

/**
 * Created by wjj on 2017/3/17.
 */
public class CategoryContentAdapter extends BaseAdapter {
    private Context context;
    private List<MyCategory> list;

    public CategoryContentAdapter(Context context,List<MyCategory> list){
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
        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.gridviewitem_category,null);
            viewHolder.image_griditem= (ImageView) view.findViewById(R.id.image_griditem);
            viewHolder.tv_griditem= (TextView) view.findViewById(R.id.tv_griditem);
            view.setTag(viewHolder);
        } else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.image_griditem.setImageResource(list.get(i).getRedId());
        viewHolder.tv_griditem.setText(list.get(i).getName());
        return view;
    }

    class ViewHolder{
        private ImageView image_griditem;
        private TextView tv_griditem;
    }
}
