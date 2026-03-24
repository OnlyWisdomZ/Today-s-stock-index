package com.huang.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.huang.stock.pojo.domain.UserMenusDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysLoginRespVo {
    /*
     * 用户ID
     * */
    @JsonSerialize(using = ToStringSerializer.class)//json转化时 把long类型的id转化为String类型
    private long id;
    /*
     * 用户名
     * */
    private String username;
    /*
     * 电话
     * */
    private String phone;
    /*
     * 昵称
     * */
    private String nickName;
    /*
    * 真实名称
    * */
    private String realName;
    /*
    * 性别
    * */
    private Integer sex;
    /*
    * 状态
    * */
    private Integer status;
    /*
    * 邮件
    * */
    private String email;
    /*侧边栏权限树*/
    private List<UserMenusDomain> menus;
}
