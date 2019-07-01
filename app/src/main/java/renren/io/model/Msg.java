package renren.io.model;

public class Msg {
    private Integer id;
    private String orderNum;
    private String billingDate;
    private String tranches;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getTranches() {
        return tranches;
    }

    public void setTranches(String tranches) {
        this.tranches = tranches;
    }

    public Msg(Integer id, String orderNum, String billingDate, String tranches) {
        this.id = id;
        this.orderNum = orderNum;
        this.billingDate = billingDate;
        this.tranches = tranches;
    }

    public Msg() {
    }
}
