package com.tree.treeojcodesandbox.controller.inner;

import com.tree.backendserviceclient.service.CodeSandboxFeignClient;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;
import com.tree.treeojcodesandbox.template.JavaDockerCodeSandbox;
import com.tree.treeojcodesandbox.template.JavaNativeCodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户内部服务（仅内部调用）
 */
@Slf4j
@RestController
@RequestMapping("/inner")
public class CodeSandboxInnerController implements CodeSandboxFeignClient {
    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;
    @Override
    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数错误");
        }
        log.info("使用代码沙箱");
        // 使用不同的代码沙箱
        return javaDockerCodeSandbox.executeCode(executeCodeRequest);
//        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }
}
