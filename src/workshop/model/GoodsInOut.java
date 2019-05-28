package workshop.model;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsInOut {
    private Integer id;

    private Integer productId;

    private String productName;

    private String batchno;

    private Integer num;

    private BigDecimal total;

    private String addPhone;

    private String addUserName;

    private String upDownId;

    private String deptName;

    private String direction;

    private String outBusinessName;

    private String outBusinessAddress;

    private String outBusinessPhone;

    private Date addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno == null ? null : batchno.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getAddPhone() {
        return addPhone;
    }

    public void setAddPhone(String addPhone) {
        this.addPhone = addPhone == null ? null : addPhone.trim();
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName == null ? null : addUserName.trim();
    }

    public String getUpDownId() {
        return upDownId;
    }

    public void setUpDownId(String upDownId) {
        this.upDownId = upDownId == null ? null : upDownId.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public String getOutBusinessName() {
        return outBusinessName;
    }

    public void setOutBusinessName(String outBusinessName) {
        this.outBusinessName = outBusinessName == null ? null : outBusinessName.trim();
    }

    public String getOutBusinessAddress() {
        return outBusinessAddress;
    }

    public void setOutBusinessAddress(String outBusinessAddress) {
        this.outBusinessAddress = outBusinessAddress == null ? null : outBusinessAddress.trim();
    }

    public String getOutBusinessPhone() {
        return outBusinessPhone;
    }

    public void setOutBusinessPhone(String outBusinessPhone) {
        this.outBusinessPhone = outBusinessPhone == null ? null : outBusinessPhone.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}