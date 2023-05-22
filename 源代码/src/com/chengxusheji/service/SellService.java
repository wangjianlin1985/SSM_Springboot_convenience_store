package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Product;
import com.chengxusheji.po.Shop;
import com.chengxusheji.po.Customer;
import com.chengxusheji.po.Manager;
import com.chengxusheji.po.Sell;

import com.chengxusheji.mapper.SellMapper;
@Service
public class SellService {

	@Resource SellMapper sellMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加商品销售记录*/
    public void addSell(Sell sell) throws Exception {
    	sellMapper.addSell(sell);
    }

    /*按照查询条件分页查询商品销售记录*/
    public ArrayList<Sell> querySell(Product productObj,Shop shopObj,Customer customerObj,String sellTime,Manager managerObj,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_sell.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_sell.shopObj=" + shopObj.getShopId();
    	if(null != customerObj && customerObj.getCustomerId()!= null && customerObj.getCustomerId()!= 0)  where += " and t_sell.customerObj=" + customerObj.getCustomerId();
    	if(!sellTime.equals("")) where = where + " and t_sell.sellTime like '%" + sellTime + "%'";
    	if(null != managerObj &&  managerObj.getManagerUserName() != null  && !managerObj.getManagerUserName().equals(""))  where += " and t_sell.managerObj='" + managerObj.getManagerUserName() + "'";
    	int startIndex = (currentPage-1) * this.rows;
    	return sellMapper.querySell(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Sell> querySell(Product productObj,Shop shopObj,Customer customerObj,String sellTime,Manager managerObj) throws Exception  { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_sell.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_sell.shopObj=" + shopObj.getShopId();
    	if(null != customerObj && customerObj.getCustomerId()!= null && customerObj.getCustomerId()!= 0)  where += " and t_sell.customerObj=" + customerObj.getCustomerId();
    	if(!sellTime.equals("")) where = where + " and t_sell.sellTime like '%" + sellTime + "%'";
    	if(null != managerObj &&  managerObj.getManagerUserName() != null && !managerObj.getManagerUserName().equals(""))  where += " and t_sell.managerObj='" + managerObj.getManagerUserName() + "'";
    	return sellMapper.querySellList(where);
    }

    /*查询所有商品销售记录*/
    public ArrayList<Sell> queryAllSell()  throws Exception {
        return sellMapper.querySellList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Product productObj,Shop shopObj,Customer customerObj,String sellTime,Manager managerObj) throws Exception {
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_sell.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_sell.shopObj=" + shopObj.getShopId();
    	if(null != customerObj && customerObj.getCustomerId()!= null && customerObj.getCustomerId()!= 0)  where += " and t_sell.customerObj=" + customerObj.getCustomerId();
    	if(!sellTime.equals("")) where = where + " and t_sell.sellTime like '%" + sellTime + "%'";
    	if(null != managerObj &&  managerObj.getManagerUserName() != null && !managerObj.getManagerUserName().equals(""))  where += " and t_sell.managerObj='" + managerObj.getManagerUserName() + "'";
        recordNumber = sellMapper.querySellCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取商品销售记录*/
    public Sell getSell(int sellId) throws Exception  {
        Sell sell = sellMapper.getSell(sellId);
        return sell;
    }

    /*更新商品销售记录*/
    public void updateSell(Sell sell) throws Exception {
        sellMapper.updateSell(sell);
    }

    /*删除一条商品销售记录*/
    public void deleteSell (int sellId) throws Exception {
        sellMapper.deleteSell(sellId);
    }

    /*删除多条商品销售信息*/
    public int deleteSells (String sellIds) throws Exception {
    	String _sellIds[] = sellIds.split(",");
    	for(String _sellId: _sellIds) {
    		sellMapper.deleteSell(Integer.parseInt(_sellId));
    	}
    	return _sellIds.length;
    }
}
