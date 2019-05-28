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
import workshop.model.FinishStatistic;
import workshop.model.Pager;
import workshop.model.Product;
import workshop.model.PtUser;
import workshop.model.Reward;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/finish_statistic")
public class FinishStaticController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String list(Model model,
			@RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "-1") int product_id,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@AuthenticationPrincipal PtUser user1

	) throws SQLException {
		if (user1.getRoleCode().equals("ADMIN") || user1.getRoleCode().equals("HrManager")
				|| user1.getRoleCode().equals("HrAssistant")) {

		} else {

			up_down_id = user1.getUpDownId();

		}

		// 非管理只能看到自己的
		if (user1.getRoleCode().equals("WorkshopEmployee") || user1.getRoleCode().equals("FinanceAssistant")) {
			phone = user1.getPhone();
		}
		
		Pager pager = pagerServer.searchFinishStastic(phone, user_name, up_down_id, product_id, beg_time, end_time, orderField, orderType, 1, 0);
		
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("product_id", product_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "all") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "-1") int product_id,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
			@AuthenticationPrincipal PtUser user1) throws SQLException {

		if (user1.getRoleCode().equals("ADMIN") || user1.getRoleCode().equals("HrManager")
				|| user1.getRoleCode().equals("HrAssistant")) {

		} else {
			if (up_down_id.equals("all")) {
				up_down_id = user1.getUpDownId();
			}
		}

		// 非管理只能看到自己的
		if (user1.getRoleCode().equals("WorkshopEmployee") || user1.getRoleCode().equals("FinanceAssistant")) {
			phone = user1.getPhone();
		}

		Pager pager = pagerServer.searchFinishStastic(phone, user_name, up_down_id, product_id, beg_time, end_time, orderField, orderType, pageNum, 0);
		
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("product_id", product_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		String retStr = "finish_statistic/list";
		return retStr;
	}

	// 添加员工记件
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String ids, 
			@RequestParam int product_id,
			@RequestParam int num, 
			@AuthenticationPrincipal PtUser user) {
		AjaxReturn ret = new AjaxReturn();
		List<Product> products = productMapper.selectAll();
		try {
			String[] temp = ids.split(",");
			int id = -1;
			for (String string : temp) {
				id = Integer.parseInt(string);
				if (user.getId() == id && !user.getRoleCode().equals("HrManager")) { // 自己不能为自己添加考勤除非是人事经理
					continue;
				}
		
				PtUser objUser = ptUserMapper.selectByPrimaryKey(id);
				FinishStatistic record = new FinishStatistic();
				record.setAddUserId(user.getId());
				record.setAddUserName(user.getRealName());
				record.setPhone(objUser.getPhone());
				record.setDeptName(objUser.getDeptName());
				record.setUpDownId(objUser.getUpDownId());
				record.setUserName(objUser.getRealName());
				record.setProductId(product_id);
				record.setNum(num);
				record.setAddtime(new Date());
				Product product = products.stream().filter(x->x.getId()==product_id).findFirst().get();
				record.setProductName(product.getProductName());
				record.setGetMoney( new BigDecimal(product.getOutIn().doubleValue()*num));
				finishStatisticMapper.insert(record);
			}

			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg(ex.getMessage());
		}

		return ret;
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody int del(@RequestParam int id, @AuthenticationPrincipal PtUser user1) {
		finishStatisticMapper.deleteByPrimaryKey(id);
		return 1;
	}

}
