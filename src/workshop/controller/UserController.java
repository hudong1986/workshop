package workshop.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

 
import workshop.model.AjaxReturn;
import workshop.model.Pager;
import workshop.model.PtDept;
import workshop.model.PtRole;
import workshop.model.PtUser;
import workshop.util.DateUtil;
import workshop.util.SecretHelper;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String searchAll(Model model,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(value = "state", required = false, defaultValue = "-1") int state,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "0") int currentPageSize,
			@AuthenticationPrincipal PtUser user1

	) throws SQLException {
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

		
		Pager pager = pagerServer.searchEmployee(phone, user_name, state, up_down_id, orderField, orderType, pageNum,
				currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("state", state);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String pager(Model model, @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "all") String up_down_id,
			@RequestParam(value = "state", required = false, defaultValue = "-1") int state,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
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

		Pager pager = pagerServer.searchEmployee(phone, user_name, state, up_down_id, orderField, orderType, pageNum,
				currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("state", state);
		String retStr = "user/list";

		return retStr;
	}

	// 添加员工
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String phone, @RequestParam String real_name,
			@RequestParam String role_code, @RequestParam int deptId, @RequestParam String pwd, @RequestParam int sex,
			@RequestParam String add_job_name, @RequestParam int add_fix_money, @RequestParam int add_fix_days,
			@RequestParam String add_work_time, @RequestParam(required = false) MultipartFile file,
			HttpServletRequest request) {

		AjaxReturn ret = new AjaxReturn();

		try {

			PtUser old = ptUserMapper.selectByPhone(phone);
			if (old != null) {
				ret.setRetMsg("添加失败，该手机号的员工已存在！");
				return ret;
			}
			PtDept dept = ptDeptMapper.selectByPrimaryKey(deptId);
			PtRole role = ptRoleMapper.selectByPrimaryKey(role_code);
			PtUser user = new PtUser();
			user.setAddTime(DateUtil.parse(add_work_time, "yyyy-MM-dd HH:mm:ss"));
			user.setDeptId(dept.getId());
			user.setDeptName(dept.getDeptName());
			user.setUpDownId(dept.getUpDownId());
			user.setState((byte) 0);
			user.setPhone(phone);
			user.setRealName(real_name);
			user.setRoleCode(role.getRoleCode());
			user.setRoleName(role.getRoleName());
			user.setSex(sex == 1 ? true : false);
			user.setUserPwd(SecretHelper.parseStrToMd5U32(pwd));
			user.setJobName(add_job_name);
			user.setFixMoney(add_fix_money);
			user.setWorkDays((byte) add_fix_days);
			String saveFileName = "default_header.png";
			if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
				String uid = UUID.randomUUID().toString();
				saveFileName = uid + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String savepath = systemProperty.getHeader_pic_path() + "/" + saveFileName;
				file.transferTo(new File(savepath));
			}

			user.setPicUrl(saveFileName);
			ptUserMapper.insert(user);
			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ret.setRetMsg(ex.getMessage());
		}

		return ret;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody int edit(@RequestParam int id, @RequestParam String deptId, @RequestParam String roleId,
			@RequestParam String edit_name, @RequestParam int fix_money, @RequestParam int fix_days,
			@RequestParam String job_name, @RequestParam int state, @AuthenticationPrincipal PtUser user1) {

		PtUser user = ptUserMapper.selectByPrimaryKey(id);
		PtDept dept = ptDeptMapper.selectByPrimaryKey(Integer.parseInt(deptId));
		PtRole role = ptRoleMapper.selectByPrimaryKey(roleId);
		user.setDeptId(dept.getId());
		user.setDeptName(dept.getDeptName());
		user.setUpDownId(dept.getUpDownId());
		user.setRealName(edit_name);
		user.setState((byte) state);
		user.setRoleCode(role.getRoleCode());
		user.setRoleName(role.getRoleName());
		user.setJobName(job_name);
		user.setFixMoney(fix_money);
		user.setWorkDays((byte) fix_days);
		ptUserMapper.updateByPrimaryKeySelective(user);

		return 1;
	}

	@RequestMapping(value = "/leaveMore", method = RequestMethod.POST)
	public @ResponseBody int leaveMore(@RequestParam String ids, @AuthenticationPrincipal PtUser user) {
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		int id = -1;
		for (String string : temp) {
			id = Integer.parseInt(string);
			if (user.getId() == id) {
				continue;
			}

			list.add(Integer.parseInt(string));
		}

		ptUserMapper.updateLeaveMore(list);
		return 1;
	}

	@RequestMapping(value = "/resetPwdMore", method = RequestMethod.POST)
	public @ResponseBody int resetPwdMore(@RequestParam String ids) {
		List<Integer> list = new ArrayList<>();
		String[] temp = ids.split(",");
		for (String string : temp) {
			list.add(Integer.parseInt(string));
		}

		ptUserMapper.updateRestPwdMore(list, SecretHelper.parseStrToMd5U32("123456"));
		return 1;
	}
	
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.GET)
	public String addAjax() {
		return "user/modifyPwd";
	}

	
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	public String modifyPwd(@RequestParam String old_pwd, @RequestParam String add_pwd1,
			@AuthenticationPrincipal PtUser user1, Model model, @RequestParam(required = false) MultipartFile file,
			HttpServletRequest request) throws IllegalStateException, IOException {
		if (SecretHelper.parseStrToMd5U32(old_pwd).equals(user1.getUserPwd())) {
			user1.setUserPwd(SecretHelper.parseStrToMd5U32(add_pwd1));
			String saveFileName = "default_header.png";
			if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
				String uid = UUID.randomUUID().toString();
				saveFileName = uid + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				//String savepath = systemProperty.getHeader_pic_path() + "/" + saveFileName;
				String savepath = request.getServletContext().getRealPath("assets/img/header") + "/" + saveFileName;
				file.transferTo(new File(savepath));
				user1.setPicUrl(saveFileName);
			}

			ptUserMapper.updateByPrimaryKey(user1);
			model.addAttribute("msg", "修改成功，请退出后重新登录");
		} else {
			model.addAttribute("msg", "修改失败，原密码不对！");
		}

		return null;
	}
}
