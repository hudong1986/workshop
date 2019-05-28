package workshop.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import workshop.model.Attendance;
import workshop.model.FinishStatistic;
import workshop.model.PtUser;
import workshop.model.Reward;
import workshop.model.Salary;
import workshop.model.SystemParam;
import workshop.repository.AttendanceMapper;
import workshop.repository.FinishStatisticMapper;
import workshop.repository.RewardMapper;
import workshop.repository.SalaryMapper;
import workshop.repository.SystemParamMapper;
import workshop.util.ByteUtil;
import workshop.util.DateUtil;
import workshop.util.StringHelper;

@Service
public class SalaryService {

	@Autowired
	SystemParamMapper systemParamMapper;

	@Autowired
	RewardMapper rewardMapper;

	@Autowired
	FinishStatisticMapper finishStatisticMapper;

	@Autowired
	AttendanceMapper attendanceMapper;

	@Autowired
	SalaryMapper salaryMapper;

	@Transactional
	public List<Salary> countSlary(List<PtUser> userList, PtUser addUser) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String firstday, lastday;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		firstday = format.format(calendar.getTime());

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		lastday = format.format(calendar.getTime());

		String begTimeStr = firstday + " 00:00:00";
		String endTimeStr = lastday + " 23:59:59";
		String objMonth = begTimeStr.substring(0, 7);// Ŀ����
		String sql;
		// ��ȡϵͳ����
		List<SystemParam> params = systemParamMapper.selectAll();
		BigDecimal quanqing =new BigDecimal(0);
		BigDecimal traffic =new BigDecimal(0);
		BigDecimal food =new BigDecimal(0);
		int addPerHour = 0;
		for (SystemParam systemParam : params) {
			if(systemParam.getSysKey().equals("ȫ��")){
				quanqing=new BigDecimal(systemParam.getSysValue());
			}
			if(systemParam.getSysKey().equals("��ͨ����")){
				traffic=new BigDecimal(systemParam.getSysValue());
			}
			if(systemParam.getSysKey().equals("�Ͳ�")){
				food=new BigDecimal(systemParam.getSysValue());
			}
			if(systemParam.getSysKey().equals("�Ӱ๤��")){
				addPerHour=Integer.parseInt(systemParam.getSysValue());
			}
		}
		
		
		// ��ȡĿ���½���
		sql = "select * from reward where obj_month='" + objMonth + "'";
		List<Reward> rewards = rewardMapper.searchBySql(sql);
		// ��ȡ����
		sql = String.format(
				" select * from attendance where (beg_time<='%s' and end_time>'%s') or (beg_time<'%s' and end_time>='%s') "
						+ "or (beg_time>='%s' and end_time<='%s')  ",
				endTimeStr, endTimeStr, begTimeStr, begTimeStr, begTimeStr, endTimeStr);
		List<Attendance> attendances = attendanceMapper.searchBySql(sql);

		// ��ȡ�Ǽ�
		sql = String.format("select * from finish_statistic where addtime>='%s' and addtime <='%s'", begTimeStr,
				endTimeStr);
		List<FinishStatistic> finishStatistics = finishStatisticMapper.searchBySql(sql);

		BigDecimal tempMoney=new BigDecimal(0);
		StringBuilder sBuilder =new StringBuilder();
		
