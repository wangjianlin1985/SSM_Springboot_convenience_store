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
import com.chengxusheji.service.ManagerService;
import com.chengxusheji.service.StockService;
import com.chengxusheji.po.Stock;
import com.chengxusheji.service.ProductService;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.ShopService;
import com.chengxusheji.po.Shop;

//Stock管理控制层
@Controller
@RequestMapping("/Stock")
public class StockController extends BaseController {

    /*业务层对象*/
    @Resource StockService stockService;
    @Resource ProductService productService;
    @Resource ShopService shopService;
    @Resource ManagerService managerService;
	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("shopObj")
	public void initBindershopObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shopObj.");
	}
	@InitBinder("stock")
	public void initBinderStock(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("stock.");
	}
	/*跳转到添加Stock视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Stock());
		/*查询所有的Product信息*/
		List<Product> productList = productService.queryAllProduct();
		request.setAttribute("productList", productList);
		/*查询所有的Shop信息*/
		List<Shop> shopList = shopService.queryAllShop();
		request.setAttribute("shopList", shopList);
		return "Stock_add";
	}

	/*客户端ajax方式提交添加商品库存信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Stock stock, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        stockService.addStock(stock);
        message = "商品库存添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	/*ajax方式按照查询条件分页查询商品库存信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)stockService.setRows(rows);
		List<Stock> stockList = stockService.queryStock(productObj, shopObj, page);
	    /*计算总的页数和总的记录数*/
	    stockService.queryTotalPageAndRecordNumber(productObj, shopObj);
	    /*获取到总的页码数目*/
	    int totalPage = stockService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = stockService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Stock stock:stockList) {
			JSONObject jsonStock = stock.getJsonObject();
			jsonArray.put(jsonStock);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}
	
	
	
	/*ajax方式按照查询条件分页查询商品库存信息*/
	@RequestMapping(value = { "/managerList" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void managerList(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)stockService.setRows(rows);
		String managerUserName = (String)session.getAttribute("manager");
		shopObj = managerService.getManager(managerUserName).getShopObj();
		List<Stock> stockList = stockService.queryStock(productObj, shopObj, page);
	    /*计算总的页数和总的记录数*/
	    stockService.queryTotalPageAndRecordNumber(productObj, shopObj);
	    /*获取到总的页码数目*/
	    int totalPage = stockService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = stockService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Stock stock:stockList) {
			JSONObject jsonStock = stock.getJsonObject();
			jsonArray.put(jsonStock);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}
	
	

	/*ajax方式按照查询条件分页查询商品库存信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Stock> stockList = stockService.queryAllStock();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Stock stock:stockList) {
			JSONObject jsonStock = new JSONObject();
			jsonStock.accumulate("stockId", stock.getStockId());
			jsonArray.put(jsonStock);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询商品库存信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<Stock> stockList = stockService.queryStock(productObj, shopObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    stockService.queryTotalPageAndRecordNumber(productObj, shopObj);
	    /*获取到总的页码数目*/
	    int totalPage = stockService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = stockService.getRecordNumber();
	    request.setAttribute("stockList",  stockList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productObj", productObj);
	    request.setAttribute("shopObj", shopObj);
	    List<Product> productList = productService.queryAllProduct();
	    request.setAttribute("productList", productList);
	    List<Shop> shopList = shopService.queryAllShop();
	    request.setAttribute("shopList", shopList);
		return "Stock/stock_frontquery_result"; 
	}

     /*前台查询Stock信息*/
	@RequestMapping(value="/{stockId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer stockId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键stockId获取Stock对象*/
        Stock stock = stockService.getStock(stockId);

        List<Product> productList = productService.queryAllProduct();
        request.setAttribute("productList", productList);
        List<Shop> shopList = shopService.queryAllShop();
        request.setAttribute("shopList", shopList);
        request.setAttribute("stock",  stock);
        return "Stock/stock_frontshow";
	}

	/*ajax方式显示商品库存修改jsp视图页*/
	@RequestMapping(value="/{stockId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer stockId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键stockId获取Stock对象*/
        Stock stock = stockService.getStock(stockId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonStock = stock.getJsonObject();
		out.println(jsonStock.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新商品库存信息*/
	@RequestMapping(value = "/{stockId}/update", method = RequestMethod.POST)
	public void update(@Validated Stock stock, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			stockService.updateStock(stock);
			message = "商品库存更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "商品库存更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除商品库存信息*/
	@RequestMapping(value="/{stockId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer stockId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  stockService.deleteStock(stockId);
	            request.setAttribute("message", "商品库存删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "商品库存删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条商品库存记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String stockIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = stockService.deleteStocks(stockIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出商品库存信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<Stock> stockList = stockService.queryStock(productObj,shopObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Stock信息记录"; 
        String[] headers = { "库存id","商品名称","所属店铺","剩余库存量"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<stockList.size();i++) {
        	Stock stock = stockList.get(i); 
        	dataset.add(new String[]{stock.getStockId() + "",stock.getProductObj().getProductName(),stock.getShopObj().getShopName(),stock.getLeftCount() + ""});
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
			response.setHeader("Content-disposition","attachment; filename="+"Stock.xls");//filename是下载的xls的名，建议最好用英文 
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
