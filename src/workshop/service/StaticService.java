package workshop.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workshop.model.PtDept;
import workshop.model.PtRole;
import workshop.repository.PtDeptMapper;
import workshop.repository.PtRoleMapper;
import workshop.util.TxtLogger;
import workshop.util.TxtLogger.LogFileCreateType;

 

@Service
public class StaticService {

	@Autowired
	PtDeptMapper deptMapper;

	@Autowired
	PtRoleMapper roleMapper;


	static List<PtDept> Deptlist;
	
	public static List<PtDept> getDeptlist() {
		return Deptlist;
	}
	
	public static List<PtDept> getDeptlist(String upDownId) {
		List<PtDept> list= Deptlist.stream().filter(x->x.getUpDownId().contains(upDownId)).collect(Collectors.toList());
		return list;
	}

	 

	public static List<PtRole> getRoleLst() {
		return RoleLst;
	}

	 


	static List<PtRole> RoleLst;


	@PostConstruct
	private void init() {
		try {
			Deptlist = deptMapper.selectALL();
			RoleLst = roleMapper.selectAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
		}
	}

}
