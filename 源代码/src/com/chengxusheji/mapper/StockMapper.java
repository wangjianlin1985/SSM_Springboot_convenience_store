package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Stock;

public interface StockMapper {
	/*添加商品库存信息*/
	public void addStock(Stock stock) throws Exception;

	/*按照查询条件分页查询商品库存记录*/
	public ArrayList<Stock> queryStock(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有商品库存记录*/
	public ArrayList<Stock> queryStockList(@Param("where") String where) throws Exception;

	/*按照查询条件的商品库存记录数*/
	public int queryStockCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条商品库存记录*/
	public Stock getStock(int stockId) throws Exception;

	/*更新商品库存记录*/
	public void updateStock(Stock stock) throws Exception;

	/*删除商品库存记录*/
	public void deleteStock(int stockId) throws Exception;

}
