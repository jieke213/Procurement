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
import cn.net.caas.procurement.entity.Mine;
import cn.net.caas.procurement.entity.MyCategory;

/**
 * Created by wjj on 2017/3/20.
 */
public class MineAdapter extends BaseAdapter {
    private Context context;
    private List<Mine> list;

    public MineAdapter(Context context, List<Mine> list){
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
            view= LayoutInflater.from(context).inflate(R.layout.gridviewitem_mine,null);
            viewHolder.image_griditem_mine= (ImageView) view.findViewById(R.id.image_griditem_mine);
            viewHolder.tv_griditem_mine= (TextView) view.findViewById(R.id.tv_griditem_mine);
            view.setTag(viewHolder);
        } else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.image_griditem_mine.setImageResource(list.get(i).getRedId());
        viewHolder.tv_griditem_mine.setText(list.get(i).getName());
        return view;
    }

    class ViewHolder{
        private ImageView image_griditem_mine;
        private TextView tv_griditem_mine;
    }
}
