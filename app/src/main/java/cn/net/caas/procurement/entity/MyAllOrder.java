package cn.net.caas.procurement.entity;

import java.util.List;

/**
 * Created by wjj on 2017/2/20.
 */
public class MyAllOrder {
    private int ID;
    private long caasOrderId;
    private int ID2;
    private long supplierOrderId;
    private String sellerNick;
    private int status;
    private List<MyGoods> list_goods;
    private int allQuantity;
    private String amountStr;

    public MyAllOrder(int ID,long caasOrderId,int ID2,long supplierOrderId,String sellerNick, int status, List<MyGoods> list_goods, int allQuantity, String amountStr) {
        this.sellerNick = sellerNick;
        this.status = status;
        this.list_goods = list_goods;
        this.allQuantity = allQuantity;
        this.amountStr = amountStr;
        this.ID=ID;
        this.caasOrderId=caasOrderId;
        this.ID2=ID2;
        this.supplierOrderId=supplierOrderId;
    }

    public long getCaasOrderId() {
        return caasOrderId;
    }

    public void setCaasOrderId(long caasOrderId) {
        this.caasOrderId = caasOrderId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID2() {
        return ID2;
    }

    public void setID2(int ID2) {
        this.ID2 = ID2;
    }

    public long getSupplierOrderId() {
        return supplierOrderId;
    }

    public void setSupplierOrderId(long supplierOrderId) {
        this.supplierOrderId = supplierOrderId;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MyGoods> getList_goods() {
        return list_goods;
    }

    public void setList_goods(List<MyGoods> list_goods) {
        this.list_goods = list_goods;
    }

    public int getAllQuantity() {
        return allQuantity;
    }

    public void setAllQuantity(int allQuantity) {
        this.allQuantity = allQuantity;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }
}
