package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/4/7.
 */
public class MyReview {
    private int resId;
    private String name;
    private int num;

    public MyReview(String name,int resId,  int num) {
        this.resId = resId;
        this.name = name;
        this.num = num;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
