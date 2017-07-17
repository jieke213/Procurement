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
import cn.net.caas.procurement.entity.MyReview;

/**
 * Created by wjj on 2017/4/7.
 */
public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private List<MyReview> list;

    public ReviewAdapter(Context context,List<MyReview> list){
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
        if (view == null){
            view=LayoutInflater.from(context).inflate(R.layout.gridviewitem_review,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_review_name= (TextView) view.findViewById(R.id.tv_review_name);
            viewHolder.tv_review_num= (TextView) view.findViewById(R.id.tv_review_num);
            viewHolder.image_review= (ImageView) view.findViewById(R.id.image_review);
            view.setTag(viewHolder);
        } else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_review_name.setText(list.get(i).getName());
        viewHolder.image_review.setImageResource(R.drawable.pic_order_up);
        int num=list.get(i).getNum();
        if (num>0){
            viewHolder.tv_review_num.setVisibility(View.VISIBLE);
            viewHolder.tv_review_num.setText(String.valueOf(list.get(i).getNum()));
        }

        return view;
    }

    class ViewHolder{
        private TextView tv_review_name;
        private TextView tv_review_num;
        private ImageView image_review;
    }
}
