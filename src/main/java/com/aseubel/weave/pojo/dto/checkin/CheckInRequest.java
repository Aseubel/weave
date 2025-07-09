package com.aseubel.weave.pojo.dto.checkin;

import lombok.Data;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 签到请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class CheckInRequest {

    private LocalDate checkDate; // 签到日期（补签时使用）

    @Size(max = 100, message = "签到位置不能超过100个字符")
    private String location; // 签到位置（可选）

    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark; // 备注（可选）

    private Boolean isMakeUp = false; // 是否为补签
}