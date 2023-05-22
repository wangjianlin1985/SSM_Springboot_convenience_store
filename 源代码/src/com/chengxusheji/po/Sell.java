package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Sell {
    /*销售id*/
    private Integer sellId;
    public Integer getSellId(){
        return sellId;
    }
    public void setSellId(Integer sellId){
        this.sellId = sellId;
    }

    /*销售商品*/
    private Product productObj;
    public Product getProductObj() {
        return productObj;
    }
    public void setProductObj(Product productObj) {
        this.productObj = productObj;
    }

    /*销售店铺*/
    private Shop shopObj;
    public Shop getShopObj() {
        return shopObj;
    }
    public void setShopObj(Shop shopObj) {
        this.shopObj = shopObj;
    }

    /*销售价格*/
    @NotNull(message="必须输入销售价格")
    private Float sellPrice;
    public Float getSellPrice() {
        return sellPrice;
    }
    public void setSellPrice(Float sellPrice) {
        this.sellPrice = sellPrice;
    }

    /*销售数量*/
    @NotNull(message="必须输入销售数量")
    private Integer sellCount;
    public Integer getSellCount() {
        return sellCount;
    }
    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    /*销售客户*/
    private Customer customerObj;
    public Customer getCustomerObj() {
        return customerObj;
    }
    public void setCustomerObj(Customer customerObj) {
        this.customerObj = customerObj;
    }

    /*销售时间*/
    @NotEmpty(message="销售时间不能为空")
    private String sellTime;
    public String getSellTime() {
        return sellTime;
    }
    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    /*操作管理员*/
    private Manager managerObj;
    public Manager getManagerObj() {
        return managerObj;
    }
    public void setManagerObj(Manager managerObj) {
        this.managerObj = managerObj;
    }

    /*销售备注*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonSell=new JSONObject(); 
		jsonSell.accumulate("sellId", this.getSellId());
		jsonSell.accumulate("productObj", this.getProductObj().getProductName());
		jsonSell.accumulate("productObjPri", this.getProductObj().getProductId());
		jsonSell.accumulate("shopObj", this.getShopObj().getShopName());
		jsonSell.accumulate("shopObjPri", this.getShopObj().getShopId());
		jsonSell.accumulate("sellPrice", this.getSellPrice());
		jsonSell.accumulate("sellCount", this.getSellCount());
		jsonSell.accumulate("customerObj", this.getCustomerObj().getName());
		jsonSell.accumulate("customerObjPri", this.getCustomerObj().getCustomerId());
		jsonSell.accumulate("sellTime", this.getSellTime().length()>19?this.getSellTime().substring(0,19):this.getSellTime());
		jsonSell.accumulate("managerObj", this.getManagerObj().getName());
		jsonSell.accumulate("managerObjPri", this.getManagerObj().getManagerUserName());
		jsonSell.accumulate("memo", this.getMemo());
		return jsonSell;
    }}