package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Product;
import com.chengxusheji.po.Shop;
import com.chengxusheji.po.Shop;
import com.chengxusheji.po.Transfer;

import com.chengxusheji.mapper.TransferMapper;
@Service
public class TransferService {

	@Resource TransferMapper transferMapper;
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

    /*添加商品调拨记录*/
    public void addTransfer(Transfer transfer) throws Exception {
    	transferMapper.addTransfer(transfer);
    }

    /*按照查询条件分页查询商品调拨记录*/
    public ArrayList<Transfer> queryTransfer(Product productObj,Shop shopObj1,Shop shopObj2,String transferTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_transfer.productObj=" + productObj.getProductId();
    	if(null != shopObj1 && shopObj1.getShopId()!= null && shopObj1.getShopId()!= 0)  where += " and t_transfer.shopObj1=" + shopObj1.getShopId();
    	if(null != shopObj2 && shopObj2.getShopId()!= null && shopObj2.getShopId()!= 0)  where += " and t_transfer.shopObj2=" + shopObj2.getShopId();
    	if(!transferTime.equals("")) where = where + " and t_transfer.transferTime like '%" + transferTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return transferMapper.queryTransfer(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Transfer> queryTransfer(Product productObj,Shop shopObj1,Shop shopObj2,String transferTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_transfer.productObj=" + productObj.getProductId();
    	if(null != shopObj1 && shopObj1.getShopId()!= null && shopObj1.getShopId()!= 0)  where += " and t_transfer.shopObj1=" + shopObj1.getShopId();
    	if(null != shopObj2 && shopObj2.getShopId()!= null && shopObj2.getShopId()!= 0)  where += " and t_transfer.shopObj2=" + shopObj2.getShopId();
    	if(!transferTime.equals("")) where = where + " and t_transfer.transferTime like '%" + transferTime + "%'";
    	return transferMapper.queryTransferList(where);
    }

    /*查询所有商品调拨记录*/
    public ArrayList<Transfer> queryAllTransfer()  throws Exception {
        return transferMapper.queryTransferList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Product productObj,Shop shopObj1,Shop shopObj2,String transferTime) throws Exception {
     	String where = "where 1=1";
    	if(null != productObj && productObj.getProductId()!= null && productObj.getProductId()!= 0)  where += " and t_transfer.productObj=" + productObj.getProductId();
    	if(null != shopObj1 && shopObj1.getShopId()!= null && shopObj1.getShopId()!= 0)  where += " and t_transfer.shopObj1=" + shopObj1.getShopId();
    	if(null != shopObj2 && shopObj2.getShopId()!= null && shopObj2.getShopId()!= 0)  where += " and t_transfer.shopObj2=" + shopObj2.getShopId();
    	if(!transferTime.equals("")) where = where + " and t_transfer.transferTime like '%" + transferTime + "%'";
        recordNumber = transferMapper.queryTransferCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取商品调拨记录*/
    public Transfer getTransfer(int transferId) throws Exception  {
        Transfer transfer = transferMapper.getTransfer(transferId);
        return transfer;
    }

    /*更新商品调拨记录*/
    public void updateTransfer(Transfer transfer) throws Exception {
        transferMapper.updateTransfer(transfer);
    }

    /*删除一条商品调拨记录*/
    public void deleteTransfer (int transferId) throws Exception {
        transferMapper.deleteTransfer(transferId);
    }

    /*删除多条商品调拨信息*/
    public int deleteTransfers (String transferIds) throws Exception {
    	String _transferIds[] = transferIds.split(",");
    	for(String _transferId: _transferIds) {
    		transferMapper.deleteTransfer(Integer.parseInt(_transferId));
    	}
    	return _transferIds.length;
    }
}
