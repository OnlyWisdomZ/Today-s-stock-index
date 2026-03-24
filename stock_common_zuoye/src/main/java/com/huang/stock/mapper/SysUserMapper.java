package com.huang.stock.mapper;

import com.huang.stock.pojo.domain.UserRoleDomain;
import com.huang.stock.pojo.entity.SysUser;
import com.huang.stock.pojo.entity.SysUserInfos;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
* @author OnlyWisdomZ
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    SysUser findByUsernameSysUser(@Param("userName")String userName);

    SysUser findByUsername(@Param("username") String username);
    /*
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * */
    List<SysUserInfos> getUserInfos(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /*
     * 获取所有的角色信息
     * */
    List<UserRoleDomain> getAllUserRole();
    /*
     * 获取用户具有的角色信息
     * */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

}
