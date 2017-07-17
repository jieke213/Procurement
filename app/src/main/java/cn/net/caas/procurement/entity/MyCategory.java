package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/1/17.
 */
public class MyCategory {
    private String name;
    private int redId;

    public MyCategory(String name, int redId) {
        this.name = name;
        this.redId = redId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRedId() {
        return redId;
    }

    public void setRedId(int redId) {
        this.redId = redId;
    }

}
