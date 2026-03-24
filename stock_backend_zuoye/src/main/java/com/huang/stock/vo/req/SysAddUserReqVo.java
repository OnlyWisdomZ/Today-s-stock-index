package com.huang.stock.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAddUserReqVo {
    /**
     * 账户
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 用户密码密文
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 手机号码
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 邮箱(唯一)
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickName;
    /**
     * 真实名称
     */
    @Schema(description = "真实名称")
    private String realName;
    /**
     * 性别(1.男 2.女)
     */
    @Schema(description = "性别")
    private Integer sex;
    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    @Schema(description = "创建来源")
    private Integer createWhere;
    /**
     * 账户状态(1.正常 2.锁定 )
     */
    @Schema(description = "账户状态")
    private Integer status;

}
