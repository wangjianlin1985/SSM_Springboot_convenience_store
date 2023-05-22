package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Customer;

import com.chengxusheji.mapper.CustomerMapper;
@Service
public class CustomerService {

	@Resource CustomerMapper customerMapper;
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

    /*添加客户记录*/
    public void addCustomer(Customer customer) throws Exception {
    	customerMapper.addCustomer(customer);
    }

    /*按照查询条件分页查询客户记录*/
    public ArrayList<Customer> queryCustomer(String name,String birthDate,String telephone,String regTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_customer.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_customer.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_customer.telephone like '%" + telephone + "%'";
    	if(!regTime.equals("")) where = where + " and t_customer.regTime like '%" + regTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return customerMapper.queryCustomer(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Customer> queryCustomer(String name,String birthDate,String telephone,String regTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_customer.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_customer.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_customer.telephone like '%" + telephone + "%'";
    	if(!regTime.equals("")) where = where + " and t_customer.regTime like '%" + regTime + "%'";
    	return customerMapper.queryCustomerList(where);
    }

    /*查询所有客户记录*/
    public ArrayList<Customer> queryAllCustomer()  throws Exception {
        return customerMapper.queryCustomerList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String name,String birthDate,String telephone,String regTime) throws Exception {
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_customer.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_customer.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_customer.telephone like '%" + telephone + "%'";
    	if(!regTime.equals("")) where = where + " and t_customer.regTime like '%" + regTime + "%'";
        recordNumber = customerMapper.queryCustomerCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取客户记录*/
    public Customer getCustomer(int customerId) throws Exception  {
        Customer customer = customerMapper.getCustomer(customerId);
        return customer;
    }

    /*更新客户记录*/
    public void updateCustomer(Customer customer) throws Exception {
        customerMapper.updateCustomer(customer);
    }

    /*删除一条客户记录*/
    public void deleteCustomer (int customerId) throws Exception {
        customerMapper.deleteCustomer(customerId);
    }

    /*删除多条客户信息*/
    public int deleteCustomers (String customerIds) throws Exception {
    	String _customerIds[] = customerIds.split(",");
    	for(String _customerId: _customerIds) {
    		customerMapper.deleteCustomer(Integer.parseInt(_customerId));
    	}
    	return _customerIds.length;
    }
}
