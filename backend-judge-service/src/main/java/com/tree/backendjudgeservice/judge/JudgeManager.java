package com.tree.backendjudgeservice.judge;


import com.tree.backendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.tree.backendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.tree.backendjudgeservice.judge.strategy.JudgeContext;
import com.tree.backendjudgeservice.judge.strategy.JudgeStrategy;
import com.tree.treeojbackendmodel.model.codesandbox.JudgeInfo;
import com.tree.treeojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getSubmitLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}