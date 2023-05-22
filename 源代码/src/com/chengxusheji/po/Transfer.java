package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Transfer {
    /*调拨id*/
    private Integer transferId;
    public Integer getTransferId(){
        return transferId;
    }
    public void setTransferId(Integer transferId){
        this.transferId = transferId;
    }

    /*调拨商品*/
    private Product productObj;
    public Product getProductObj() {
        return productObj;
    }
    public void setProductObj(Product productObj) {
        this.productObj = productObj;
    }

    /*调拨数量*/
    @NotNull(message="必须输入调拨数量")
    private Integer transferCount;
    public Integer getTransferCount() {
        return transferCount;
    }
    public void setTransferCount(Integer transferCount) {
        this.transferCount = transferCount;
    }

    /*调出店铺*/
    private Shop shopObj1;
    public Shop getShopObj1() {
        return shopObj1;
    }
    public void setShopObj1(Shop shopObj1) {
        this.shopObj1 = shopObj1;
    }

    /*调入店铺*/
    private Shop shopObj2;
    public Shop getShopObj2() {
        return shopObj2;
    }
    public void setShopObj2(Shop shopObj2) {
        this.shopObj2 = shopObj2;
    }

    /*调拨时间*/
    @NotEmpty(message="调拨时间不能为空")
    private String transferTime;
    public String getTransferTime() {
        return transferTime;
    }
    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    /*调拨备注*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonTransfer=new JSONObject(); 
		jsonTransfer.accumulate("transferId", this.getTransferId());
		jsonTransfer.accumulate("productObj", this.getProductObj().getProductName());
		jsonTransfer.accumulate("productObjPri", this.getProductObj().getProductId());
		jsonTransfer.accumulate("transferCount", this.getTransferCount());
		jsonTransfer.accumulate("shopObj1", this.getShopObj1().getShopName());
		jsonTransfer.accumulate("shopObj1Pri", this.getShopObj1().getShopId());
		jsonTransfer.accumulate("shopObj2", this.getShopObj2().getShopName());
		jsonTransfer.accumulate("shopObj2Pri", this.getShopObj2().getShopId());
		jsonTransfer.accumulate("transferTime", this.getTransferTime().length()>19?this.getTransferTime().substring(0,19):this.getTransferTime());
		jsonTransfer.accumulate("memo", this.getMemo());
		return jsonTransfer;
    }}