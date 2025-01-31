package com.tree.backendmodel.model.dto.questionsumbit;


import com.tree.backendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询题目提交
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String submitLanguage;

    /**
     * 提交状态
     */
    private Integer submitStatus;


    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    private static final long serialVersionUID = 1L;
}