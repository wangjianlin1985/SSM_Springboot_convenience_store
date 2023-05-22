package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Product;
import com.chengxusheji.po.Shop;
import com.chengxusheji.po.Stock;

import com.chengxusheji.mapper.StockMapper;
@Service
public class StockService {

	@Resource StockMapper stockMapper;
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

    /*添加商品库存记录*/
    public void addStock(Stock stock) throws Exception {
    	stockMapper.addStock(stock);
    }

    /*按照查询条件分页查询商品库存记录*/
    public ArrayList<Stock> queryStock(Product productObj,Shop shopObj,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_stock.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_stock.shopObj=" + shopObj.getShopId();
    	int startIndex = (currentPage-1) * this.rows;
    	return stockMapper.queryStock(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Stock> queryStock(Product productObj,Shop shopObj) throws Exception  { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_stock.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_stock.shopObj=" + shopObj.getShopId();
    	return stockMapper.queryStockList(where);
    }

    /*查询所有商品库存记录*/
    public ArrayList<Stock> queryAllStock()  throws Exception {
        return stockMapper.queryStockList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Product productObj,Shop shopObj) throws Exception {
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_stock.productObj=" + productObj.getProductId();
    	if(null != shopObj && shopObj.getShopId()!= null && shopObj.getShopId()!= 0)  where += " and t_stock.shopObj=" + shopObj.getShopId();
        recordNumber = stockMapper.queryStockCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取商品库存记录*/
    public Stock getStock(int stockId) throws Exception  {
        Stock stock = stockMapper.getStock(stockId);
        return stock;
    }

    /*更新商品库存记录*/
    public void updateStock(Stock stock) throws Exception {
        stockMapper.updateStock(stock);
    }

    /*删除一条商品库存记录*/
    public void deleteStock (int stockId) throws Exception {
        stockMapper.deleteStock(stockId);
    }

    /*删除多条商品库存信息*/
    public int deleteStocks (String stockIds) throws Exception {
    	String _stockIds[] = stockIds.split(",");
    	for(String _stockId: _stockIds) {
    		stockMapper.deleteStock(Integer.parseInt(_stockId));
    	}
    	return _stockIds.length;
    }
}
