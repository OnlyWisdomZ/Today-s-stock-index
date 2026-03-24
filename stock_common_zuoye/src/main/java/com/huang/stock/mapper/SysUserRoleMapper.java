package com.huang.stock.mapper;

import com.huang.stock.pojo.entity.SysUserRole;

/**
* @author OnlyWisdomZ
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}
