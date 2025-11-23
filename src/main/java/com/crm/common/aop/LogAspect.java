package com.crm.common.aop;


import com.alibaba.fastjson2.JSON;
import com.crm.common.filter.PropertyPreExcludeFilter;
import com.crm.entity.OperLog;
import com.crm.enums.BusinessStatus;
import com.crm.security.user.ManagerDetail;
import com.crm.security.user.SecurityUser;
import com.crm.service.OperLogService;
import com.crm.utils.IpUtils;
import com.crm.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils; // 添加此行
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;
import org.springframework.http.HttpMethod;


/**
 * @author alani
 */
@Aspect
@Component
@AllArgsConstructor
public class LogAspect {
    //1.需要过滤的敏感参数字段名
    public static final String[] EXCLUDE_PROPERTIES={"password","oldPassword"};
    //2.记录代码执行的过程日志
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    //3.记录操作消耗时间
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    private final OperLogService operLogService;
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(Long.valueOf(System.currentTimeMillis()));
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)",returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(pointcut = "@annotation(controllerLog)",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            ManagerDetail loginManager = SecurityUser.getManager();

            // ******=====数据库日志=====****
            OperLog operLog = new OperLog();
            Integer businessType = (Integer) 0;
            operLog.setStatus(businessType);
            // 请求的地址
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));

            if (loginManager != null) {
                operLog.setOperName(loginManager.getRealName());
                operLog.setManagerId(loginManager.getId().toString());
            }

            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存数据库
            operLogService.recordOperLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
           log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }
    // 暂时未使用，保留方法结构
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperLog operLog, Object jsonResult) throws Exception {
       operLog.setTitle(log.title());
       operLog.setOperType(log.businessType().ordinal());
       if(log.isSaveRequestData()){
           setRequestValue(joinPoint, operLog, log.excludeParamNames());
       }
       if(log.isSaveResponseData()){
           operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult, excludePropertyPreFilter(EXCLUDE_PROPERTIES)), 0, 2000));
       }
    }

    private void setRequestValue(JoinPoint joinPoint, OperLog operLog, String[] excludeParamNames) throws Exception {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        if (paramsMap.isEmpty()
                && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjectUtils.isNotEmpty(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    //2.调用敏感词过滤器，将敏感信息排除
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeProperties) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeProperties));
    }

    //1.定义特殊参数，不需要记录到日志的数据表中
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        // 检查是否是数组
        if (clazz.isArray()) {
            // 判断数组的元素类型是否是MultipartFile或其子类
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            // 只要集合中有一个元素是MultipartFile，就整个过滤掉
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            // 只要Map中有一个值是MultipartFile，就整个过滤掉
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}