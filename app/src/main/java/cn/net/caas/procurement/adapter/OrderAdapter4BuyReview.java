package cn.net.caas.procurement.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.net.caas.procurement.InstiuteBuyReviewActivity;
import cn.net.caas.procurement.InstiuteBuyReviewPassActivity;
import cn.net.caas.procurement.InstiuteBuyReviewRefuseActivity;
import cn.net.caas.procurement.LeaderBuyReviewActivity;
import cn.net.caas.procurement.LeaderBuyReviewPassActivity;
import cn.net.caas.procurement.LeaderBuyReviewRefuseActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.entity.MyAllOrder;
import cn.net.caas.procurement.entity.MyGoods;
import cn.net.caas.procurement.entity.MyOrder4BuyReview;

/**
 * Created by wjj on 2017/4/11.
 */
public class OrderAdapter4BuyReview extends BaseAdapter{
    private List<MyOrder4BuyReview> list;
    private Context context;

    public boolean isShowLeaderButton=false;
    public boolean isShowInstiuteButton=false;

    public OrderAdapter4BuyReview(Context context,List<MyOrder4BuyReview> list){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=new ViewHolder();
        view= LayoutInflater.from(context).inflate(R.layout.listitem_order4buyreview,null);
        viewHolder.layout_buyreview= (LinearLayout) view.findViewById(R.id.layout_buyreview);
        viewHolder.tv_status_buyreview= (TextView) view.findViewById(R.id.tv_status_buyreview);
        viewHolder.btn_ok_buyreview= (Button) view.findViewById(R.id.btn_ok_buyreview);
        viewHolder.btn_cancel_buyreview= (Button) view.findViewById(R.id.btn_cancel_buyreview);

        //默认不显示按钮
        viewHolder.btn_ok_buyreview.setVisibility(View.GONE);
        viewHolder.btn_cancel_buyreview.setVisibility(View.GONE);

        final int status = list.get(i).getStatus();
        switch (status){
            case Constants.LEADER_REVIEW:
                viewHolder.tv_status_buyreview.setText("课题组长审核中");
                if (isShowLeaderButton){
                    viewHolder.btn_ok_buyreview.setVisibility(View.VISIBLE);
                    viewHolder.btn_cancel_buyreview.setVisibility(View.VISIBLE);
                    viewHolder.btn_ok_buyreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, LeaderBuyReviewPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((LeaderBuyReviewActivity)context).startActivityForResult(intent,0011);
                        }
                    });
                    viewHolder.btn_cancel_buyreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, LeaderBuyReviewRefuseActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((LeaderBuyReviewActivity)context).startActivityForResult(intent,0021);
                        }
                    });
                }
                break;
            case Constants.INSTIUTE_REVIEW:
                viewHolder.tv_status_buyreview.setText("研究所审核中");
                if (isShowInstiuteButton){
                    viewHolder.btn_ok_buyreview.setVisibility(View.VISIBLE);
                    viewHolder.btn_cancel_buyreview.setVisibility(View.VISIBLE);
                    viewHolder.btn_ok_buyreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, InstiuteBuyReviewPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((InstiuteBuyReviewActivity)context).startActivityForResult(intent,0011);
                        }
                    });
                    viewHolder.btn_cancel_buyreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, InstiuteBuyReviewRefuseActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((InstiuteBuyReviewActivity)context).startActivityForResult(intent,0021);
                        }
                    });
                }
                break;
            case Constants.REVIEW_PASS:
                viewHolder.tv_status_buyreview.setText("审核通过");
                break;
            case Constants.CONFIRM_ORDER:
                viewHolder.tv_status_buyreview.setText("订单确认");
                break;
            case Constants.DELIVERED:
                viewHolder.tv_status_buyreview.setText("已发货");
                break;
            case Constants.CHECKING:
                viewHolder.tv_status_buyreview.setText("验货中");
                break;
            case Constants.ORDER_DONE:
                viewHolder.tv_status_buyreview.setText("订单完成");
                break;
            case Constants.LEADER_REVIEW_EXPENSES_CLAIMS:
                viewHolder.tv_status_buyreview.setText("课题组长报销审核中");
                break;
            case Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS:
                viewHolder.tv_status_buyreview.setText("研究所报销审核中");
                break;
            case Constants.CLAIMING:
                viewHolder.tv_status_buyreview.setText("报销中");
                break;
            case Constants.DONE_EXPENSES_CLAIMS:
                viewHolder.tv_status_buyreview.setText("完成报销");
                break;
            case Constants.REVIEW_NOT_PASS:
                viewHolder.tv_status_buyreview.setText("审核未通过");
                break;
            case Constants.CANCEL_ORDER:
                viewHolder.tv_status_buyreview.setText("取消订单");
                break;
            case Constants.REFUNDED_PRODUCTS:
                viewHolder.tv_status_buyreview.setText("已退货");
                break;
            case Constants.REVIEW_EXPENSES_CLAIMS_NOT_PASS:
                viewHolder.tv_status_buyreview.setText("报销未通过");
                break;
            default:
                break;
        }

        List<MyAllOrder> list_myorder = list.get(i).getList_myorder();
        for (int j = 0; j < list_myorder.size(); j++) {
            Log.i("123","list_myorder.size(): "+list_myorder.size());
            LinearLayout layout_row= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_buyreview,null);
            viewHolder.tv_sellerNick_buyreview = (TextView) layout_row.findViewById(R.id.tv_sellerNick_buyreview);
            viewHolder.layout_goods_buyreview = (LinearLayout) layout_row.findViewById(R.id.layout_goods_buyreview);
            viewHolder.tv_AllQuantity_buyreview = (TextView) layout_row.findViewById(R.id.tv_AllQuantity_buyreview);
            viewHolder.tv_amountStr_buyreview = (TextView) layout_row.findViewById(R.id.tv_amountStr_buyreview);

            viewHolder.tv_sellerNick_buyreview.setText(list_myorder.get(j).getSellerNick());
            viewHolder.tv_AllQuantity_buyreview.setText("共"+String.valueOf(list_myorder.get(j).getAllQuantity())+"件商品");
            viewHolder.tv_amountStr_buyreview.setText("合计:¥"+list_myorder.get(j).getAmountStr());

            List<MyGoods> list_goods = list_myorder.get(j).getList_goods();
            for (int k = 0; k < list_goods.size(); k++) {
                LinearLayout layout_row2= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_goods,null);
                viewHolder.layout_goods_image= (ImageView) layout_row2.findViewById(R.id.layout_goods_image);
                viewHolder.layout_goods_buyerNick= (TextView) layout_row2.findViewById(R.id.layout_goods_buyerNick);
                viewHolder.layout_goods_teamName= (TextView) layout_row2.findViewById(R.id.layout_goods_teamName);
                viewHolder.layout_goods_leaderNick= (TextView) layout_row2.findViewById(R.id.layout_goods_leaderNick);
                viewHolder.layout_goods_projFeeFrom= (TextView) layout_row2.findViewById(R.id.layout_goods_projFeeFrom);
                viewHolder.layout_goods_itemName= (TextView) layout_row2.findViewById(R.id.layout_goods_itemName);
                viewHolder.layout_goods_unitPriceStr= (TextView) layout_row2.findViewById(R.id.layout_goods_unitPriceStr);
                viewHolder.layout_goods_quantity= (TextView) layout_row2.findViewById(R.id.layout_goods_quantity);

                viewHolder.layout_goods_image.setImageResource(R.mipmap.image_goods);
                viewHolder.layout_goods_buyerNick.setText(list_goods.get(k).getBuyerNick());
                viewHolder.layout_goods_teamName.setText(list_goods.get(k).getTeamName());
                viewHolder.layout_goods_leaderNick.setText(list_goods.get(k).getLeaderNick());
                String projFeeFrom = list_goods.get(k).getProjFeeFrom();
                if (projFeeFrom.equals("") || projFeeFrom.equals("null")){
                    viewHolder.layout_goods_projFeeFrom.setText("");
                }else{
                    viewHolder.layout_goods_projFeeFrom.setText(projFeeFrom);
                }
                viewHolder.layout_goods_itemName.setText(list_goods.get(k).getItemName());
                viewHolder.layout_goods_unitPriceStr.setText("¥ "+list_goods.get(k).getUnitPriceStr());
                viewHolder.layout_goods_quantity.setText("x"+String.valueOf(list_goods.get(k).getQuantity()));
                viewHolder.layout_goods_buyreview.addView(layout_row2);
            }
            viewHolder.layout_buyreview.addView(layout_row);
        }

        return view;
    }

    class ViewHolder{
        private LinearLayout layout_buyreview;
        private TextView tv_status_buyreview;
        private Button btn_ok_buyreview;
        private Button btn_cancel_buyreview;

        private TextView tv_sellerNick_buyreview;
        private LinearLayout layout_goods_buyreview;
        private TextView tv_AllQuantity_buyreview;
        private TextView tv_amountStr_buyreview;

        private ImageView layout_goods_image;
        private TextView layout_goods_buyerNick;
        private TextView layout_goods_teamName;
        private TextView layout_goods_leaderNick;
        private TextView layout_goods_projFeeFrom;
        private TextView layout_goods_itemName;
        private TextView layout_goods_unitPriceStr;
        private TextView layout_goods_quantity;
    }

    private void toast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
