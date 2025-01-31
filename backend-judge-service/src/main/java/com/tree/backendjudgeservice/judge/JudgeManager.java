package com.tree.backendjudgeservice.judge;


import com.tree.backendjudgeservice.judge.strategy.*;
import com.tree.backendmodel.model.codesandbox.JudgeInfo;
import com.tree.backendmodel.model.entity.QuestionSubmit;
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
        if ("c".equals(language)) {
            judgeStrategy = new CLanguageJudgeStrategy();
        }
        if ("python".equals(language)){
            judgeStrategy = new PythonLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}