package com.tree.backendquestionservice.controller.inner;

import com.tree.backendmodel.model.entity.Question;
import com.tree.backendmodel.model.entity.QuestionSubmit;
import com.tree.backendquestionservice.service.QuestionService;
import com.tree.backendquestionservice.service.QuestionSubmitService;
import com.tree.backendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户内部服务（仅内部调用）
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据 id 获取题目信息
     *
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据 id 获取到提交题目信息
     *
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 根据id 更新提交题目信息
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    /**
     * 保存数据
     *
     * @param question
     * @return
     */
    @PostMapping("/question/save")
    @Override
    public boolean updateQuestion(@RequestBody Question question) {
        return questionService.updateById(question);
    }
}
