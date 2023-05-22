package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Stock {
    /*库存id*/
    private Integer stockId;
    public Integer getStockId(){
        return stockId;
    }
    public void setStockId(Integer stockId){
        this.stockId = stockId;
    }

    /*商品名称*/
    private Product productObj;
    public Product getProductObj() {
        return productObj;
    }
    public void setProductObj(Product productObj) {
        this.productObj = productObj;
    }

    /*所属店铺*/
    private Shop shopObj;
    public Shop getShopObj() {
        return shopObj;
    }
    public void setShopObj(Shop shopObj) {
        this.shopObj = shopObj;
    }

    /*剩余库存量*/
    @NotNull(message="必须输入剩余库存量")
    private Integer leftCount;
    public Integer getLeftCount() {
        return leftCount;
    }
    public void setLeftCount(Integer leftCount) {
        this.leftCount = leftCount;
    }

    /*备注信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonStock=new JSONObject(); 
		jsonStock.accumulate("stockId", this.getStockId());
		jsonStock.accumulate("productObj", this.getProductObj().getProductName());
		jsonStock.accumulate("productObjPri", this.getProductObj().getProductId());
		jsonStock.accumulate("shopObj", this.getShopObj().getShopName());
		jsonStock.accumulate("shopObjPri", this.getShopObj().getShopId());
		jsonStock.accumulate("leftCount", this.getLeftCount());
		jsonStock.accumulate("memo", this.getMemo());
		return jsonStock;
    }}