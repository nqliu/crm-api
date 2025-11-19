package com.crm.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.crm.common.exception.ServerException;

/**
 * 校验工具类
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class AssertUtils {

    // 现有方法：校验字符串为空或空白
    public static void isBlank(String str, String variable) {
        if (StrUtil.isBlank(str)) {
            throw new ServerException(variable + "不能为空");
        }
    }

    // 新增方法：校验字符串不为空且非空白（与isBlank反向）
    public static void notBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new ServerException(message);
        }
    }

    // 现有方法：校验对象为null
    public static void isNull(Object object, String variable) {
        if (object == null) {
            throw new ServerException(variable + "不能为空");
        }
    }

    // 新增方法：校验对象不为null（与isNull反向）
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ServerException(message);
        }
    }

    // 现有方法：校验数组为空
    public static void isArrayEmpty(Object[] array, String variable) {
        if (ArrayUtil.isEmpty(array)) {
            throw new ServerException(variable + "不能为空");
        }
    }

    // 新增方法：校验数组不为空（与isArrayEmpty反向）
    public static void notArrayEmpty(Object[] array, String message) {
        if (ArrayUtil.isEmpty(array)) {
            throw new ServerException(message);
        }
    }
}
