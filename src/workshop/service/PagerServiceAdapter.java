package workshop.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import workshop.model.Attendance;
import workshop.model.FinishStatistic;
import workshop.model.GoodsInOut;
import workshop.model.Pager;
import workshop.model.PtUser;
import workshop.model.Reward;
import workshop.model.Salary;
import workshop.repository.AttendanceMapper;
import workshop.repository.FinishStatisticMapper;
import workshop.repository.GoodsInOutMapper;
import workshop.repository.PtUserMapper;
import workshop.repository.RewardMapper;
import workshop.repository.SalaryMapper;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;
import workshop.util.TxtLogger.LogTye;

@Service
public class PagerServiceAdapter {

	@Value("#{sysProperties['page_size']}")
	private int pageSize = 20;

	@Autowired
	PtUserMapper ptUserMapper;
	@Autowired
	AttendanceMapper attendanceMapper;
	@Autowired
	RewardMapper rewardMapper;

	@Autowired
	FinishStatisticMapper finishStatisticMapper;

	@Autowired
	SalaryMapper salaryMapper;

	@Autowired
	GoodsInOutMapper goodsInOutMapper;

	// 查询用户表信息
	public Pager searchEmployee(String phone, String real_name, int state, String up_down_id, String orderField,
			String orderType, int pageNum, int currentPageSize) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from pt_user where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(real_name)) {
			sWhere.append(" and real_name like '%" + real_name + "%'");
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		if (state != -1) {
			sWhere.append(" and state = " + state);
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "asc";
			orderString = " order by id asc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from pt_user where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from pt_user where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from pt_user where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<PtUser> list = ptUserMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&real_name=%s&state=%d&up_down_id=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d&currentPageSize=%d",
				phone, real_name, state, up_down_id, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1, currentPageSize);
		String prePage = String.format(
				"phone=%s&real_name=%s&state=%d&up_down_id=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d&currentPageSize=%d",
				phone, real_name, state, up_down_id, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1,
				currentPageSize);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&real_name=%s&state=%d&up_down_id=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d&currentPageSize=%d",
				phone, real_name, state, up_down_id, "id", "asc", 1, currentPageSize);
		String tempOrderString;

		// 设置排序

		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("add_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	// 查询考勤表信息
	public Pager searchAttendance(String phone, String user_name, int type, String up_down_id, String beg_time,
			String end_time, String orderField, String orderType, int pageNum, int currentPageSize)
			throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from attendance where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(beg_time) && StringUtils.isNotBlank(end_time)) {
			sWhere.append(String.format(
					" and ((beg_time<='%s 23:59:59' and end_time>'%s 23:59:59') or (beg_time<'%s' and end_time>='%s') "
							+ "or (beg_time>='%s' and end_time<='%s 23:59:59'))  ",
					end_time, end_time, beg_time, beg_time, beg_time, end_time));
		} else if (StringUtils.isNotBlank(beg_time)) {
			sWhere.append(" and beg_time >= '" + beg_time + "'");
		} else if (StringUtils.isNotBlank(end_time)) {
			sWhere.append(" and end_time <= '" + end_time + " 23:59:59'");
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		if (type != -1) {
			sWhere.append(" and type = " + type);
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from attendance where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from attendance where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from attendance where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<Attendance> list = attendanceMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&user_name=%s&type=%d&up_down_id=%s&" + "beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, type, up_down_id, beg_time, end_time, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&user_name=%s&type=%d&up_down_id=%s&" + "beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, type, up_down_id, beg_time, end_time, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&user_name=%s&type=%d&up_down_id=%s&" + "beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, type, up_down_id, beg_time, end_time, "id", "asc", 1);
		String tempOrderString;

		// 设置排序
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("beg_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=beg_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=beg_time");
			pager.setOrderString3(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("end_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=end_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString4(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=end_time");
			pager.setOrderString4(tempOrderString);
		}

		return pager;
	}

	// 奖励
	public Pager searchReward(String phone, String user_name, String up_down_id, String obj_month, String orderField,
			String orderType, int pageNum, int currentPageSize) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from reward where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(obj_month)) {
			sWhere.append(" and obj_month = '" + obj_month + "'");
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from reward where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from reward where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from reward where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<Reward> list = rewardMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, "id", "asc", 1);
		String tempOrderString;

		// 设置排序
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("obj_month")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=obj_month");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=obj_month");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	public Pager searchSalary(String phone, String user_name, String up_down_id, String obj_month, String orderField,
			String orderType, int pageNum, int currentPageSize) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from salary where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(obj_month)) {
			sWhere.append(" and month = '" + obj_month + "'");
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from salary where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from salary where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from salary where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<Salary> list = salaryMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "obj_month=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, obj_month, "id", "asc", 1);
		String tempOrderString;

		// 设置排序
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("month")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=month");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=month");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	// 员工记件入库
	public Pager searchFinishStastic(String phone, String user_name, String up_down_id, int product_id, String beg_time,
			String end_time, String orderField, String orderType, int pageNum, int currentPageSize)
			throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from finish_statistic where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(beg_time)) {
			sWhere.append(" and addtime >= '" + beg_time + "'");
		}

		if (StringUtils.isNotBlank(end_time)) {
			sWhere.append(" and addtime <= '" + end_time + "'");
		}

		if (product_id != -1) {
			sWhere.append(" and product_id = " + product_id);
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from finish_statistic where 1=1 %s",
				sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from finish_statistic where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from finish_statistic where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<FinishStatistic> list = finishStatisticMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, beg_time, end_time, product_id, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, beg_time, end_time, product_id, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&user_name=%s&up_down_id=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				phone, user_name, up_down_id, beg_time, end_time, product_id, "id", "asc", 1);
		String tempOrderString;

		// 设置排序
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("addtime")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=addtime");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=addtime");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	// 产品出入库
	public Pager searchGoodInOut(String direction, String batchNo, int product_id, String beg_time, String end_time,
			String orderField, String orderType, int pageNum, int currentPageSize) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from goods_in_out where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(direction)) {
			sWhere.append(" and direction = '" + direction + "'");
		}

		if (StringUtils.isNotBlank(batchNo)) {
			sWhere.append(" and batchNo like '%" + batchNo + "%'");
		}

		if (StringUtils.isNotBlank(beg_time)) {
			sWhere.append(" and addtime >= '" + beg_time + "'");
		}

		if (StringUtils.isNotBlank(end_time)) {
			sWhere.append(" and addtime <= '" + end_time + "'");
		}

		if (product_id != -1) {
			sWhere.append(" and product_id = " + product_id);
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from goods_in_out where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from goods_in_out where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from goods_in_out where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		List<GoodsInOut> list = goodsInOutMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"direction=%s&batchNo=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				direction, batchNo, beg_time, end_time, product_id, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"direction=%s&batchNo=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				direction, batchNo, beg_time, end_time, product_id, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"direction=%s&batchNo=%s&" + "beg_time=%s&end_time=%s&product_id=%d&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				direction, batchNo, beg_time, end_time, product_id, "id", "asc", 1);
		String tempOrderString;

		// 设置排序
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("product_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=product_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=product_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("addtime")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=addtime");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=addtime");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	public int getCount(String sql) throws SQLException {
		int count = 0;
		try {
			count = ptUserMapper.countBySql(sql);

		} catch (Exception ex) {

			TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "");
			ex.printStackTrace();
		}
		return count;
	}

}
