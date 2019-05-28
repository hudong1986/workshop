package workshop.model;

import java.math.BigDecimal;
import java.util.Date;

public class Salary {
    private Integer id;

    private Integer userId;

    private String phone;

    private String userName;

    private String upDownId;

    private String deptName;

    private BigDecimal fixMoney = new BigDecimal(0);

    private String fixMoneyDes;

    private BigDecimal achievement= new BigDecimal(0);

    private String achievementDes;

    private BigDecimal holidayReduce= new BigDecimal(0);

    private String holidayDes;

    private BigDecimal addWork= new BigDecimal(0);

    private String addWorkDes;

    private BigDecimal fuliMoney= new BigDecimal(0);

    private String fuliDes;

    private BigDecimal rewardMoney= new BigDecimal(0);

    private String rewardDes;

    private BigDecimal total= new BigDecimal(0);

    private String month;

    private BigDecimal otherReduce= new BigDecimal(0);

    private String otherReduceDes;

    private BigDecimal otherAdd= new BigDecimal(0);

    private String otherAddDes;

    private Date addtime;

    private Integer addUserId;

    private String addUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public BigDecimal getFixMoney() {
        return fixMoney;
    }

    public void setFixMoney(BigDecimal fixMoney) {
        this.fixMoney = fixMoney;
    }

    public String getFixMoneyDes() {
        return fixMoneyDes;
    }

    public void setFixMoneyDes(String fixMoneyDes) {
        this.fixMoneyDes = fixMoneyDes == null ? null : fixMoneyDes.trim();
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(BigDecimal achievement) {
        this.achievement = achievement;
    }

    public String getAchievementDes() {
        return achievementDes;
    }

    public void setAchievementDes(String achievementDes) {
        this.achievementDes = achievementDes == null ? null : achievementDes.trim();
    }

    public BigDecimal getHolidayReduce() {
        return holidayReduce;
    }

    public void setHolidayReduce(BigDecimal holidayReduce) {
        this.holidayReduce = holidayReduce;
    }

    public String getHolidayDes() {
        return holidayDes;
    }

    public void setHolidayDes(String holidayDes) {
        this.holidayDes = holidayDes == null ? null : holidayDes.trim();
    }

    public BigDecimal getAddWork() {
        return addWork;
    }

    public void setAddWork(BigDecimal addWork) {
        this.addWork = addWork;
    }

    public String getAddWorkDes() {
        return addWorkDes;
    }

    public void setAddWorkDes(String addWorkDes) {
        this.addWorkDes = addWorkDes == null ? null : addWorkDes.trim();
    }

    public BigDecimal getFuliMoney() {
        return fuliMoney;
    }

    public void setFuliMoney(BigDecimal fuliMoney) {
        this.fuliMoney = fuliMoney;
    }

    public String getFuliDes() {
        return fuliDes;
    }

    public void setFuliDes(String fuliDes) {
        this.fuliDes = fuliDes == null ? null : fuliDes.trim();
    }

    public BigDecimal getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(BigDecimal rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getRewardDes() {
        return rewardDes;
    }

    public void setRewardDes(String rewardDes) {
        this.rewardDes = rewardDes == null ? null : rewardDes.trim();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public BigDecimal getOtherReduce() {
        return otherReduce;
    }

    public void setOtherReduce(BigDecimal otherReduce) {
        this.otherReduce = otherReduce;
    }

    public String getOtherReduceDes() {
        return otherReduceDes;
    }

    public void setOtherReduceDes(String otherReduceDes) {
        this.otherReduceDes = otherReduceDes == null ? null : otherReduceDes.trim();
    }

    public BigDecimal getOtherAdd() {
        return otherAdd;
    }

    public void setOtherAdd(BigDecimal otherAdd) {
        this.otherAdd = otherAdd;
    }

    public String getOtherAddDes() {
        return otherAddDes;
    }

    public void setOtherAddDes(String otherAddDes) {
        this.otherAddDes = otherAddDes == null ? null : otherAddDes.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName == null ? null : addUserName.trim();
    }
}