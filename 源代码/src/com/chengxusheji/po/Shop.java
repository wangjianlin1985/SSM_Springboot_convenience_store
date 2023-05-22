package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Shop {
    /*店铺id*/
    private Integer shopId;
    public Integer getShopId(){
        return shopId;
    }
    public void setShopId(Integer shopId){
        this.shopId = shopId;
    }

    /*店铺名称*/
    @NotEmpty(message="店铺名称不能为空")
    private String shopName;
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /*联系人*/
    @NotEmpty(message="联系人不能为空")
    private String connectPerson;
    public String getConnectPerson() {
        return connectPerson;
    }
    public void setConnectPerson(String connectPerson) {
        this.connectPerson = connectPerson;
    }

    /*联系电话*/
    @NotEmpty(message="联系电话不能为空")
    private String connectPhone;
    public String getConnectPhone() {
        return connectPhone;
    }
    public void setConnectPhone(String connectPhone) {
        this.connectPhone = connectPhone;
    }

    /*店铺地址*/
    @NotEmpty(message="店铺地址不能为空")
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonShop=new JSONObject(); 
		jsonShop.accumulate("shopId", this.getShopId());
		jsonShop.accumulate("shopName", this.getShopName());
		jsonShop.accumulate("connectPerson", this.getConnectPerson());
		jsonShop.accumulate("connectPhone", this.getConnectPhone());
		jsonShop.accumulate("address", this.getAddress());
		return jsonShop;
    }}