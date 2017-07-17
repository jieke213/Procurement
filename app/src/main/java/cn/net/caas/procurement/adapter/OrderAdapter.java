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

import cn.net.caas.procurement.BuyerApplyClaimsActivity;
import cn.net.caas.procurement.CheckPassActivity;
import cn.net.caas.procurement.CheckedActivity;
import cn.net.caas.procurement.CompleteRefundActivity;
import cn.net.caas.procurement.InstiuteToclaimeReviewActivity;
import cn.net.caas.procurement.InstiuteToclaimeReviewNotpassActivity;
import cn.net.caas.procurement.InstiuteToclaimeReviewPassActivity;
import cn.net.caas.procurement.LeaderToclaimeReviewActivity;
import cn.net.caas.procurement.LeaderToclaimeReviewNotpassActivity;
import cn.net.caas.procurement.LeaderToclaimeReviewPassActivity;
import cn.net.caas.procurement.MyOrderActivity4Buyer4Leader;
import cn.net.caas.procurement.PartArrivalActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.constant.RequestCode;
import cn.net.caas.procurement.entity.MyAllOrder;
import cn.net.caas.procurement.entity.MyGoods;

/**
 * Created by wjj on 2017/2/20.
 */
public class OrderAdapter extends BaseAdapter {
    private Context context =null;
    private List<MyAllOrder> list=null;
    private ViewHolder viewHolder=null;

    public boolean isShowLeaderButton=false;
    public boolean isShowInstiuteButton=false;
    public boolean isShowCheckButton=false;
    public boolean isShowApplyClaimsButton=false;

