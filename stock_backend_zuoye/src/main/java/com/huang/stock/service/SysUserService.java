package com.huang.stock.service;

import com.huang.stock.pojo.entity.SysUser;
import com.huang.stock.pojo.entity.SysUserInfos;
import com.huang.stock.vo.req.SysAddUserReqVo;
import com.huang.stock.vo.req.SysLoginReqVo;
import com.huang.stock.vo.req.SysUpdateUserReqVo;
import com.huang.stock.vo.resp.PageResult;
import com.huang.stock.vo.resp.R;
import com.huang.stock.vo.resp.SysLoginRespVo;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface SysUserService {
    SysUser selectUserByname(String userName);

    R<SysLoginRespVo> login(SysLoginReqVo vo);

    R<Map> getCaptCode();

    /*
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * */
    R<PageResult<SysUserInfos>> getUserInfos(Integer page, Integer pageSize, Date startTime, Date endTime);

    /*
     * 添加用户信息
     * */
    R addUserInfos(SysAddUserReqVo vo);
    /*
     * 获取用户具有的角色信息
     * */
    R<Map<String, List>> getUserRole(Long userId);
    /*
     * 更新用户角色信息
     * */
    R updateUserRoles(SysUpdateUserReqVo vo,Long userId);
}
