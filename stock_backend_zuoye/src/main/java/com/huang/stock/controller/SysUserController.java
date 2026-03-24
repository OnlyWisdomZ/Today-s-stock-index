package com.huang.stock.controller;

import com.huang.stock.pojo.entity.SysUserInfos;
import com.huang.stock.service.SysUserService;
import com.huang.stock.pojo.entity.SysUser;
import com.huang.stock.vo.req.SysAddUserReqVo;
import com.huang.stock.vo.req.SysLoginReqVo;
import com.huang.stock.vo.req.SysUpdateUserReqVo;
import com.huang.stock.vo.resp.PageResult;
import com.huang.stock.vo.resp.R;
import com.huang.stock.vo.resp.SysLoginRespVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name ="用户相关接口处理器")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "根据用户名查询用户信息",responses = {
            @ApiResponse(responseCode = "200",description = "成功获取用户信息",
                    content = @Content(schema = @Schema(implementation = SysUser.class)))
    })//Operation注解 会自动生成日志，供前端查看
    @GetMapping("/user/{userName}")
    public SysUser selectUserByname(@PathVariable("userName") String userName){
        return sysUserService.selectUserByname(userName);
    }

    @Operation(summary = "用户登录功能",responses = {
            @ApiResponse(responseCode = "200",description = "成功登录",
                    content = @Content(schema = @Schema(implementation = SysLoginRespVo.class)))
    })
    @PostMapping("/login")
    public R<SysLoginRespVo> login (@RequestBody SysLoginReqVo vo){
        return sysUserService.login(vo);
    }

    @Operation(summary = "验证码生成",responses = {
            @ApiResponse(responseCode = "200",description = "成功获取验证码",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/captcha")
    public R<Map> getCaptCode(){
        return sysUserService.getCaptCode();
    }

    /*
    * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
    * */
    @PostMapping("/users")
    public R<PageResult<SysUserInfos>> getUserInfos(@RequestParam(name="page",required = false,defaultValue = "1")Integer page,
                                                    @RequestParam(name="pageSize",required = false,defaultValue = "10")Integer pageSize,
                                                    @RequestParam(name="startTime",required = false) Date startTime,
                                                    @RequestParam(name="endTime",required = false)Date endTime){
        return  sysUserService.getUserInfos(page,pageSize,startTime,endTime);
    }
    /*
    * 添加用户信息
    * */
    @PostMapping("/user")
    public R addUserInfos(@RequestBody SysAddUserReqVo vo){
        return sysUserService.addUserInfos(vo);
    }
    /*
    * 获取用户具有的角色信息
    * */
    @GetMapping("/user/roles/{userId}")
    public R<Map<String, List>> getUserRole(@PathVariable Long userId){
        return sysUserService.getUserRole(userId);
    }
    /*
    * 更新用户角色信息
    * */
    @PutMapping("/user/roles")
    public R updateUserRoles(@RequestBody SysUpdateUserReqVo vo,@PathVariable Long userId){
        return  sysUserService.updateUserRoles(vo,userId);
    }
}
