package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Shop;

import com.chengxusheji.mapper.ShopMapper;
@Service
public class ShopService {

	@Resource ShopMapper shopMapper;
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

    /*添加门店记录*/
    public void addShop(Shop shop) throws Exception {
    	shopMapper.addShop(shop);
    }

    /*按照查询条件分页查询门店记录*/
    public ArrayList<Shop> queryShop(String shopName,String connectPerson,String connectPhone,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!shopName.equals("")) where = where + " and t_shop.shopName like '%" + shopName + "%'";
    	if(!connectPerson.equals("")) where = where + " and t_shop.connectPerson like '%" + connectPerson + "%'";
    	if(!connectPhone.equals("")) where = where + " and t_shop.connectPhone like '%" + connectPhone + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return shopMapper.queryShop(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Shop> queryShop(String shopName,String connectPerson,String connectPhone) throws Exception  { 
     	String where = "where 1=1";
    	if(!shopName.equals("")) where = where + " and t_shop.shopName like '%" + shopName + "%'";
    	if(!connectPerson.equals("")) where = where + " and t_shop.connectPerson like '%" + connectPerson + "%'";
    	if(!connectPhone.equals("")) where = where + " and t_shop.connectPhone like '%" + connectPhone + "%'";
    	return shopMapper.queryShopList(where);
    }

    /*查询所有门店记录*/
    public ArrayList<Shop> queryAllShop()  throws Exception {
        return shopMapper.queryShopList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String shopName,String connectPerson,String connectPhone) throws Exception {
     	String where = "where 1=1";
    	if(!shopName.equals("")) where = where + " and t_shop.shopName like '%" + shopName + "%'";
    	if(!connectPerson.equals("")) where = where + " and t_shop.connectPerson like '%" + connectPerson + "%'";
    	if(!connectPhone.equals("")) where = where + " and t_shop.connectPhone like '%" + connectPhone + "%'";
        recordNumber = shopMapper.queryShopCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取门店记录*/
    public Shop getShop(int shopId) throws Exception  {
        Shop shop = shopMapper.getShop(shopId);
        return shop;
    }

    /*更新门店记录*/
    public void updateShop(Shop shop) throws Exception {
        shopMapper.updateShop(shop);
    }

    /*删除一条门店记录*/
    public void deleteShop (int shopId) throws Exception {
        shopMapper.deleteShop(shopId);
    }

    /*删除多条门店信息*/
    public int deleteShops (String shopIds) throws Exception {
    	String _shopIds[] = shopIds.split(",");
    	for(String _shopId: _shopIds) {
    		shopMapper.deleteShop(Integer.parseInt(_shopId));
    	}
    	return _shopIds.length;
    }
}
