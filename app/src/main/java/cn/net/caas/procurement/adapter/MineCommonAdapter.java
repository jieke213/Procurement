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
import cn.net.caas.procurement.entity.MineCommon;

/**
 * Created by wjj on 2017/6/23.
 */
public class MineCommonAdapter extends BaseAdapter {
    private Context context;
    private List<MineCommon> list;

    public MineCommonAdapter(Context context,List<MineCommon> list){
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
            view= LayoutInflater.from(context).inflate(R.layout.gridviewitem_minecommon,null);
            viewHolder=new ViewHolder();
            viewHolder.image_griditem_minecommon= (ImageView) view.findViewById(R.id.image_griditem_minecommon);
            viewHolder.tv_griditem_minecommon= (TextView) view.findViewById(R.id.tv_griditem_minecommon);
            viewHolder.arrow_griditem_minecommon= (ImageView) view.findViewById(R.id.arrow_griditem_minecommon);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.image_griditem_minecommon.setImageResource(list.get(i).getResId());
        viewHolder.tv_griditem_minecommon.setText(list.get(i).getName());
        viewHolder.arrow_griditem_minecommon.setImageResource(list.get(i).getArrow_enter());

        return view;
    }

    class ViewHolder{
        private ImageView image_griditem_minecommon;
        private TextView tv_griditem_minecommon;
        private ImageView arrow_griditem_minecommon;
    }
}
