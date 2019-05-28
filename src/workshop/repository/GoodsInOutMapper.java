package workshop.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import workshop.model.GoodsInOut;
import workshop.model.GoodsInReport;
import workshop.model.StatisticReport;

public interface GoodsInOutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsInOut record);

    int insertSelective(GoodsInOut record);

    GoodsInOut selectByPrimaryKey(Integer id);
    
    List<GoodsInOut> searchBySql(@Param("sql")String sql);

    List<GoodsInReport> searchGroup1(@Param("direction")String direction,@Param("beg_time")String beg_time,
    		@Param("end_time")String end_time);
    
    int updateByPrimaryKeySelective(GoodsInOut record);

    int updateByPrimaryKey(GoodsInOut record);
}