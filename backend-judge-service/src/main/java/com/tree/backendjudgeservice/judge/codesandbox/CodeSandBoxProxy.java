package com.tree.backendjudgeservice.judge.codesandbox;


import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理代码沙箱接口
 */
@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;

    /**
     * 代码沙箱执行代码接口
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
