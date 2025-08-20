package com.aseubel.weave.common.desensitize;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author Aseubel
 * @date 2025/8/1 下午9:40
 */
public enum DesensitizationTypeEnum {
    // 自定义
    CUSTOM {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return StrUtil.hide(str, startInclude, endExclude);
        }
    },
    // 用户id
    USER_ID {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return String.valueOf(DesensitizedUtil.userId());
        }
    },
    // 手机号
    MOBILE {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return String.valueOf(DesensitizedUtil.mobilePhone(str));
        }
    },
    // 身份证号
    ID_CARD {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return DesensitizedUtil.idCardNum(str, 6, 4);
        }
    },
    // 邮箱
    EMAIL {
        @Override
        public String desensitize(String email, Integer startInclude, Integer endExclude) {
            if (StrUtil.isBlank(email)) {
                return "";
            } else {
                int index = StrUtil.indexOf(email, '@');
                return index <= 1 ? email : StrUtil.hide(email, 3, index);
            }
        }
    },
    // 地址
    ADDRESS {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return DesensitizedUtil.address(str, 3);
        }
    },
    // 姓名
    NAME {
        @Override
        public String desensitize(String str, Integer startInclude, Integer endExclude) {
            return DesensitizedUtil.chineseName(str);
        }
    },
    ;
    public abstract String desensitize(String str, Integer startInclude, Integer endExclude);
}
