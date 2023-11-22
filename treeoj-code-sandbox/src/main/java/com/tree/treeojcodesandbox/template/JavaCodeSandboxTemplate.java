package com.tree.treeojcodesandbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.tree.treeojcodesandbox.CodeSandBox;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeRequest;
import com.tree.backendmodel.model.codesandbox.ExecuteCodeResponse;
import com.tree.treeojcodesandbox.model.ExecuteMessage;
import com.tree.backendmodel.model.codesandbox.JudgeInfo;
import com.tree.treeojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 代码沙箱模板父类
 * 此处的抽象类abstract是为了强调它是一个模板类，子类需要根据具体情况进行扩展，因为此类里并没有抽象方法，子类也不必去实现抽象方法
 * 只是为了被其他类继承并提供一些通用的结构和行为
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandBox {

    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";

    public static final String GLOBAL_CODE_CLASS_NAME = "Main.java";

    public static final long TIME_OUT = 10000L;

    private static final List<String> blackList = Arrays.asList("Files", "exec");

    private static final String SECURITY_MANAGER_PATH = "C:\\code\\treeoj-code-sandbox\\src\\main\\resounces\\security";

    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    // 字典树，可以用更少的空间存储更多的敏感词汇，实现更高效的敏感词查找
    private static final WordTree WORD_TREE;

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blackList);
    }

    /**
     * 完整的流程
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("开始运行代码");
        // 开启权限管理器
//        System.setSecurityManager(new MySecurityManager());

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 1、将用户提交的代码保存为文件。
        File userCodeFile = saveCodeToFile(code);
        System.out.println("========"+userCodeFile);
        // 2、编译代码，得到class文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        log.info("编译后信息：{}", compileFileExecuteMessage);

        // 3、执行程序
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);

        // 4、整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList);

        // 5、文件清理
        boolean del = clearFile(userCodeFile);
        if (!del) {
            log.error("deleteFile Field ,userCodeFilePath = {}", userCodeFile.getAbsoluteFile());
        }

        return outputResponse;
    }

    /**
     * 1、将用户提交的代码保存为文件。
     *
     * @param code
     * @return
     */
    public File saveCodeToFile(String code) {

        // 校验代码中是否包含黑名单中的命令
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            System.out.println("包含禁止词：" + foundWord.getFoundWord());
            return null;
        }

        // 获取用户工作文件路径
        String userDir = System.getProperty("user.dir");
        //  File.separator 区分不同系统的分隔符：\\ or /
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局目录路径是否存在
        if (!FileUtil.exist(globalCodePathName)) {
            // 不存在，则创建文件目录
            FileUtil.mkdir(globalCodePathName);
        }
        // 存在，则保存用户提交代码
        // 把用户的代码隔离存放，
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        // 实际存放文件的目录
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 2、编译代码，得到class文件
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            // 直接返回结果
            return ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3、执行程序文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            // 限制资源的分配
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            // TODO 使用Docker时区分win和Linux的写法 win：%s;%s Linux：%s:%s
            // String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=% s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 创建守护线程，用于超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("程序运行超时，已经中断");
                        runProcess.destroy();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                // ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, inputArgs);
                System.out.println("代码程序执行信息：" + executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("程序执行错误" + e);
            }
        }
        return executeMessageList;
    }

    /**
     * 4、获取响应输出结果
     *
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 执行错误
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交程序执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long executeTime = executeMessage.getTime();
            if (executeTime != null) {
                maxTime = Math.max(maxTime, executeTime);
            }
        }
        // 正常执行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        // TODO 内存判断,需要借助第三方库获取内存占用，非常麻烦
        // judgeInfo.setMemory();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }


    /**
     * 5、清理文件
     *
     * @param userCodeFile
     * @return
     */
    public boolean clearFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            log.info("删除" + (del ? "成功！" : "失败！"));
            return del;
        }
        return true;
    }

    /**
     * 6、获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 代码沙箱错误，编译错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
