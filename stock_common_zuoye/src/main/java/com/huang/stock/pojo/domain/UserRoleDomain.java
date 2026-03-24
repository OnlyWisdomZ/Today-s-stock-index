package com.huang.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
/*角色信息封装*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDomain {
    /*id*/
    private BigInteger id;
    /*用户角色名称*/
    private String name;
    /*描述*/
    private String description;
    /*状态*/
    private Integer status;
    /*创建时间*/
    private String createTime;
    /*更新时间*/
    private String updateTime;
    /*是否可以删除*/
    private Integer deleted;
}
