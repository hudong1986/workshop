package workshop.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import workshop.model.Salary;
import workshop.model.SalaryReport;
import workshop.model.StatisticReport;

public interface SalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Salary record);

    int insertSelective(Salary record);

    Salary selectByPrimaryKey(Integer id);
    
    List<Salary> searchBySql(@Param("sql")String sql);

    List<SalaryReport> searchGroup1(@Param("up_down_id") String updwonid,@Param("month")String month);
    
    int updateByPrimaryKeySelective(Salary record);

    int updateByPrimaryKey(Salary record);
}