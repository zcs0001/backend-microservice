package com.tree.backendjudgeservice.judge.strategy;


import com.tree.backendmodel.model.codesandbox.JudgeInfo;
import com.tree.backendmodel.model.dto.question.JudgeCase;
import com.tree.backendmodel.model.entity.Question;
import com.tree.backendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
