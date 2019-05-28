package workshop.model;

import java.math.BigDecimal;

public class Product {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product.product_name
     *
     * @mbggenerated
     */
    private String productName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product.out_in
     *
     * @mbggenerated
     */
    private BigDecimal outIn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product.type
     *
     * @mbggenerated
     */
    private Byte type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product.id
     *
     * @return the value of product.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product.id
     *
     * @param id the value for product.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product.product_name
     *
     * @return the value of product.product_name
     *
     * @mbggenerated
     */
    public String getProductName() {
        return productName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product.product_name
     *
     * @param productName the value for product.product_name
     *
     * @mbggenerated
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product.out_in
     *
     * @return the value of product.out_in
     *
     * @mbggenerated
     */
    public BigDecimal getOutIn() {
        return outIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product.out_in
     *
     * @param outIn the value for product.out_in
     *
     * @mbggenerated
     */
    public void setOutIn(BigDecimal outIn) {
        this.outIn = outIn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product.type
     *
     * @return the value of product.type
     *
     * @mbggenerated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product.type
     *
     * @param type the value for product.type
     *
     * @mbggenerated
     */
    public void setType(Byte type) {
        this.type = type;
    }
}