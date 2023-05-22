package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Supplier {
    /*供应商id*/
    private Integer supplierId;
    public Integer getSupplierId(){
        return supplierId;
    }
    public void setSupplierId(Integer supplierId){
        this.supplierId = supplierId;
    }

    /*供应商名称*/
    @NotEmpty(message="供应商名称不能为空")
    private String supplierName;
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /*法人代表*/
    @NotEmpty(message="法人代表不能为空")
    private String farendaibiao;
    public String getFarendaibiao() {
        return farendaibiao;
    }
    public void setFarendaibiao(String farendaibiao) {
        this.farendaibiao = farendaibiao;
    }

    /*供应商电话*/
    @NotEmpty(message="供应商电话不能为空")
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*供应商地址*/
    @NotEmpty(message="供应商地址不能为空")
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonSupplier=new JSONObject(); 
		jsonSupplier.accumulate("supplierId", this.getSupplierId());
		jsonSupplier.accumulate("supplierName", this.getSupplierName());
		jsonSupplier.accumulate("farendaibiao", this.getFarendaibiao());
		jsonSupplier.accumulate("telephone", this.getTelephone());
		jsonSupplier.accumulate("address", this.getAddress());
		return jsonSupplier;
    }}