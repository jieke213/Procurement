package cn.net.caas.procurement.entity;

import java.util.List;

/**
 * Created by wjj on 2017/4/11.
 */
public class MyOrder4BuyReview {
    private List<MyAllOrder> list_myorder;
    private int status;
    private int ID;
    private long caasOrderId;

    public MyOrder4BuyReview(List<MyAllOrder> list_myorder, int status,int ID,long caasOrderId) {
        this.list_myorder = list_myorder;
        this.status = status;
        this.ID=ID;
        this.caasOrderId=caasOrderId;
    }

    public List<MyAllOrder> getList_myorder() {
        return list_myorder;
    }

    public void setList_myorder(List<MyAllOrder> list_myorder) {
        this.list_myorder = list_myorder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getCaasOrderId() {
        return caasOrderId;
    }

    public void setCaasOrderId(long caasOrderId) {
        this.caasOrderId = caasOrderId;
    }
}
