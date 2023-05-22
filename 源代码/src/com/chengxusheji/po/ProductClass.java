package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductClass {
    /*商品类别id*/
    private Integer productClassId;
    public Integer getProductClassId(){
        return productClassId;
    }
    public void setProductClassId(Integer productClassId){
        this.productClassId = productClassId;
    }

    /*商品类别名称*/
    @NotEmpty(message="商品类别名称不能为空")
    private String prodcutClassName;
    public String getProdcutClassName() {
        return prodcutClassName;
    }
    public void setProdcutClassName(String prodcutClassName) {
        this.prodcutClassName = prodcutClassName;
    }

    /*类别描述*/
    private String classDesc;
    public String getClassDesc() {
        return classDesc;
    }
    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProductClass=new JSONObject(); 
		jsonProductClass.accumulate("productClassId", this.getProductClassId());
		jsonProductClass.accumulate("prodcutClassName", this.getProdcutClassName());
		jsonProductClass.accumulate("classDesc", this.getClassDesc());
		return jsonProductClass;
    }}