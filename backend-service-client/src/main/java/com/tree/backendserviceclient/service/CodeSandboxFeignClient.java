package com.tree.backendserviceclient.service;

import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;
import com.tree.backendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 代码沙箱服务接口
 */
@FeignClient(name = "treeoj-code-sandbox", path = "/api/codesandbox/inner")
public interface CodeSandboxFeignClient {

    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest);
}
