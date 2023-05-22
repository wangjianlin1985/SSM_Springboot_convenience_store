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
import com.chengxusheji.service.BuyInfoService;
import com.chengxusheji.service.StockService;
import com.chengxusheji.po.BuyInfo;
import com.chengxusheji.po.Stock;
import com.chengxusheji.service.ManagerService;
import com.chengxusheji.po.Manager;
import com.chengxusheji.service.ProductService;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.ShopService;
import com.chengxusheji.po.Shop;
import com.chengxusheji.service.SupplierService;
import com.chengxusheji.po.Supplier;

//BuyInfo管理控制层
@Controller
@RequestMapping("/BuyInfo")
public class BuyInfoController extends BaseController {

    /*业务层对象*/
    @Resource BuyInfoService buyInfoService;
    @Resource StockService stockService;
    @Resource ManagerService managerService;
    @Resource ProductService productService;
    @Resource ShopService shopService;
    @Resource SupplierService supplierService;
	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("shopObj")
	public void initBindershopObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shopObj.");
	}
	@InitBinder("supplierObj")
	public void initBindersupplierObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("supplierObj.");
	}
	@InitBinder("managerObj")
	public void initBindermanagerObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("managerObj.");
	}
	@InitBinder("buyInfo")
	public void initBinderBuyInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("buyInfo.");
	}
	/*跳转到添加BuyInfo视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new BuyInfo());
		/*查询所有的Manager信息*/
		List<Manager> managerList = managerService.queryAllManager();
		request.setAttribute("managerList", managerList);
		/*查询所有的Product信息*/
		List<Product> productList = productService.queryAllProduct();
		request.setAttribute("productList", productList);
		/*查询所有的Shop信息*/
		List<Shop> shopList = shopService.queryAllShop();
		request.setAttribute("shopList", shopList);
		/*查询所有的Supplier信息*/
		List<Supplier> supplierList = supplierService.queryAllSupplier();
		request.setAttribute("supplierList", supplierList);
		return "BuyInfo_add";
	}
	 
	/*客户端ajax方式提交添加商品进货信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated BuyInfo buyInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        buyInfoService.addBuyInfo(buyInfo);
        message = "商品进货添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式提交添加商品进货信息*/
	@RequestMapping(value = "/managerAdd", method = RequestMethod.POST)
	public void managerAdd(@Validated BuyInfo buyInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		
		String managerUserName = (String)session.getAttribute("manager");
		Manager manager = managerService.getManager(managerUserName);
		Shop shop = manager.getShopObj();
		ArrayList<Stock> stockList = stockService.queryStock(buyInfo.getProductObj(), shop);
		if(stockList.size() > 0) {
			Stock stock = stockList.get(0);
			stock.setLeftCount(stock.getLeftCount() + buyInfo.getBuyCount());
			stockService.updateStock(stock);
		} else {
			Stock stock = new Stock();
			stock.setProductObj(buyInfo.getProductObj());
			stock.setShopObj(shop);
			stock.setLeftCount(buyInfo.getBuyCount());
			stock.setMemo("");
			stockService.addStock(stock); 
		}
		
		 
        buyInfoService.addBuyInfo(buyInfo);
        message = "商品进货添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*ajax方式按照查询条件分页查询商品进货信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("supplierObj") Supplier supplierObj,String buyDate,@ModelAttribute("managerObj") Manager managerObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (buyDate == null) buyDate = "";
		if(rows != 0)buyInfoService.setRows(rows);
		List<BuyInfo> buyInfoList = buyInfoService.queryBuyInfo(productObj, shopObj, supplierObj, buyDate, managerObj, page);
	    /*计算总的页数和总的记录数*/
	    buyInfoService.queryTotalPageAndRecordNumber(productObj, shopObj, supplierObj, buyDate, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = buyInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = buyInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(BuyInfo buyInfo:buyInfoList) {
			JSONObject jsonBuyInfo = buyInfo.getJsonObject();
			jsonArray.put(jsonBuyInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}
	
	
	/*ajax方式按照查询条件分页查询商品进货信息*/
	@RequestMapping(value = { "/managerList" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void managerList(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("supplierObj") Supplier supplierObj,String buyDate,@ModelAttribute("managerObj") Manager managerObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		if (page==null || page == 0) page = 1;
		if (buyDate == null) buyDate = "";
		if(rows != 0)buyInfoService.setRows(rows);
		String managerUserName = (String) session.getAttribute("manager");
		managerObj = managerService.getManager(managerUserName);
		List<BuyInfo> buyInfoList = buyInfoService.queryBuyInfo(productObj, shopObj, supplierObj, buyDate, managerObj, page);
	    /*计算总的页数和总的记录数*/
	    buyInfoService.queryTotalPageAndRecordNumber(productObj, shopObj, supplierObj, buyDate, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = buyInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = buyInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(BuyInfo buyInfo:buyInfoList) {
			JSONObject jsonBuyInfo = buyInfo.getJsonObject();
			jsonArray.put(jsonBuyInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}
	
	

	/*ajax方式按照查询条件分页查询商品进货信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<BuyInfo> buyInfoList = buyInfoService.queryAllBuyInfo();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(BuyInfo buyInfo:buyInfoList) {
			JSONObject jsonBuyInfo = new JSONObject();
			jsonBuyInfo.accumulate("buyId", buyInfo.getBuyId());
			jsonArray.put(jsonBuyInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询商品进货信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("supplierObj") Supplier supplierObj,String buyDate,@ModelAttribute("managerObj") Manager managerObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (buyDate == null) buyDate = "";
		List<BuyInfo> buyInfoList = buyInfoService.queryBuyInfo(productObj, shopObj, supplierObj, buyDate, managerObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    buyInfoService.queryTotalPageAndRecordNumber(productObj, shopObj, supplierObj, buyDate, managerObj);
	    /*获取到总的页码数目*/
	    int totalPage = buyInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = buyInfoService.getRecordNumber();
	    request.setAttribute("buyInfoList",  buyInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productObj", productObj);
	    request.setAttribute("shopObj", shopObj);
	    request.setAttribute("supplierObj", supplierObj);
	    request.setAttribute("buyDate", buyDate);
	    request.setAttribute("managerObj", managerObj);
	    List<Manager> managerList = managerService.queryAllManager();
	    request.setAttribute("managerList", managerList);
	    List<Product> productList = productService.queryAllProduct();
	    request.setAttribute("productList", productList);
	    List<Shop> shopList = shopService.queryAllShop();
	    request.setAttribute("shopList", shopList);
	    List<Supplier> supplierList = supplierService.queryAllSupplier();
	    request.setAttribute("supplierList", supplierList);
		return "BuyInfo/buyInfo_frontquery_result"; 
	}

     /*前台查询BuyInfo信息*/
	@RequestMapping(value="/{buyId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer buyId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键buyId获取BuyInfo对象*/
        BuyInfo buyInfo = buyInfoService.getBuyInfo(buyId);

        List<Manager> managerList = managerService.queryAllManager();
        request.setAttribute("managerList", managerList);
        List<Product> productList = productService.queryAllProduct();
        request.setAttribute("productList", productList);
        List<Shop> shopList = shopService.queryAllShop();
        request.setAttribute("shopList", shopList);
        List<Supplier> supplierList = supplierService.queryAllSupplier();
        request.setAttribute("supplierList", supplierList);
        request.setAttribute("buyInfo",  buyInfo);
        return "BuyInfo/buyInfo_frontshow";
	}

	/*ajax方式显示商品进货修改jsp视图页*/
	@RequestMapping(value="/{buyId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer buyId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键buyId获取BuyInfo对象*/
        BuyInfo buyInfo = buyInfoService.getBuyInfo(buyId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonBuyInfo = buyInfo.getJsonObject();
		out.println(jsonBuyInfo.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新商品进货信息*/
	@RequestMapping(value = "/{buyId}/update", method = RequestMethod.POST)
	public void update(@Validated BuyInfo buyInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			buyInfoService.updateBuyInfo(buyInfo);
			message = "商品进货更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "商品进货更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除商品进货信息*/
	@RequestMapping(value="/{buyId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer buyId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  buyInfoService.deleteBuyInfo(buyId);
	            request.setAttribute("message", "商品进货删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "商品进货删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条商品进货记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String buyIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = buyInfoService.deleteBuyInfos(buyIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出商品进货信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj") Shop shopObj,@ModelAttribute("supplierObj") Supplier supplierObj,String buyDate,@ModelAttribute("managerObj") Manager managerObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(buyDate == null) buyDate = "";
        List<BuyInfo> buyInfoList = buyInfoService.queryBuyInfo(productObj,shopObj,supplierObj,buyDate,managerObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "BuyInfo信息记录"; 
        String[] headers = { "进货id","进货商品","进货店铺","供应商","进货单价","进货数量","进货日期","进货管理员"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<buyInfoList.size();i++) {
        	BuyInfo buyInfo = buyInfoList.get(i); 
        	dataset.add(new String[]{buyInfo.getBuyId() + "",buyInfo.getProductObj().getProductName(),buyInfo.getShopObj().getShopName(),buyInfo.getSupplierObj().getSupplierName(),buyInfo.getBuyPrice() + "",buyInfo.getBuyCount() + "",buyInfo.getBuyDate(),buyInfo.getManagerObj().getName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"BuyInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
