package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/3/20.
 */
public class Mine {
    private String name;
    private int redId;

    public Mine(String name, int redId) {
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
