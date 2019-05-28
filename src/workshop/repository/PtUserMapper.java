package workshop.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import workshop.model.PtUser;

public interface PtUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    int insert(PtUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    int insertSelective(PtUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    PtUser selectByPrimaryKey(Integer id);
    
    List<PtUser> searchBySql(@Param("sql")String sql);

    PtUser selectByPhone(@Param("phone") String phone);
    
    int countBySql(@Param("sql") String sql);
    
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PtUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pt_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PtUser record);
    
    int updateLeaveMore(@Param("list") List<Integer> list);
    
    int updateRestPwdMore(@Param("list") List<Integer> list,@Param("pwd") String pwd);
}