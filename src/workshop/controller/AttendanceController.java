package workshop.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import workshop.model.AjaxReturn;
import workshop.model.Attendance;
import workshop.model.Pager;
import workshop.model.PtDept;
import workshop.model.PtRole;
import workshop.model.PtUser;
import workshop.util.DateUtil;
import workshop.util.SecretHelper;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String list(Model model, @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "all") String up_down_id,
			@RequestParam(required = false, defaultValue = "-1") int type,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
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

		Pager pager = pagerServer.searchAttendance(phone, user_name, type, up_down_id, beg_time, end_time, orderField,
				orderType, pageNum, currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		model.addAttribute("type", type);
		return null;
	}

	// 分页功能
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String pager(Model model, @RequestParam(required = false, defaultValue = "") String phone,
			@RequestParam(required = false, defaultValue = "") String user_name,
			@RequestParam(required = false, defaultValue = "all") String up_down_id,
			@RequestParam(required = false, defaultValue = "-1") int type,
			@RequestParam(required = false, defaultValue = "") String orderField,
			@RequestParam(required = false, defaultValue = "") String orderType,
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

		Pager pager = pagerServer.searchAttendance(phone, user_name, type, up_down_id, beg_time, end_time, orderField,
				orderType, pageNum, currentPageSize);
		model.addAttribute("pager", pager);
		model.addAttribute("phone", phone);
		model.addAttribute("user_name", user_name);
		model.addAttribute("currentPageSize", currentPageSize);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		model.addAttribute("type", type);
		String retStr = "attendance/list";
		return retStr;
	}

	// 添加员工
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody AjaxReturn addAjax(@RequestParam String ids, @RequestParam int type,
			@RequestParam String beg_time, @RequestParam String end_time, @RequestParam String remark,
			@AuthenticationPrincipal PtUser user) {
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
				Attendance attendance = new Attendance();
				attendance.setType((byte) type);
				attendance.setBegTime(DateUtil.parse(beg_time, "yyyy-MM-dd HH:mm:ss"));
				attendance.setEndTime(DateUtil.parse(end_time, "yyyy-MM-dd HH:mm:ss"));
				attendance.setPhone(objUser.getPhone());
				attendance.setDeptName(objUser.getDeptName());
				attendance.setRemark(remark);
				attendance.setUpDownId(objUser.getUpDownId());
				attendance.setUserName(objUser.getRealName());
				attendanceMapper.insert(attendance);

			}

			ret.setCode(1);
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			ex.printStackTrace();
			ret.setRetMsg("添加失败");
		}

		return ret;
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody int del(@RequestParam int id, @AuthenticationPrincipal PtUser user1) {
		attendanceMapper.deleteByPrimaryKey(id);
		return 1;
	}

}
