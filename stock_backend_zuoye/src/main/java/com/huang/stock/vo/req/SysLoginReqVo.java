package com.huang.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysLoginReqVo {
    /*
     * 用户名
     * */
    private String username;
    /*
     * 密码
     * */
    private String password;
    /*
     * 验证码
     * */
    private String code;
    /*
     * 会话ID
     * */
    private String sessionId;
}
