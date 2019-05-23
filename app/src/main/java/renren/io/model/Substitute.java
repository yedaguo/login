package renren.io.model;


import java.util.Date;

public class Substitute {
    private Long id;
    /**
     * 开单日期
     */
    private Date billingDate;
    /**
     * 付款日期
     */
    private Date paymentDate;
    /**
     * 批核签字
     */
    private String signature;
    /**
     * 付款状态（0.未付款1.已付款）
     */
    private Integer paymentStatus;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 款号
     */
    private String sectionNumber;
    /**
     * 组别
     */
    private String tranches;
    /**
     * 工序号
     */
    private String employeeId;
    /**
     * 基本工价
     */
    private double basicWage;
    /**
     * 代办单价
     */
    private double chargeunitPrice;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 人员姓名
     */
    private String personnelName;
    /**
     * 人员照片
     */
    private String personnelPic;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 计酬单照片
     */
    private String payPic;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 创建人员
     */
    private Long createUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getTranches() {
        return tranches;
    }

    public void setTranches(String tranches) {
        this.tranches = tranches;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getBasicWage() {
        return basicWage;
    }

    public void setBasicWage(double basicWage) {
        this.basicWage = basicWage;
    }

    public double getChargeunitPrice() {
        return chargeunitPrice;
    }

    public void setChargeunitPrice(double chargeunitPrice) {
        this.chargeunitPrice = chargeunitPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getPersonnelPic() {
        return personnelPic;
    }

    public void setPersonnelPic(String personnelPic) {
        this.personnelPic = personnelPic;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPayPic() {
        return payPic;
    }

    public void setPayPic(String payPic) {
        this.payPic = payPic;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Substitute(Long id, Date billingDate, Date paymentDate, String signature, Integer paymentStatus, String orderNum, String sectionNumber, String tranches, String employeeId, double basicWage, double chargeunitPrice, Integer num, String personnelName, String personnelPic, String idCard, String payPic, Date createDate, Date updateDate, Long createUser) {
        this.id = id;
        this.billingDate = billingDate;
        this.paymentDate = paymentDate;
        this.signature = signature;
        this.paymentStatus = paymentStatus;
        this.orderNum = orderNum;
        this.sectionNumber = sectionNumber;
        this.tranches = tranches;
        this.employeeId = employeeId;
        this.basicWage = basicWage;
        this.chargeunitPrice = chargeunitPrice;
        this.num = num;
        this.personnelName = personnelName;
        this.personnelPic = personnelPic;
        this.idCard = idCard;
        this.payPic = payPic;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUser = createUser;
    }

    public Substitute() {
    }
}
