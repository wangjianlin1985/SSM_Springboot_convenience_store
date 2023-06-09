package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.CustomerService;
import com.chengxusheji.po.Customer;

//Customer管理控制层
@Controller
@RequestMapping("/Customer")
public class CustomerController extends BaseController {

    /*业务层对象*/
    @Resource CustomerService customerService;

	@InitBinder("customer")
	public void initBinderCustomer(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("customer.");
	}
	/*跳转到添加Customer视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Customer());
		return "Customer_add";
	}

	/*客户端ajax方式提交添加客户信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Customer customer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        customerService.addCustomer(customer);
        message = "客户添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询客户信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String name,String birthDate,String telephone,String regTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		if (regTime == null) regTime = "";
		if(rows != 0)customerService.setRows(rows);
		List<Customer> customerList = customerService.queryCustomer(name, birthDate, telephone, regTime, page);
	    /*计算总的页数和总的记录数*/
	    customerService.queryTotalPageAndRecordNumber(name, birthDate, telephone, regTime);
	    /*获取到总的页码数目*/
	    int totalPage = customerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = customerService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Customer customer:customerList) {
			JSONObject jsonCustomer = customer.getJsonObject();
			jsonArray.put(jsonCustomer);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询客户信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Customer> customerList = customerService.queryAllCustomer();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Customer customer:customerList) {
			JSONObject jsonCustomer = new JSONObject();
			jsonCustomer.accumulate("customerId", customer.getCustomerId());
			jsonCustomer.accumulate("name", customer.getName());
			jsonArray.put(jsonCustomer);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询客户信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String name,String birthDate,String telephone,String regTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		if (regTime == null) regTime = "";
		List<Customer> customerList = customerService.queryCustomer(name, birthDate, telephone, regTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    customerService.queryTotalPageAndRecordNumber(name, birthDate, telephone, regTime);
	    /*获取到总的页码数目*/
	    int totalPage = customerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = customerService.getRecordNumber();
	    request.setAttribute("customerList",  customerList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("name", name);
	    request.setAttribute("birthDate", birthDate);
	    request.setAttribute("telephone", telephone);
	    request.setAttribute("regTime", regTime);
		return "Customer/customer_frontquery_result"; 
	}

     /*前台查询Customer信息*/
	@RequestMapping(value="/{customerId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer customerId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键customerId获取Customer对象*/
        Customer customer = customerService.getCustomer(customerId);

        request.setAttribute("customer",  customer);
        return "Customer/customer_frontshow";
	}

	/*ajax方式显示客户修改jsp视图页*/
	@RequestMapping(value="/{customerId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer customerId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键customerId获取Customer对象*/
        Customer customer = customerService.getCustomer(customerId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonCustomer = customer.getJsonObject();
		out.println(jsonCustomer.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新客户信息*/
	@RequestMapping(value = "/{customerId}/update", method = RequestMethod.POST)
	public void update(@Validated Customer customer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			customerService.updateCustomer(customer);
			message = "客户更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "客户更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除客户信息*/
	@RequestMapping(value="/{customerId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer customerId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  customerService.deleteCustomer(customerId);
	            request.setAttribute("message", "客户删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "客户删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条客户记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String customerIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = customerService.deleteCustomers(customerIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出客户信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String name,String birthDate,String telephone,String regTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(name == null) name = "";
        if(birthDate == null) birthDate = "";
        if(telephone == null) telephone = "";
        if(regTime == null) regTime = "";
        List<Customer> customerList = customerService.queryCustomer(name,birthDate,telephone,regTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Customer信息记录"; 
        String[] headers = { "客户id","姓名","性别","出生日期","联系电话","加入时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<customerList.size();i++) {
        	Customer customer = customerList.get(i); 
        	dataset.add(new String[]{customer.getCustomerId() + "",customer.getName(),customer.getGender(),customer.getBirthDate(),customer.getTelephone(),customer.getRegTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Customer.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
