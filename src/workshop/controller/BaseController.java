package workshop.controller;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import workshop.repository.AttendanceMapper;
import workshop.repository.FinishStatisticMapper;
import workshop.repository.GoodsInOutMapper;
import workshop.repository.ProductMapper;
import workshop.repository.PtDeptMapper;
import workshop.repository.PtRoleMapper;
import workshop.repository.PtUserMapper;
import workshop.repository.RewardMapper;
import workshop.repository.SalaryMapper;
import workshop.repository.SystemParamMapper;
import workshop.service.PagerServiceAdapter;
import workshop.service.SalaryService;
import workshop.service.SystemProperty;
import workshop.util.DateUtil;

public class BaseController {

	@Autowired
	PtUserMapper ptUserMapper;
	
	@Autowired
	AttendanceMapper attendanceMapper;
	
	@Autowired
	FinishStatisticMapper finishStatisticMapper;
	
	@Autowired
	GoodsInOutMapper goodsInOutMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	@Autowired
	PtDeptMapper ptDeptMapper;
	
	@Autowired
	PtRoleMapper ptRoleMapper;
	
	@Autowired
	RewardMapper rewardMapper;
	
	@Autowired
	SalaryMapper salaryMapper;
	
	@Autowired
	SystemParamMapper systemParamMapper;
	
	@Autowired
	PagerServiceAdapter pagerServer;
	
	@Autowired
	SalaryService salaryService;
	
	@Autowired
	SystemProperty systemProperty;
	
 
	/*public void check() throws Exception{
		if(new Date().getTime()> DateUtil.parse("2017-08-16").getTime()){
			throw new Exception("对不起，系统有效期已经过，请续费");
		}
	}*/
}
