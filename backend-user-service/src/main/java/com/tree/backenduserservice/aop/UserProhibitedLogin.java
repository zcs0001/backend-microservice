package com.tree.backenduserservice.aop;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.backendcommon.annotation.AuthCheck;
import com.tree.backendcommon.annotation.LoginCheck;
import com.tree.backendcommon.common.ErrorCode;
import com.tree.backendcommon.exception.BusinessException;
import com.tree.backendmodel.model.dto.user.UserLoginRequest;
import com.tree.backendmodel.model.entity.User;
import com.tree.backendmodel.model.enums.UserRoleEnum;
import com.tree.backendmodel.model.enums.UserStatusEnum;
import com.tree.backenduserservice.mapper.UserMapper;
import com.tree.backenduserservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.tree.backendcommon.constant.UserConstant.SALT;

/**
 * @Aspect: 声明这是一个切面类。
 * 在Spring AOP中，@Around 注解用于定义环绕通知（Around Advice），而 @Aspect 注解用于标识一个类为切面类。
 * 切面类（Aspect）是一个包含 切点 和 通知 的类，而通知可以是前置通知（@Before）、后置通知（@After）、环绕通知（@Around）等。
 * @Around 注解用于定义环绕通知，它能够在目标方法执行前后都进行处理，包括决定是否继续执行目标方法。
 * @Component: 将该类标记为 Spring 的组件，使其能够被自动扫描并纳入 Spring 容器管
 * 这个拦截器主要用于在方法执行前进行权限校验，确保调用该方法的用户具备指定的权限。
 * 这是通过在 AOP 切面中使用 @Around 注解实现的，拦截了带有 @AuthCheck 注解的方法，并在执行前进行了权限检查。
 */

/**
 * 权限校验 AOP
 */
@Aspect
@Component
public class UserProhibitedLogin {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     * 执行拦截
     * @Around("@annotation(authCheck)"): 通过 @Around 注解声明一个环绕通知，拦截带有 @AuthCheck 注解的方法。
     * @annotation(authCheck) 表示匹配带有 AuthCheck 注解的方法。
     * ProceedingJoinPoint joinPoint: 代表被拦截的方法。
     * AuthCheck authCheck: 通过方法参数获取 @AuthCheck 注解的实例，以便获取其中的权限信息。
     *
     * 从当前请求的上下文中获取 HttpServletRequest 对象。
     * 通过 userService.getLoginUser(request) 获取当前登录用户的信息。
     * 获取 AuthCheck 注解中定义的必须具备的角色 mustRole。
     * 如果 mustRole 不为空，说明该方法需要进行权限校验，根据用户的角色进行校验。
     * 如果用户角色不满足要求，抛出 BusinessException 异常，表示权限不足，返回错误码 ErrorCode.NO_AUTH_ERROR。
     * 如果权限校验通过，调用 joinPoint.proceed() 继续执行被拦截的方法。
     */
    @Around("@annotation(loginCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        String userAccount = "";
        String userPassword = "";
        String mustStatus = loginCheck.mustStatus();
        // 获取方法参数
        Object[] userLoginArgs = joinPoint.getArgs();
        // 遍历方法参数，找到UserLoginRequest对象
        for (Object arg : userLoginArgs) {
            if (arg instanceof UserLoginRequest) {
                UserLoginRequest userLoginRequest = (UserLoginRequest) arg;
                userAccount = userLoginRequest.getUserAccount();
                userPassword = userLoginRequest.getUserPassword();
            }
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 判断用户状态
        String userStatus = user.getUserStatus();
        UserStatusEnum userStatusEnum = UserStatusEnum.getEnumByText(userStatus);
        if (userStatusEnum != null) {
            if (!StringUtils.equals(userStatus, mustStatus)) {
                if (UserStatusEnum.WRITE_OFF.equals(userStatusEnum)) {
                    throw new BusinessException(ErrorCode.WRITE_OFF_ERROR);
                }
                if (UserStatusEnum.BAN.equals(userStatusEnum)) {
                    throw new BusinessException(ErrorCode.BAN_ERROR);
                }
            }
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

