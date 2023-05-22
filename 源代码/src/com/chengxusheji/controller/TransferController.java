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
import com.chengxusheji.service.StockService;
import com.chengxusheji.service.TransferService;
import com.chengxusheji.po.Stock;
import com.chengxusheji.po.Transfer;
import com.chengxusheji.service.ProductService;
import com.chengxusheji.po.Product;
import com.chengxusheji.service.ShopService;
import com.chengxusheji.po.Shop;

//Transfer管理控制层
@Controller
@RequestMapping("/Transfer")
public class TransferController extends BaseController {

    /*业务层对象*/
    @Resource TransferService transferService;
    @Resource StockService stockService;
    @Resource ProductService productService;
    @Resource ShopService shopService;
	@InitBinder("productObj")
	public void initBinderproductObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("productObj.");
	}
	@InitBinder("shopObj1")
	public void initBindershopObj1(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shopObj1.");
	}
	@InitBinder("shopObj2")
	public void initBindershopObj2(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("shopObj2.");
	}
	@InitBinder("transfer")
	public void initBinderTransfer(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("transfer.");
	}
	/*跳转到添加Transfer视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Transfer());
		/*查询所有的Product信息*/
		List<Product> productList = productService.queryAllProduct();
		request.setAttribute("productList", productList);
		/*查询所有的Shop信息*/
		List<Shop> shopList = shopService.queryAllShop();
		request.setAttribute("shopList", shopList);
		return "Transfer_add";
	}

	/*客户端ajax方式提交添加商品调拨信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Transfer transfer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;  
		Shop shop1 = transfer.getShopObj1();
		Shop shop2 = transfer.getShopObj2();
		if(shop1.getShopId().intValue() == shop2.getShopId().intValue()) {
			message = "商品调拨需要在2个店铺之间进行！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		Product product = transfer.getProductObj();
		
		ArrayList<Stock> stockList = stockService.queryStock(product, shop1);
		if(stockList.size() == 0) {
			message = "调出店铺该商品不存在！";
			writeJsonResponse(response, success, message);
			return ;
		} else {
			Stock stock = stockList.get(0);
			if(stock.getLeftCount() < transfer.getTransferCount()) {
				message = "调拨数量超过了调出店铺的该商品库存量！";
				writeJsonResponse(response, success, message);
				return ;
			}
			
			stock.setLeftCount(stock.getLeftCount() - transfer.getTransferCount());
			stockService.updateStock(stock); //更新调出店铺的商品库存
			
			stockList = stockService.queryStock(product, shop2);
			if(stockList.size() == 0) {
				stock = new Stock();
				stock.setProductObj(product);
				stock.setShopObj(shop2);
				stock.setLeftCount(transfer.getTransferCount());
				stock.setMemo("");
				stockService.addStock(stock); //如果调入店铺该商品没货就建立一条库存信息
			} else {
				stock = stockList.get(0);
				stock.setLeftCount(stock.getLeftCount() + transfer.getTransferCount());
				stockService.updateStock(stock); //如果调入店铺商品有库存记录就更新数量即可
				
			}
			
		}
		
        transferService.addTransfer(transfer);
        message = "商品调拨添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*ajax方式按照查询条件分页查询商品调拨信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj1") Shop shopObj1,@ModelAttribute("shopObj2") Shop shopObj2,String transferTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (transferTime == null) transferTime = "";
		if(rows != 0)transferService.setRows(rows);
		List<Transfer> transferList = transferService.queryTransfer(productObj, shopObj1, shopObj2, transferTime, page);
	    /*计算总的页数和总的记录数*/
	    transferService.queryTotalPageAndRecordNumber(productObj, shopObj1, shopObj2, transferTime);
	    /*获取到总的页码数目*/
	    int totalPage = transferService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = transferService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Transfer transfer:transferList) {
			JSONObject jsonTransfer = transfer.getJsonObject();
			jsonArray.put(jsonTransfer);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询商品调拨信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Transfer> transferList = transferService.queryAllTransfer();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Transfer transfer:transferList) {
			JSONObject jsonTransfer = new JSONObject();
			jsonTransfer.accumulate("transferId", transfer.getTransferId());
			jsonArray.put(jsonTransfer);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询商品调拨信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj1") Shop shopObj1,@ModelAttribute("shopObj2") Shop shopObj2,String transferTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (transferTime == null) transferTime = "";
		List<Transfer> transferList = transferService.queryTransfer(productObj, shopObj1, shopObj2, transferTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    transferService.queryTotalPageAndRecordNumber(productObj, shopObj1, shopObj2, transferTime);
	    /*获取到总的页码数目*/
	    int totalPage = transferService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = transferService.getRecordNumber();
	    request.setAttribute("transferList",  transferList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("productObj", productObj);
	    request.setAttribute("shopObj1", shopObj1);
	    request.setAttribute("shopObj2", shopObj2);
	    request.setAttribute("transferTime", transferTime);
	    List<Product> productList = productService.queryAllProduct();
	    request.setAttribute("productList", productList);
	    List<Shop> shopList = shopService.queryAllShop();
	    request.setAttribute("shopList", shopList);
		return "Transfer/transfer_frontquery_result"; 
	}

     /*前台查询Transfer信息*/
	@RequestMapping(value="/{transferId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer transferId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键transferId获取Transfer对象*/
        Transfer transfer = transferService.getTransfer(transferId);

        List<Product> productList = productService.queryAllProduct();
        request.setAttribute("productList", productList);
        List<Shop> shopList = shopService.queryAllShop();
        request.setAttribute("shopList", shopList);
        request.setAttribute("transfer",  transfer);
        return "Transfer/transfer_frontshow";
	}

	/*ajax方式显示商品调拨修改jsp视图页*/
	@RequestMapping(value="/{transferId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer transferId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键transferId获取Transfer对象*/
        Transfer transfer = transferService.getTransfer(transferId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonTransfer = transfer.getJsonObject();
		out.println(jsonTransfer.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新商品调拨信息*/
	@RequestMapping(value = "/{transferId}/update", method = RequestMethod.POST)
	public void update(@Validated Transfer transfer, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			transferService.updateTransfer(transfer);
			message = "商品调拨更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "商品调拨更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除商品调拨信息*/
	@RequestMapping(value="/{transferId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer transferId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  transferService.deleteTransfer(transferId);
	            request.setAttribute("message", "商品调拨删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "商品调拨删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条商品调拨记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String transferIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = transferService.deleteTransfers(transferIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出商品调拨信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("productObj") Product productObj,@ModelAttribute("shopObj1") Shop shopObj1,@ModelAttribute("shopObj2") Shop shopObj2,String transferTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(transferTime == null) transferTime = "";
        List<Transfer> transferList = transferService.queryTransfer(productObj,shopObj1,shopObj2,transferTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Transfer信息记录"; 
        String[] headers = { "调拨id","调拨商品","调拨数量","调出店铺","调入店铺","调拨时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<transferList.size();i++) {
        	Transfer transfer = transferList.get(i); 
        	dataset.add(new String[]{transfer.getTransferId() + "",transfer.getProductObj().getProductName(),transfer.getTransferCount() + "",transfer.getShopObj1().getShopName(),transfer.getShopObj2().getShopName(),transfer.getTransferTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Transfer.xls");//filename是下载的xls的名，建议最好用英文 
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
