package workshop.model;

public class SystemParam {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_param.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_param.sys_key
     *
     * @mbggenerated
     */
    private String sysKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_param.sys_value
     *
     * @mbggenerated
     */
    private String sysValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_param.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system_param.id
     *
     * @return the value of system_param.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system_param.id
     *
     * @param id the value for system_param.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system_param.sys_key
     *
     * @return the value of system_param.sys_key
     *
     * @mbggenerated
     */
    public String getSysKey() {
        return sysKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system_param.sys_key
     *
     * @param sysKey the value for system_param.sys_key
     *
     * @mbggenerated
     */
    public void setSysKey(String sysKey) {
        this.sysKey = sysKey == null ? null : sysKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system_param.sys_value
     *
     * @return the value of system_param.sys_value
     *
     * @mbggenerated
     */
    public String getSysValue() {
        return sysValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system_param.sys_value
     *
     * @param sysValue the value for system_param.sys_value
     *
     * @mbggenerated
     */
    public void setSysValue(String sysValue) {
        this.sysValue = sysValue == null ? null : sysValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system_param.remark
     *
     * @return the value of system_param.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system_param.remark
     *
     * @param remark the value for system_param.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}