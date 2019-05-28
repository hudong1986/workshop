package workshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import workshop.model.AjaxReturn;
import workshop.model.Pager;
import workshop.model.SystemParam;

//系统参数相关
@Controller
@RequestMapping("system")
public class SystemSetController extends BaseController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		Pager pager = new Pager();
		List<SystemParam> list = systemParamMapper.selectAll();
		if (list != null && list.size() > 0) {
			pager.setTotalCount(list.size());
			pager.setData(list);
			model.addAttribute("pager", pager);
		}
		
		return "system/list";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody int edit(
			@RequestParam int id,
			@RequestParam String edit_value,  
			@RequestParam String edit_remark
			) {
		SystemParam systemParam = systemParamMapper.selectByPrimaryKey(id);
		if(systemParam!=null){
			systemParam.setSysValue(edit_value);
			systemParam.setRemark(edit_remark);
			systemParamMapper.updateByPrimaryKey(systemParam);
			return 1;
		}
		
		return 0;
	}
}
