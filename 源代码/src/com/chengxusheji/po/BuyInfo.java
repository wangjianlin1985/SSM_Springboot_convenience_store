package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class BuyInfo {
    /*进货id*/
    private Integer buyId;
    public Integer getBuyId(){
        return buyId;
    }
    public void setBuyId(Integer buyId){
        this.buyId = buyId;
    }

    /*进货商品*/
    private Product productObj;
    public Product getProductObj() {
        return productObj;
    }
    public void setProductObj(Product productObj) {
        this.productObj = productObj;
    }

    /*进货店铺*/
    private Shop shopObj;
    public Shop getShopObj() {
        return shopObj;
    }
    public void setShopObj(Shop shopObj) {
        this.shopObj = shopObj;
    }

    /*供应商*/
    private Supplier supplierObj;
    public Supplier getSupplierObj() {
        return supplierObj;
    }
    public void setSupplierObj(Supplier supplierObj) {
        this.supplierObj = supplierObj;
    }

    /*进货单价*/
    @NotNull(message="必须输入进货单价")
    private Float buyPrice;
    public Float getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    /*进货数量*/
    @NotNull(message="必须输入进货数量")
    private Integer buyCount;
    public Integer getBuyCount() {
        return buyCount;
    }
    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    /*进货日期*/
    @NotEmpty(message="进货日期不能为空")
    private String buyDate;
    public String getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    /*进货管理员*/
    private Manager managerObj;
    public Manager getManagerObj() {
        return managerObj;
    }
    public void setManagerObj(Manager managerObj) {
        this.managerObj = managerObj;
    }

    /*进货备注*/
    private String buyMemo;
    public String getBuyMemo() {
        return buyMemo;
    }
    public void setBuyMemo(String buyMemo) {
        this.buyMemo = buyMemo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonBuyInfo=new JSONObject(); 
		jsonBuyInfo.accumulate("buyId", this.getBuyId());
		jsonBuyInfo.accumulate("productObj", this.getProductObj().getProductName());
		jsonBuyInfo.accumulate("productObjPri", this.getProductObj().getProductId());
		jsonBuyInfo.accumulate("shopObj", this.getShopObj().getShopName());
		jsonBuyInfo.accumulate("shopObjPri", this.getShopObj().getShopId());
		jsonBuyInfo.accumulate("supplierObj", this.getSupplierObj().getSupplierName());
		jsonBuyInfo.accumulate("supplierObjPri", this.getSupplierObj().getSupplierId());
		jsonBuyInfo.accumulate("buyPrice", this.getBuyPrice());
		jsonBuyInfo.accumulate("buyCount", this.getBuyCount());
		jsonBuyInfo.accumulate("buyDate", this.getBuyDate().length()>19?this.getBuyDate().substring(0,19):this.getBuyDate());
		jsonBuyInfo.accumulate("managerObj", this.getManagerObj().getName());
		jsonBuyInfo.accumulate("managerObjPri", this.getManagerObj().getManagerUserName());
		jsonBuyInfo.accumulate("buyMemo", this.getBuyMemo());
		return jsonBuyInfo;
    }}