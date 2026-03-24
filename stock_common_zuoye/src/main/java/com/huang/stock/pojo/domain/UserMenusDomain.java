package com.huang.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMenusDomain {
    /*权限id*/
    private String id;
    /*权限标题*/
    private String title;
    /*权限图片*/
    private String icon;
    /*请求地址*/
    private String path;
    /*权限名称对应前端vue组件名称*/
    private String name;
    /*子节点*/
    private List<UserMenusDomain> children;
    /*
    * 父节点
    * */
    private UserMenusDomain menus;
    /*权限*/
    private List permissions;
}
