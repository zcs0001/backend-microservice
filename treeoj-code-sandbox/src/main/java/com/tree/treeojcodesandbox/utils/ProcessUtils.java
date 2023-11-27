package com.tree.treeojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.tree.backendmodel.model.codesandbox.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序执行进程工具类
 * 如果使用docker的话就用不到了
 */
public class ProcessUtils {

    /**
     * 执行进程并获取进程信息
     *
     * @param runProcess
     * @param processStatus 执行进程状态
     * @return
     * runProcessAndGetMessage 方法：
     * 该方法用于执行一个外部进程（例如编译代码、运行程序等）。
     * 接受一个 Process 对象（外部进程）和一个表示进程状态的字符串（例如 "编译"、"运行"）作为参数。
     * 通过 waitFor() 方法等待进程执行完成，获取进程的退出码（0 表示正常退出）。
     * 如果进程正常退出，将进程的标准输出信息读取并存储在 ExecuteMessage 对象中。
     * 如果进程异常退出，除了标准输出信息外，还获取进程的错误输出信息，并存储在 ExecuteMessage 对象中。
     * 最后，记录进程执行时间，并将相关信息封装在 ExecuteMessage 中返回。
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String processStatus) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待 Process 程序执行完，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);

            // 正常退出
            if (exitValue == 0) {
                System.out.println(processStatus + "成功");
                // 通过进程获取正常输出到控制台的信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));
            } else {
                // 异常退出
                System.out.println(processStatus + "失败，错误码：" + exitValue);
                // 通过进程获取错误输出到控制台的信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));

                // 分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                List<String> errorOutputStrList = new ArrayList<>();
                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutputStrList.add(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, "\n"));
            }
            stopWatch.stop();
            // 将程序执行时间设置进来
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

    /**
     * 交互式执行进程并获取进程信息，也就是用户从键盘输入输入用例
     * runInteractProcessAndGetMessage 方法：
     * 该方法用于执行一个交互式的外部进程，支持向进程传递参数。
     * 接受一个 Process 对象和一个字符串参数作为输入。
     * 通过获取进程的输入流，将参数写入进程的标准输入。
     * 同时，获取进程的标准输出，将其存储在 ExecuteMessage 对象中。
     * 最后，释放相关资源，关闭输入输出流，并销毁进程。
     * @param runProcess
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 从控制台输入参数
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] arguments = args.split(" ");
            String join = StrUtil.join("\n", arguments) + "\n";
            outputStreamWriter.write(join);
            // 回车，发送参数
            outputStreamWriter.flush();

            // 通过进程获取正常输出到控制台的信息
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());
            // 释放资源
            outputStream.close();
            outputStreamWriter.close();
            inputStream.close();
            runProcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
