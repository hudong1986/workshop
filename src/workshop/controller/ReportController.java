package workshop.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import workshop.model.GoodsInReport;
import workshop.model.Pager;
import workshop.model.PtUser;
import workshop.model.SalaryReport;
import workshop.model.StatisticReport;
import workshop.util.DateUtil;
import workshop.util.ExportExcelUtils;

@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {

	@RequestMapping(value = "/statistic")
	public String list(Model model,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "0") int isExport,
			@AuthenticationPrincipal PtUser user1,HttpServletResponse response

	) throws SQLException {
		if (user1.getRoleCode().equals("ADMIN") || user1.getRoleCode().equals("HrManager")
				|| user1.getRoleCode().equals("HrAssistant")) {

		} else {

			up_down_id = user1.getUpDownId();

		}
		
		if(StringUtils.isBlank(beg_time) || StringUtils.isBlank(end_time)){
			beg_time= DateUtil.format(DateUtil.addMonth(new Date(), -1),"yyyy-MM-dd");
			end_time= DateUtil.format(new Date(),"yyyy-MM-dd");
		}
		
		Pager pager = new Pager();
		List<StatisticReport> list = finishStatisticMapper.searchGroup1(up_down_id, beg_time, end_time);
		if(list!=null&& list.size()>0){
			//判断是否导出操作
			if(isExport==1){
				String[] rowsName=new String[]{"ID","部门","产品","总数量"};  
				List<Object[]> listObj = new ArrayList<>();
				Object[] objs = null;  
		        for (int i = 0; i < list.size(); i++) {  
		        	StatisticReport po = list.get(i);  
		            objs = new Object[rowsName.length];  
		            objs[1] = po.getDept_name(); 
		            objs[2] = po.getProduct_name(); 
		            objs[3] = po.getNum();  
		            listObj.add(objs);  
		        }  

				ExportExcelUtils ex = new ExportExcelUtils("记件统计表-"+beg_time+"至"+end_time+"", rowsName, listObj,response);  
		        ex.exportData();  
				return null;
			}
			
			pager.setData(list);
			pager.setTotalCount(list.size());
		}
		model.addAttribute("pager", pager);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		return "report/statistic";
	}
	
	@RequestMapping(value = "/salary")
	public String salary(Model model,
			@RequestParam(required = false, defaultValue = "") String up_down_id,
			@RequestParam(required = false, defaultValue = "") String month,
			@RequestParam(required = false, defaultValue = "0") int isExport,
			@AuthenticationPrincipal PtUser user1,HttpServletResponse response

	) throws SQLException {
		if (user1.getRoleCode().equals("ADMIN") || user1.getRoleCode().equals("HrManager")
				|| user1.getRoleCode().equals("HrAssistant")) {

		} else {

			up_down_id = user1.getUpDownId();

		}
		
		if(StringUtils.isBlank(month)){
			month= DateUtil.format(DateUtil.addMonth(new Date(), -1),"yyyy-MM");
		}
		
		Pager pager = new Pager();
		List<SalaryReport> list = salaryMapper.searchGroup1(up_down_id,month);
		if(list!=null&& list.size()>0){
			//判断是否导出操作
			if(isExport==1){
				String[] rowsName=new String[]{"ID","部门","总发放(元)"};  
				List<Object[]> listObj = new ArrayList<>();
				Object[] objs = null;  
		        for (int i = 0; i < list.size(); i++) {  
		        	SalaryReport po = list.get(i);  
		            objs = new Object[rowsName.length];  
		            objs[1] = po.getDept_name(); 
		            objs[2] = po.getToalMoney(); 
		            listObj.add(objs);  
		        }  

				ExportExcelUtils ex = new ExportExcelUtils("薪资统计表-"+month+"", rowsName, listObj,response);  
		        ex.exportData();  
				return null;
			}
			pager.setData(list);
			pager.setTotalCount(list.size());
		}
		model.addAttribute("pager", pager);
		model.addAttribute("up_down_id", up_down_id);
		model.addAttribute("month", month);
		return "report/salary";
	}

	@RequestMapping(value = "/goods_in_out")
	public String goods_in_out(Model model,
			@RequestParam(required = false, defaultValue = "出库") String direction,
			@RequestParam(required = false, defaultValue = "") String beg_time,
			@RequestParam(required = false, defaultValue = "") String end_time,
			@RequestParam(required = false, defaultValue = "0") int isExport,
			@AuthenticationPrincipal PtUser user1,HttpServletResponse response

	) throws SQLException {
		 
		
		if(StringUtils.isBlank(beg_time) || StringUtils.isBlank(end_time)){
			beg_time= DateUtil.format(DateUtil.addMonth(new Date(), -1),"yyyy-MM-dd");
			end_time= DateUtil.format(new Date(),"yyyy-MM-dd");
		}
		
		Pager pager = new Pager();
		List<GoodsInReport> list = goodsInOutMapper.searchGroup1(direction, beg_time, end_time);
		if(list!=null&& list.size()>0){
			//判断是否导出操作
			if(isExport==1){
				String[] rowsName=new String[]{"ID","产品","数量","总金额(元)"};  
				List<Object[]> listObj = new ArrayList<>();
				Object[] objs = null;  
		        for (int i = 0; i < list.size(); i++) {  
		        	GoodsInReport po = list.get(i);  
		            objs = new Object[rowsName.length];  
		            objs[1] = po.getProduct_name(); 
		            objs[2] = po.getNum(); 
		            objs[3] = po.getTotalMoney();
		            listObj.add(objs);  
		        }  

				ExportExcelUtils ex = new ExportExcelUtils("出入库统计表-"+beg_time+"至"+end_time+"", rowsName, listObj,response);  
		        ex.exportData();  
				return null;
			}
			
			pager.setData(list);
			pager.setTotalCount(list.size());
		}
		model.addAttribute("pager", pager);
		model.addAttribute("direction", direction);
		model.addAttribute("beg_time", beg_time);
		model.addAttribute("end_time", end_time);
		return "report/goods_in_out";
	}
 
	 
}
