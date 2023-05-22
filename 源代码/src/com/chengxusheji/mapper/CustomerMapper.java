package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Customer;

public interface CustomerMapper {
	/*添加客户信息*/
	public void addCustomer(Customer customer) throws Exception;

	/*按照查询条件分页查询客户记录*/
	public ArrayList<Customer> queryCustomer(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有客户记录*/
	public ArrayList<Customer> queryCustomerList(@Param("where") String where) throws Exception;

	/*按照查询条件的客户记录数*/
	public int queryCustomerCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条客户记录*/
	public Customer getCustomer(int customerId) throws Exception;

	/*更新客户记录*/
	public void updateCustomer(Customer customer) throws Exception;

	/*删除客户记录*/
	public void deleteCustomer(int customerId) throws Exception;

}
