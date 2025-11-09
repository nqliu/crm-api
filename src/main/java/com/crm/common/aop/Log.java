package com.crm.common.aop;

import com.crm.enums.BusinessType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String title() default "";
    BusinessType businessType() default BusinessType.OTHER;
    boolean isSaveRequestData() default true;
    boolean isSaveResponseData() default true;
    String[] excludeParamNames() default {};

}
