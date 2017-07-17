package cn.net.caas.procurement.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */
public class MyPartArrival4Json {
    private String access_token;
    private Long supplierOrderId;
    private List<ArrivalItem> arrivalItems;;

    public MyPartArrival4Json(String access_token, Long supplierOrderId, List<ArrivalItem> arrivalItems) {
        this.access_token = access_token;
        this.supplierOrderId = supplierOrderId;
        this.arrivalItems = arrivalItems;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getSupplierOrderId() {
        return supplierOrderId;
    }

    public void setSupplierOrderId(Long supplierOrderId) {
        this.supplierOrderId = supplierOrderId;
    }

    public List<ArrivalItem> getArrivalItems() {
        return arrivalItems;
    }

    public void setArrivalItems(List<ArrivalItem> arrivalItems) {
        this.arrivalItems = arrivalItems;
    }
}
