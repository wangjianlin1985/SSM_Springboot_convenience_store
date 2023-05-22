package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Shop;

public interface ShopMapper {
	/*添加门店信息*/
	public void addShop(Shop shop) throws Exception;

	/*按照查询条件分页查询门店记录*/
	public ArrayList<Shop> queryShop(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有门店记录*/
	public ArrayList<Shop> queryShopList(@Param("where") String where) throws Exception;

	/*按照查询条件的门店记录数*/
	public int queryShopCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条门店记录*/
	public Shop getShop(int shopId) throws Exception;

	/*更新门店记录*/
	public void updateShop(Shop shop) throws Exception;

	/*删除门店记录*/
	public void deleteShop(int shopId) throws Exception;

}