    public OrderAdapter(Context context, List<MyAllOrder> list){
        this.context =context;
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
        viewHolder=new ViewHolder();
        view = LayoutInflater.from(context).inflate(R.layout.listitem_order, null);
        viewHolder.listitem_sellerNick= (TextView) view.findViewById(R.id.listitem_sellerNick);
        viewHolder.listitem_status= (TextView) view.findViewById(R.id.listitem_status);
        viewHolder.layout_goods= (LinearLayout) view.findViewById(R.id.layout_goods);
        viewHolder.listitem_allQuantity= (TextView) view.findViewById(R.id.listitem_AllQuantity);
        viewHolder.listitem_amountStr= (TextView) view.findViewById(R.id.listitem_amountStr);
        //审核的通过和拒绝
        viewHolder.listitem_btn_ok= (Button) view.findViewById(R.id.listitem_btn_ok);
        viewHolder.listitem_btn_cancel= (Button) view.findViewById(R.id.listitem_btn_cancel);
        //默认不显示审核按钮
        viewHolder.listitem_btn_ok.setVisibility(View.GONE);
        viewHolder.listitem_btn_cancel.setVisibility(View.GONE);

        //验货
        viewHolder.listitem_btn_checkPass= (Button) view.findViewById(R.id.listitem_btn_checkPass);
        viewHolder.listitem_btn_partArrival= (Button) view.findViewById(R.id.listitem_btn_partArrival);
        viewHolder.listitem_btn_completeRefund= (Button) view.findViewById(R.id.listitem_btn_completeRefund);
        //默认不显示验货按钮
        viewHolder.listitem_btn_checkPass.setVisibility(View.GONE);
        viewHolder.listitem_btn_partArrival.setVisibility(View.GONE);
        viewHolder.listitem_btn_completeRefund.setVisibility(View.GONE);

        //申请报销
        viewHolder.listitem_btn_applyClaims= (Button) view.findViewById(R.id.listitem_btn_applyClaims);
        viewHolder.listitem_btn_applyClaims.setVisibility(View.GONE);

        viewHolder.listitem_sellerNick.setText(list.get(i).getSellerNick());
        viewHolder.listitem_amountStr.setText("合计:¥"+list.get(i).getAmountStr());
        viewHolder.listitem_allQuantity.setText("共"+String.valueOf(list.get(i).getAllQuantity())+"件商品");
        final int status = list.get(i).getStatus();
        switch (status){
            case Constants.LEADER_REVIEW:
                viewHolder.listitem_status.setText("课题组长审核中");
                break;
            case Constants.INSTIUTE_REVIEW:
                viewHolder.listitem_status.setText("研究所审核中");
                break;
            case Constants.REVIEW_PASS:
                viewHolder.listitem_status.setText("审核通过");
                //审核通过后，不显示审核按钮
                viewHolder.listitem_btn_ok.setVisibility(View.GONE);
                viewHolder.listitem_btn_cancel.setVisibility(View.GONE);
                break;
            case Constants.CONFIRM_ORDER:
                viewHolder.listitem_status.setText("订单确认");
                break;
            case Constants.DELIVERED:
                viewHolder.listitem_status.setText("已发货");
                if (isShowCheckButton){
                    viewHolder.listitem_btn_checkPass.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_partArrival.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_completeRefund.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_checkPass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, CheckPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.CHECKPASS_TOCHECK);
                        }
                    });
                    viewHolder.listitem_btn_partArrival.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, PartArrivalActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.PARTARRIVAL_TOCHECK);
                        }
                    });
                    viewHolder.listitem_btn_completeRefund.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, CompleteRefundActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.COMPLETEREFUND_TOCHECK);
                        }
                    });
                }
                break;
            case Constants.CHECKING:
                viewHolder.listitem_status.setText("部分到货");
                if (isShowCheckButton){
                    viewHolder.listitem_btn_checkPass.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_partArrival.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_completeRefund.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_checkPass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, CheckPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.CHECKPASS_PARTARRIVAL);
                        }
                    });
                    viewHolder.listitem_btn_partArrival.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, PartArrivalActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.PARTARRIVAL_PARTARRIVAL);
                        }
                    });
                    viewHolder.listitem_btn_completeRefund.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, CompleteRefundActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((CheckedActivity)context).startActivityForResult(intent,RequestCode.COMPLETEREFUND_PARTARRIVAL);
                        }
                    });
                }
                break;
            case Constants.ORDER_DONE:
                viewHolder.listitem_status.setText("订单完成");
                Log.i("12345","OrderAdapter订单完成");
                if (isShowApplyClaimsButton){
                    viewHolder.listitem_btn_applyClaims.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_applyClaims.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, BuyerApplyClaimsActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((MyOrderActivity4Buyer4Leader)context).startActivityForResult(intent, RequestCode.APPLYCLAIMS_MYORDER);
                        }
                    });
                }
                break;
            case Constants.LEADER_REVIEW_EXPENSES_CLAIMS:
                viewHolder.listitem_status.setText("课题组长报销审核中");
                //课题组长报销审核中的状态，显示审核按钮
                if (isShowLeaderButton){
                    viewHolder.listitem_btn_ok.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_cancel.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, LeaderToclaimeReviewPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((LeaderToclaimeReviewActivity)context).startActivityForResult(intent,RequestCode.LEADER_TOCLAIME_PASS);
                        }
                    });
                    viewHolder.listitem_btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, LeaderToclaimeReviewNotpassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((LeaderToclaimeReviewActivity)context).startActivityForResult(intent,RequestCode.LEADER_TOCLAIME_REFUSE);
                        }
                    });
                }
                break;
            case Constants.INSTIUTE_REVIEW_EXPENSES_CLAIMS:
                viewHolder.listitem_status.setText("研究所报销审核中");
                //研究所报销审核中的状态，显示审核按钮
                if (isShowInstiuteButton){
                    viewHolder.listitem_btn_ok.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_cancel.setVisibility(View.VISIBLE);
                    viewHolder.listitem_btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, InstiuteToclaimeReviewPassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((InstiuteToclaimeReviewActivity)context).startActivityForResult(intent,RequestCode.INSTIUTE_TOCLAIME_PASS);
                        }
                    });
                    viewHolder.listitem_btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, InstiuteToclaimeReviewNotpassActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putLong("caasOrderId",list.get(i).getCaasOrderId());
                            bundle.putLong("supplierOrderId",list.get(i).getSupplierOrderId());
                            bundle.putInt("status",list.get(i).getStatus());
                            intent.putExtras(bundle);
                            ((InstiuteToclaimeReviewActivity)context).startActivityForResult(intent,RequestCode.INSTIUTE_TOCLAIME_REFUSE);
                        }
                    });
                }
                break;
            case Constants.CLAIMING:
                viewHolder.listitem_status.setText("报销中");
                break;
            case Constants.DONE_EXPENSES_CLAIMS:
                viewHolder.listitem_status.setText("完成报销");
                break;
            case Constants.REVIEW_NOT_PASS:
                viewHolder.listitem_status.setText("审核未通过");
                break;
            case Constants.CANCEL_ORDER:
                viewHolder.listitem_status.setText("取消订单");
                Log.i("12345","OrderAdapter取消订单");
                break;
            case Constants.REFUNDED_PRODUCTS:
                viewHolder.listitem_status.setText("已退货");
                break;
            case Constants.REVIEW_EXPENSES_CLAIMS_NOT_PASS:
                viewHolder.listitem_status.setText("报销未通过");
                break;
            default:
                break;
        }


        //商品列表
        List<MyGoods> list_goods = list.get(i).getList_goods();
        Log.i("1234","OrderAdapter------------list_goods.size():"+list_goods.size());
        for (int j = 0; j < list_goods.size(); j++) {
            LinearLayout layout_row= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_goods,null);
            viewHolder.layout_goods_image= (ImageView) layout_row.findViewById(R.id.layout_goods_image);
            viewHolder.layout_goods_buyerNick= (TextView) layout_row.findViewById(R.id.layout_goods_buyerNick);
            viewHolder.layout_goods_teamName= (TextView) layout_row.findViewById(R.id.layout_goods_teamName);
            viewHolder.layout_goods_leaderNick= (TextView) layout_row.findViewById(R.id.layout_goods_leaderNick);
            viewHolder.layout_goods_projFeeFrom= (TextView) layout_row.findViewById(R.id.layout_goods_projFeeFrom);
            viewHolder.layout_goods_itemName= (TextView) layout_row.findViewById(R.id.layout_goods_itemName);
            viewHolder.layout_goods_unitPriceStr= (TextView) layout_row.findViewById(R.id.layout_goods_unitPriceStr);
            viewHolder.layout_goods_quantity= (TextView) layout_row.findViewById(R.id.layout_goods_quantity);

            viewHolder.layout_goods_image.setImageResource(R.mipmap.image_goods);
            viewHolder.layout_goods_buyerNick.setText(list_goods.get(j).getBuyerNick());
            viewHolder.layout_goods_teamName.setText(list_goods.get(j).getTeamName());
            viewHolder.layout_goods_leaderNick.setText(list_goods.get(j).getLeaderNick());
            viewHolder.layout_goods_projFeeFrom.setText(list_goods.get(j).getProjFeeFrom());
            viewHolder.layout_goods_itemName.setText(list_goods.get(j).getItemName());
            viewHolder.layout_goods_unitPriceStr.setText("¥ "+list_goods.get(j).getUnitPriceStr());
            viewHolder.layout_goods_quantity.setText("x"+String.valueOf(list_goods.get(j).getQuantity()));
            viewHolder.layout_goods.addView(layout_row);
        }

        return view;
    }

    class ViewHolder{
        private TextView listitem_sellerNick;
        private TextView listitem_status;
        private LinearLayout layout_goods;
        private TextView listitem_allQuantity;
        private TextView listitem_amountStr;
        private Button listitem_btn_ok;//审核通过
        private Button listitem_btn_cancel;//审核拒绝
        private Button listitem_btn_checkPass;//验货通过
        private Button listitem_btn_partArrival;//部分到货
        private Button listitem_btn_completeRefund;//全部退货
        private Button listitem_btn_applyClaims;//申请报销

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
