package com.tree.treeojcodesandbox.template;



import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import org.springframework.stereotype.Component;

/**
 * 原生Java代码沙箱 - 实现模板方法
 *
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {


    /**
     * 执行程序
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
