package com.tree.backenduserservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.treeojbackendmodel.model.entity.UserCode;

public interface UserCodeService extends IService<UserCode> {

    /**
     * 查看用户有无调用次数
     * @param userId
     * @return
     */
    UserCode getUserCodeByUserId(long userId);
}
