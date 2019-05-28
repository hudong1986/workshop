package workshop.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import workshop.model.AjaxReturn;
import workshop.model.GoodsInOut;
import workshop.model.Pager;
import workshop.model.Product;
import workshop.model.PtUser;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

//系统参数相关
@Controller
@RequestMapping("goods_in_out")
public class GoodInOutController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, 
			@RequestParam(required = false,defaultValue="出库") String direction,
			@RequestParam(required = false, defaultValue = "-1") int product_id,
			@RequestParam(required = false) String batchNo,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize) throws SQLException {
		Pager pager = pagerServer.searchGoodInOut(direction, batchNo, product_id, beg_time, end_time, orderField,
				orderType, pageNum, currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("direction", direction);
		model.addAttribute("batchNo", batchNo);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("product_id", product_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);

		return "goods_in_out/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String list(Model model, @RequestParam(required = false) String direction,
			@RequestParam(required = false, defaultValue = "-1") int product_id,
			@RequestParam(required = false) String batchNo,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time) throws SQLException {
		Pager pager = pagerServer.searchGoodInOut(direction, batchNo, product_id, beg_time, end_time, "", "", 1, 0);
		model.addAttribute("pager", pager);
		model.addAttribute("direction", direction);
		model.addAttribute("batchNo", batchNo);
		model.addAttribute("currentPageSize", 0);
		model.addAttribute("product_id", product_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);

		return null;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn add(
			@RequestParam String add_direction,
			@RequestParam String add_batchno,
			@RequestParam int add_product_id,
			@RequestParam int add_num,
			@RequestParam(defaultValue = "0") BigDecimal add_total,
			@RequestParam String add_out_business_name,
			@RequestParam String add_out_business_address,
			@RequestParam String add_out_business_phone,
			@AuthenticationPrincipal PtUser user) {
		AjaxReturn ret = new AjaxReturn();
		try {
			Product product = productMapper.selectByPrimaryKey(add_product_id);
			GoodsInOut goodsInOut = new GoodsInOut();
			goodsInOut.setAddPhone(user.getPhone());
			goodsInOut.setAddtime(new Date());
			goodsInOut.setAddUserName(user.getRealName());
			goodsInOut.setBatchno(add_batchno);
			goodsInOut.setDeptName(user.getDeptName());
			goodsInOut.setDirection(add_direction);
			goodsInOut.setNum(add_num);
			goodsInOut.setOutBusinessAddress(add_out_business_address);
			goodsInOut.setOutBusinessName(add_out_business_name);
			goodsInOut.setOutBusinessPhone(add_out_business_phone);
			goodsInOut.setProductId(add_product_id);
			goodsInOut.setProductName(product.getProductName());
			goodsInOut.setTotal(add_total);
			goodsInOut.setUpDownId(user.getUpDownId());
			goodsInOutMapper.insert(goodsInOut);
			ret.setCode(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg("添加失败");
		}

		return ret;
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody int del(@RequestParam int id, @AuthenticationPrincipal PtUser user1) {
		goodsInOutMapper.deleteByPrimaryKey(id);
		return 1;
	}
}