		List<Salary> salaries = new ArrayList<>();
		for (PtUser user : userList) {
			//����²ż���Ĳ�����
			if(user.getAddTime().getTime()>DateUtil.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss").getTime()){
				continue; //��ǰ�²ż����ְԱҪ�¸��²ż��㹤��
			}
			
			//���ϸ�����ְ�Ĳ�����
			if(user.getState()==1 && user.getLeaveTime().getTime()<DateUtil.parse(begTimeStr, "yyyy-MM-dd HH:mm:ss").getTime()){
				continue;  
			}
			
			Salary salary = new Salary();
			salary.setAddUserId(addUser.getDeptId());
			salary.setAddUserName(addUser.getRealName());
			salary.setUserId(user.getId());
			salary.setDeptName(user.getDeptName());
			salary.setPhone(user.getPhone());
			salary.setUserName(user.getRealName());
			salary.setUpDownId(user.getUpDownId());
			salary.setMonth(objMonth);
			// ����̶����� ������ְ�����
			salary.setFixMoney(new BigDecimal(user.getFixMoney()));
			if(user.getAddTime().getTime()>DateUtil.parse(begTimeStr, "yyyy-MM-dd HH:mm:ss").getTime()
					|| user.getLeaveTime().getTime()<DateUtil.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss").getTime()
					){
				//������ְ������ǰ��ְ��
				salary.setFixMoneyDes("��Ա��������ְ����������ְ�����޸Ļ�������");
			}
			else {
				salary.setFixMoneyDes("�������Ż�������:"+user.getFixMoney());
			}
			// ���㽱��
			tempMoney= new BigDecimal(0);
			sBuilder =new StringBuilder();
			if (rewards != null && rewards.size() > 0) {
				List<Reward> tempRewardList = rewards.stream().filter(x -> x.getPhone().equals(user.getPhone()))
						.collect(Collectors.toList());
				if(tempRewardList!=null){
					for (Reward reward : tempRewardList) {
						sBuilder.append(String.format("[%s-%.2f],", reward.getRemark(),reward.getMoney()));
						tempMoney=tempMoney.add(reward.getMoney());
					}
					salary.setRewardDes(StringHelper.trim(sBuilder.toString(),','));
					salary.setRewardMoney(tempMoney);
				}
			}

			// �Ǽ����
			tempMoney= new BigDecimal(0);
			sBuilder =new StringBuilder();
			if (finishStatistics != null && finishStatistics.size() > 0) {
				List<FinishStatistic> tempFinishList = finishStatistics.stream().filter(x -> x.getPhone().equals(user.getPhone()))
						.collect(Collectors.toList());
				if(tempFinishList!=null){
					for (FinishStatistic item : tempFinishList) {
						sBuilder.append(String.format("[%s-%s-%d],",DateUtil.format(item.getAddtime(),"yyyy-MM-dd"), item.getProductName(),item.getNum()));
						tempMoney=tempMoney.add(item.getGetMoney());
					}
					salary.setAchievementDes(StringHelper.trim(sBuilder.toString(),','));
					salary.setAchievement(tempMoney);
				}
			}
			
			// �����¼�
			tempMoney= new BigDecimal(0);
			sBuilder =new StringBuilder();
			if (attendances != null && attendances.size() > 0) {
				List<Attendance> tempAttendanceList = attendances.stream().filter(x -> x.getPhone().equals(user.getPhone()) && x.getType()==1)
						.collect(Collectors.toList());
				if(tempAttendanceList!=null && tempAttendanceList.size()>0){
					for (Attendance item : tempAttendanceList) {
						sBuilder.append(String.format("[%s��%s],", DateUtil.format(item.getBegTime(),"yyyy-MM-dd HH:mm:ss"),
								DateUtil.format(item.getEndTime(),"yyyy-MM-dd HH:mm:ss")));
					}
					 
					salary.setHolidayDes("��Ա���������¼٣����������۳���Ӧн��"+StringHelper.trim(sBuilder.toString(),','));
					salary.setHolidayReduce(tempMoney);
				}
			}
			//����Ӱ�
			tempMoney= new BigDecimal(0);
			sBuilder =new StringBuilder();
			if (attendances != null && attendances.size() > 0) {
				List<Attendance> tempAttendanceList = attendances.stream().filter(x -> x.getPhone().equals(user.getPhone()) && x.getType()==2)
						.collect(Collectors.toList());
				if(tempAttendanceList!=null){
					for (Attendance item : tempAttendanceList) {
						double add_hours=(item.getEndTime().getTime()-item.getBegTime().getTime())/3600000.0;
						BigDecimal add_hour_money= new BigDecimal(add_hours*addPerHour);
						tempMoney=tempMoney.add(add_hour_money);
						sBuilder.append(String.format("[%s��%s],", DateUtil.format(item.getBegTime(),"yyyy-MM-dd HH:mm:ss"),
								DateUtil.format(item.getEndTime(),"yyyy-MM-dd HH:mm:ss")));
						
					}
					salary.setAddWorkDes(StringHelper.trim(sBuilder.toString(),','));
					salary.setAddWork(tempMoney);
				}
			}
			// ���㸣��
			tempMoney= new BigDecimal(0);
			sBuilder =new StringBuilder();
			if(StringUtils.isBlank(salary.getHolidayDes())){
				//˵��û������¼�
				tempMoney=tempMoney.add(quanqing);
				tempMoney=tempMoney.add(traffic.multiply(new BigDecimal(user.getWorkDays())));
				tempMoney=tempMoney.add(food.multiply(new BigDecimal(user.getWorkDays())));
				sBuilder.append("ȫ��+�Ͳ�+��ͨ����");
			}
			else{
				sBuilder.append("��Ա���漰�¼٣����޸�Ϊ��Ӧ�ĸ����ܺ�");
			}
			
			salary.setFuliDes(sBuilder.toString());
			salary.setFuliMoney(tempMoney);
			tempMoney= new BigDecimal(0); //�����ܵ�����
			tempMoney=tempMoney.add(salary.getFixMoney());
			tempMoney=tempMoney.add(salary.getAchievement());
			tempMoney=tempMoney.add(salary.getFuliMoney());
			tempMoney=tempMoney.add(salary.getRewardMoney());
			tempMoney=tempMoney.add(salary.getAddWork());
			tempMoney=tempMoney.add(salary.getOtherAdd());
			tempMoney=tempMoney.subtract(salary.getHolidayReduce());
			tempMoney=tempMoney.subtract(salary.getOtherReduce());
			salary.setTotal(tempMoney);
			salary.setAddtime(new Date());
			salaryMapper.insert(salary);
			salaries.add(salary);
		}

		return salaries;
	}
}
