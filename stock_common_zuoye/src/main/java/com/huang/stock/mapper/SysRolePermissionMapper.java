package com.huang.stock.mapper;

import com.huang.stock.pojo.entity.SysRolePermission;

/**
* @author OnlyWisdomZ
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

}
