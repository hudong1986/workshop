package workshop.controller;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import workshop.model.AjaxReturn;
import workshop.model.Pager;
import workshop.model.PtUser;
import workshop.model.Reward;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/reward")
public class RewardController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String list(Model model, @RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "") String obj_month,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
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
		Pager pager = pagerServer.searchReward(phone, user_name, up_down_id, obj_month, orderField, orderType, 1,
				currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("obj_month", obj_month);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "all") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "") String obj_month,
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

		Pager pager = pagerServer.searchReward(phone, user_name, up_down_id, obj_month, orderField, orderType, pageNum,
				currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("obj_month", obj_month);
		String retStr = "reward/list";
		return retStr;
	}

	// 添加员工
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String ids, 
			@RequestParam String obj_month,
			@RequestParam String money, 
			@RequestParam String remark, @AuthenticationPrincipal PtUser user) {
		AjaxReturn ret = new AjaxReturn();
		try {
			String[] temp = ids.split(",");
			int id = -1;
			for (String string : temp) {
				id = Integer.parseInt(string);
				if (user.getId() == id && !user.getRoleCode().equals("HrManager")) { // 自己不能为自己添加考勤除非是人事经理
					continue;
				}
				// 添加考勤
				PtUser objUser = ptUserMapper.selectByPrimaryKey(id);
				Reward reward = new Reward();
				reward.setMoney(new BigDecimal(money));
				reward.setObjMonth(obj_month);
				reward.setAddUserName(user.getRealName());
				reward.setAddUserPhone(user.getPhone());
				reward.setPhone(objUser.getPhone());
				reward.setDeptName(objUser.getDeptName());
				reward.setRemark(remark);
				reward.setUpDownId(objUser.getUpDownId());
				reward.setUserName(objUser.getRealName());
				rewardMapper.insert(reward);

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
		rewardMapper.deleteByPrimaryKey(id);
		return 1;
	}

}
