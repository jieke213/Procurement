package cn.net.caas.procurement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.net.caas.procurement.R;
import cn.net.caas.procurement.entity.MyPartArrivalGoods;
import cn.net.caas.procurement.entity.ViewHolder;

/**
 * Created by wjj on 2017/4/14.
 */
public class OrderAdapter4PartArrival extends BaseAdapter{
    private List<MyPartArrivalGoods> list;
    private Context context;
    // 用来控制CheckBox的选中状况
    private static Map<Integer,Boolean> map;

    public OrderAdapter4PartArrival(Context context,List<MyPartArrivalGoods> list){
        this.context=context;
        this.list=list;
        map=new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            getMap().put(i,false);
        }
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_partarrival,null);
            viewHolder=new ViewHolder();
            viewHolder.checkbox_partarrival= (CheckBox) view.findViewById(R.id.checkbox_partarrival);
            viewHolder.image_partarrival= (ImageView) view.findViewById(R.id.image_partarrival);
            viewHolder.tv_goodsName_partarrival= (TextView) view.findViewById(R.id.tv_goodsName_partarrival);
            viewHolder.tv_supplierPartId_partarrival= (TextView) view.findViewById(R.id.tv_supplierPartId_partarrival);
            viewHolder.tv_brandName_partarrival= (TextView) view.findViewById(R.id.tv_brandName_partarrival);
            viewHolder.tv_sku_partarrival= (TextView) view.findViewById(R.id.tv_sku_partarrival);
            viewHolder.tv_specifications_partarrival= (TextView) view.findViewById(R.id.tv_specifications_partarrival);
            viewHolder.tv_cycle_partarrival= (TextView) view.findViewById(R.id.tv_cycle_partarrival);
            viewHolder.tv_unitPriceStr_partarrival= (TextView) view.findViewById(R.id.tv_unitPriceStr_partarrival);
            viewHolder.tv_quantity_partarrival= (TextView) view.findViewById(R.id.tv_quantity_partarrival);
            viewHolder.tv_num_partarrival= (TextView) view.findViewById(R.id.tv_num_partarrival);
            viewHolder.layout_num_partarrival= (LinearLayout) view.findViewById(R.id.layout_num_partarrival);
            viewHolder.btn_remove_partarrival= (Button) view.findViewById(R.id.btn_remove_partarrival);
            viewHolder.btn_add_partarrival= (Button) view.findViewById(R.id.btn_add_partarrival);
            view.setTag(viewHolder);
        } else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.itemId=list.get(i).getItemId();
        viewHolder.image_partarrival.setImageResource(list.get(i).getResId());
        viewHolder.tv_goodsName_partarrival.setText(list.get(i).getGoodsName());
        viewHolder.tv_supplierPartId_partarrival.setText(list.get(i).getSupplierPartId());
        viewHolder.tv_brandName_partarrival.setText(list.get(i).getBrandName());
        viewHolder.tv_sku_partarrival.setText(list.get(i).getSku());
        viewHolder.tv_specifications_partarrival.setText(list.get(i).getSpecifications());
        viewHolder.tv_cycle_partarrival.setText(list.get(i).getCycle());
        viewHolder.tv_unitPriceStr_partarrival.setText("¥ "+list.get(i).getUnitPriceStr());
        viewHolder.tv_quantity_partarrival.setText("x"+String.valueOf(list.get(i).getQuantity()));
        Log.i("123","设置到货数量");
        viewHolder.tv_num_partarrival.setText(String.valueOf(list.get(i).getArrivalQuantity()));

        viewHolder.checkbox_partarrival.setChecked(getMap().get(i));
        viewHolder.layout_num_partarrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("123","layout被点击");
            }
        });
        //减少到货商品的数量
        viewHolder.btn_remove_partarrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("123","remove被点击");
                String quantityStr = viewHolder.tv_num_partarrival.getText().toString();
                int arrivalQuantity = Integer.parseInt(quantityStr);
                if (arrivalQuantity>0){
                    arrivalQuantity--;

                    viewHolder.tv_num_partarrival.setText(String.valueOf(arrivalQuantity));
//                    OrderAdapter4PartArrival.this.notifyDataSetChanged();//(为什么此处可以不加notifyDataSetChanged())
                }
            }
        });
        //增加到货商品的数量
        viewHolder.btn_add_partarrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("123","add被点击");
                String quantityStr = viewHolder.tv_num_partarrival.getText().toString();
                int arrivalQuantity = Integer.parseInt(quantityStr);

                int allQuantity = list.get(i).getQuantity();

                if (arrivalQuantity<allQuantity){
                    arrivalQuantity++;
                }

                viewHolder.tv_num_partarrival.setText(String.valueOf(arrivalQuantity));
//                OrderAdapter4PartArrival.this.notifyDataSetChanged();
            }
        });


        return view;
    }

//    class ViewHolder{
//        private CheckBox checkbox_partarrival;
//        private ImageView image_partarrival;
//        private TextView tv_goodsName_partarrival;
//        private TextView tv_supplierPartId_partarrival;
//        private TextView tv_brandName_partarrival;
//        private TextView tv_sku_partarrival;
//        private TextView tv_specifications_partarrival;
//        private TextView tv_cycle_partarrival;
//        private TextView tv_unitPriceStr_partarrival;
//        private TextView tv_quantity_partarrival;
//        private TextView tv_num_partarrival;
//        private LinearLayout layout_num_partarrival;
//        private Button btn_remove_partarrival;
//        private Button btn_add_partarrival;
//    }

    public static Map<Integer,Boolean> getMap(){
        return map;
    }

    public static void setMap(Map<Integer,Boolean> map){
        OrderAdapter4PartArrival.map=map;
    }

    private void toast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
