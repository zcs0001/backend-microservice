package com.tree.backendcommon.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 盐值，混淆密码
     */
    String SALT = "tree";

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";


    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    String NORMAL_STATUS = "正常";
    /**
     * 默认头像
     */
    //String USER_DEFAULT_AVATAR = "https://p3-passport.byteimg.com/img/user-avatar/2ea9106b748a0b88d5bfcf517a4dc2ef~180x180.awebp";
    String USER_DEFAULT_AVATAR = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F8f260db8-84f0-4fc0-8b8c-15bbccac365c%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1697352296&t=bce44a1c683bf9ea6b0cc46d4140c793";
}
