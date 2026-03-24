package com.huang.stock.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUpdateUserReqVo {
    /*用户id*/
    @Schema(description = "用户id*/")
    private Long userId;
    /*角色id列表*/
    @Schema(description = "角色id列表*/")
    private List roleIds;
}
