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
import com.chengxusheji.service.SellService;
import com.chengxusheji.service.StockService;
import com.chengxusheji.po.Sell;
import com.chengxusheji.po.Stock;
import com.chengxusheji.service.CustomerService;
import com.chengxusheji.po.Customer;
import com.chengxusheji.service.ManagerService;
import com.chengxusheji.po.Manager;
import com.chengxusheji.service.ProductService;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.ShopService;
import com.chengxusheji.po.Shop;

//Sell管理控制层
@Controller
@RequestMapping("/Sell")
public class SellController extends BaseController {

    /*业务层对象*/
    @Resource SellService sellService;
    @Resource StockService stockService;
    @Resource CustomerService customerService;
    @Resource ManagerService managerService;
    @Resource ProductService productService;
    @Resource ShopService shopService;
	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("shopObj")
	public void initBindershopObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shopObj.");
	}
	@InitBinder("customerObj")
	public void initBindercustomerObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("customerObj.");
	}
	@InitBinder("managerObj")
	public void initBindermanagerObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("managerObj.");
	}
	@InitBinder("sell")
	public void initBinderSell(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("sell.");
	}
	/*跳转到添加Sell视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Sell());
		/*查询所有的Customer信息*/
		List<Customer> customerList = customerService.queryAllCustomer();
		request.setAttribute("customerList", customerList);
		/*查询所有的Manager信息*/
		List<Manager> managerList = managerService.queryAllManager();
		request.setAttribute("managerList", managerList);
		/*查询所有的Product信息*/
		List<Product> productList = productService.queryAllProduct();
		request.setAttribute("productList", productList);
		/*查询所有的Shop信息*/
		List<Shop> shopList = shopService.queryAllShop();
		request.setAttribute("shopList", shopList);
		return "Sell_add";
	}

	/*客户端ajax方式提交添加商品销售信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Sell sell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        sellService.addSell(sell);
        message = "商品销售添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	/*客户端ajax方式提交添加商品销售信息*/
	@RequestMapping(value = "/managerAdd", method = RequestMethod.POST)
	public void managerAdd(@Validated Sell sell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		String managerUserName = (String) session.getAttribute("manager");
		Manager manager = managerService.getManager(managerUserName);
		Shop shop = manager.getShopObj();
		ArrayList<Stock> stockList = stockService.queryStock(sell.getProductObj(), shop);
		if(stockList.size() == 0) {
			message = "本店铺目前该商品没货，请先进货！";
			writeJsonResponse(response, success, message);
			return ;
		} else {
			Stock stock = stockList.get(0);
			if(stock.getLeftCount() < sell.getSellCount()) {
				message = "商品库存不足,无法销售";
				writeJsonResponse(response, success, message);
				return ;
			}
			stock.setLeftCount(stock.getLeftCount() - sell.getSellCount());
			stockService.updateStock(stock);
		}
		 
        sellService.addSell(sell);
        message = "商品销售添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询商品销售信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("customerObj") Customer customerObj,String sellTime,@ModelAttribute("managerObj") Manager managerObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (sellTime == null) sellTime = "";
		if(rows != 0)sellService.setRows(rows);
		List<Sell> sellList = sellService.querySell(productObj, shopObj, customerObj, sellTime, managerObj, page);
	    /*计算总的页数和总的记录数*/
	    sellService.queryTotalPageAndRecordNumber(productObj, shopObj, customerObj, sellTime, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = sellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = sellService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Sell sell:sellList) {
			JSONObject jsonSell = sell.getJsonObject();
			jsonArray.put(jsonSell);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	
	
	/*ajax方式按照查询条件分页查询商品销售信息*/
	@RequestMapping(value = { "/managerList" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void managerList(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("customerObj") Customer customerObj,String sellTime,@ModelAttribute("managerObj") Manager managerObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		if (page==null || page == 0) page = 1;
		if (sellTime == null) sellTime = "";
		if(rows != 0)sellService.setRows(rows);
		String managerUserName = (String)session.getAttribute("manager");
		managerObj = managerService.getManager(managerUserName);
		
		List<Sell> sellList = sellService.querySell(productObj, shopObj, customerObj, sellTime, managerObj, page);
	    /*计算总的页数和总的记录数*/
	    sellService.queryTotalPageAndRecordNumber(productObj, shopObj, customerObj, sellTime, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = sellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = sellService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Sell sell:sellList) {
			JSONObject jsonSell = sell.getJsonObject();
			jsonArray.put(jsonSell);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}
	
	
	
	/*ajax方式按照查询条件分页查询商品销售信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Sell> sellList = sellService.queryAllSell();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Sell sell:sellList) {
			JSONObject jsonSell = new JSONObject();
			jsonSell.accumulate("sellId", sell.getSellId());
			jsonArray.put(jsonSell);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询商品销售信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("customerObj") Customer customerObj,String sellTime,@ModelAttribute("managerObj") Manager managerObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (sellTime == null) sellTime = "";
		List<Sell> sellList = sellService.querySell(productObj, shopObj, customerObj, sellTime, managerObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    sellService.queryTotalPageAndRecordNumber(productObj, shopObj, customerObj, sellTime, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = sellService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = sellService.getRecordNumber();
	    request.setAttribute("sellList",  sellList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productObj", productObj);
	    request.setAttribute("shopObj", shopObj);
	    request.setAttribute("customerObj", customerObj);
	    request.setAttribute("sellTime", sellTime);
	    request.setAttribute("managerObj", managerObj);
	    List<Customer> customerList = customerService.queryAllCustomer();
	    request.setAttribute("customerList", customerList);
	    List<Manager> managerList = managerService.queryAllManager();
	    request.setAttribute("managerList", managerList);
	    List<Product> productList = productService.queryAllProduct();
	    request.setAttribute("productList", productList);
	    List<Shop> shopList = shopService.queryAllShop();
	    request.setAttribute("shopList", shopList);
		return "Sell/sell_frontquery_result"; 
	}

     /*前台查询Sell信息*/
	@RequestMapping(value="/{sellId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer sellId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键sellId获取Sell对象*/
        Sell sell = sellService.getSell(sellId);

        List<Customer> customerList = customerService.queryAllCustomer();
        request.setAttribute("customerList", customerList);
        List<Manager> managerList = managerService.queryAllManager();
        request.setAttribute("managerList", managerList);
        List<Product> productList = productService.queryAllProduct();
        request.setAttribute("productList", productList);
        List<Shop> shopList = shopService.queryAllShop();
        request.setAttribute("shopList", shopList);
        request.setAttribute("sell",  sell);
        return "Sell/sell_frontshow";
	}

	/*ajax方式显示商品销售修改jsp视图页*/
	@RequestMapping(value="/{sellId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer sellId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键sellId获取Sell对象*/
        Sell sell = sellService.getSell(sellId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonSell = sell.getJsonObject();
		out.println(jsonSell.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新商品销售信息*/
	@RequestMapping(value = "/{sellId}/update", method = RequestMethod.POST)
	public void update(@Validated Sell sell, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			sellService.updateSell(sell);
			message = "商品销售更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "商品销售更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除商品销售信息*/
	@RequestMapping(value="/{sellId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer sellId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  sellService.deleteSell(sellId);
	            request.setAttribute("message", "商品销售删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "商品销售删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条商品销售记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String sellIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = sellService.deleteSells(sellIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出商品销售信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("customerObj") Customer customerObj,String sellTime,@ModelAttribute("managerObj") Manager managerObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(sellTime == null) sellTime = "";
        List<Sell> sellList = sellService.querySell(productObj,shopObj,customerObj,sellTime,managerObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Sell信息记录"; 
        String[] headers = { "销售id","销售商品","销售店铺","销售价格","销售数量","销售客户","销售时间","操作管理员"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<sellList.size();i++) {
        	Sell sell = sellList.get(i); 
        	dataset.add(new String[]{sell.getSellId() + "",sell.getProductObj().getProductName(),sell.getShopObj().getShopName(),sell.getSellPrice() + "",sell.getSellCount() + "",sell.getCustomerObj().getName(),sell.getSellTime(),sell.getManagerObj().getName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Sell.xls");//filename是下载的xls的名，建议最好用英文 
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
