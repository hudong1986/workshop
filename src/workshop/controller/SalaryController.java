package workshop.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.property.IndexPropertyAccessor.IndexGetter;
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
import workshop.model.Salary;
import workshop.util.DateUtil;
import workshop.util.ExportExcelUtils;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/salary")
public class SalaryController extends BaseController {

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
		Pager pager = pagerServer.searchSalary(phone, user_name, up_down_id, obj_month, orderField, orderType, 1,
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
		if (StringUtils.isBlank(obj_month)) {
			obj_month = DateUtil.format(DateUtil.addMonth(new Date(), -1), "yyyy-MM");
		}

		Pager pager = pagerServer.searchSalary(phone, user_name, up_down_id, obj_month, orderField, orderType, pageNum,
				currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("obj_month", obj_month);
		String retStr = "salary/list";
		return retStr;
	}
	
	// 分页功能
		@RequestMapping(value = "/export", method = RequestMethod.GET)
		public String export(Model model, @RequestParam(required = false, defaultValue = "") String phone,
				@RequestParam(required = false, defaultValue = "") String user_name,
				@RequestParam(required = false, defaultValue = "all") String up_down_id,
				@RequestParam(required = false, defaultValue = "") String orderField,
				@RequestParam(required = false, defaultValue = "") String orderType,
				@RequestParam(required = false, defaultValue = "") String obj_month,
				@RequestParam(required = false, defaultValue = "1") int pageNum,
				@RequestParam(required = false, defaultValue = "0") int currentPageSize,
				@AuthenticationPrincipal PtUser user1,
				HttpServletResponse response) throws SQLException {

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
			if (StringUtils.isBlank(obj_month)) {
				obj_month = DateUtil.format(DateUtil.addMonth(new Date(), -1), "yyyy-MM");
			}

			Pager pager = pagerServer.searchSalary(phone, user_name, up_down_id, obj_month, orderField, orderType, pageNum,
					1000);
			String[] rowsName=new String[]{"ID","日期","部门","手机","姓名","基本薪水(元)","基本薪水说明","记件提成(元)"
					,"记件详情","加班工资(元)","加班详情","奖励(元)","奖励详情","事假扣钱(元)","事假详情","福利(元)","福利详情","其它加薪资(元)",
					"其它加薪资详情","其它扣薪资(元)","其它扣薪资详情","总额(元)"};  
			if(pager.getTotalCount()==0){
				return null;
			}
			
			List<Salary> salaries = (List<Salary>)pager.getData();
			
			List<Object[]> listObj = new ArrayList<>();
			Object[] objs = null;  
	        for (int i = 0; i < salaries.size(); i++) {  
	        	Salary po = salaries.get(i);  
	            objs = new Object[rowsName.length];  
	            objs[1] = po.getMonth(); 
	            objs[2] = po.getDeptName(); 
	            objs[3] = po.getPhone();  
	            objs[4] = po.getUserName();
	            objs[5] = po.getFixMoney();
	            objs[6] = po.getFixMoneyDes();
	            objs[7] = po.getAchievement();
	            objs[8] = po.getAchievementDes();
	            objs[9] = po.getAddWork();
	            objs[10] = po.getAddWorkDes();
	            objs[11] = po.getRewardMoney();
	            objs[12] = po.getRewardDes();
	            objs[13] = po.getHolidayReduce();
	            objs[14] = po.getHolidayDes();
	            objs[15] = po.getFuliMoney();
	            objs[16] = po.getFuliDes();
	            objs[17] = po.getOtherAdd();
	            objs[18] = po.getOtherAddDes();
	            objs[19] = po.getOtherReduce();
	            objs[20] = po.getOtherReduceDes();
	            objs[21] = po.getTotal();
	            listObj.add(objs);  
	        }  

			ExportExcelUtils ex = new ExportExcelUtils("薪资发放表-"+obj_month+"", rowsName, listObj,response);  
	        ex.exportData();  
			return null;
		}
		

	// 计算新资
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String ids,  @AuthenticationPrincipal PtUser user) {
		AjaxReturn ret = new AjaxReturn();
		try {
			String[] temp = ids.split(",");
			int id = -1;
			List<PtUser> userList = new ArrayList<>();
			for (String string : temp) {
				id = Integer.parseInt(string);
				PtUser objUser = ptUserMapper.selectByPrimaryKey(id);
				userList.add(objUser);
			}

			salaryService.countSlary(userList, user);
			ret.setCode(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg("薪资计算失败");
		}

		return ret;
	}
	
	// 计算新资
		@RequestMapping(value = "/edit", method = RequestMethod.POST)
		public @ResponseBody AjaxReturn editAjax(@RequestParam int id,
				@RequestParam BigDecimal edit_fix_money,
				@RequestParam String edit_fix_des,
				@RequestParam BigDecimal edit_jijian_money,
				@RequestParam String edit_jijian_money_des,
				@RequestParam BigDecimal edit_addwork_money,
				@RequestParam String edit_addwork_money_des,
				@RequestParam BigDecimal edit_reward_money,
				@RequestParam String edit_reward_money_des,
				@RequestParam BigDecimal edit_fuli_money,
				@RequestParam String edit_fuli_money_des,
				@RequestParam BigDecimal edit_holiday_money,
				@RequestParam String edit_holiday_money_des,
				@RequestParam BigDecimal edit_other_add_money,
				@RequestParam String edit_other_add_money_des,
				@RequestParam BigDecimal edit_other_reduce_money,
				@RequestParam String edit_other_reduce_money_des,
				
				@AuthenticationPrincipal PtUser user) {
			AjaxReturn ret = new AjaxReturn();
			try {
				 
				Salary old = salaryMapper.selectByPrimaryKey(id);
				old.setFixMoney(edit_fix_money);
				old.setFixMoneyDes(edit_fix_des);
				old.setAchievement(edit_jijian_money);
				old.setAchievementDes(edit_jijian_money_des);
				old.setAddWork(edit_addwork_money);
				old.setAddWorkDes(edit_addwork_money_des);
				old.setRewardMoney(edit_reward_money);
				old.setRewardDes(edit_reward_money_des);
				old.setFuliMoney(edit_fuli_money);
				old.setFuliDes(edit_fuli_money_des);
				old.setHolidayReduce(edit_holiday_money);
				old.setHolidayDes(edit_holiday_money_des);
				old.setOtherAdd(edit_other_add_money);
				old.setOtherAddDes(edit_other_add_money_des);
				old.setOtherReduce(edit_other_reduce_money);
				old.setOtherReduceDes(edit_other_reduce_money_des);
				BigDecimal tempMoney= new BigDecimal(0);
				tempMoney=tempMoney.add(old.getFixMoney());
				tempMoney=tempMoney.add(old.getAchievement());
				tempMoney=tempMoney.add(old.getFuliMoney());
				tempMoney=tempMoney.add(old.getRewardMoney());
				tempMoney=tempMoney.add(old.getAddWork());
				tempMoney=tempMoney.add(old.getOtherAdd());
				tempMoney=tempMoney.subtract(old.getHolidayReduce());
				tempMoney=tempMoney.subtract(old.getOtherReduce());
				old.setTotal(tempMoney);
				salaryMapper.updateByPrimaryKey(old);
				ret.setCode(1);
			} catch (Exception ex) {
				TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
				ret.setRetMsg("修改失败");
			}

			return ret;
		}
		

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody int del(@RequestParam int id, @AuthenticationPrincipal PtUser user1) {
		salaryMapper.deleteByPrimaryKey(id);
		return 1;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody Salary get(@RequestParam int id, @AuthenticationPrincipal PtUser user1) {
		return salaryMapper.selectByPrimaryKey(id);
	}

}
