package com.tree.backenduserservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.backenduserservice.mapper.UserCodeMapper;
import com.tree.backenduserservice.service.UserCodeService;
import com.tree.backendcommon.common.ErrorCode;
import com.tree.backendcommon.exception.BusinessException;
import com.tree.backendcommon.exception.ThrowUtils;
import com.tree.backendmodel.model.entity.UserCode;
import org.springframework.stereotype.Service;

@Service
public class UserCodeServiceImpl extends ServiceImpl<UserCodeMapper, UserCode>
        implements UserCodeService {

    @Override
    public UserCode getUserCodeByUserId(long userId) {
        if (userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserCode> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        UserCode userCode = this.getOne(wrapper);
        ThrowUtils.throwIf(userCode == null, ErrorCode.NULL_ERROR, "此用户不存在");
        return userCode;
    }
}




