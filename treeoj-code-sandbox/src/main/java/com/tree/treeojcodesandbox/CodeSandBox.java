package com.tree.treeojcodesandbox;


import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 */
public interface CodeSandBox {

    /**
     * 代码沙箱执行代码接口
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
