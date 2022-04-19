package com.theblind.privatenotes.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * 只做简单的git操作
 * 通过Process 实现
 */
public class GitUtil {


    public static void commitAndPush(String directory) throws Exception {
        if (!addAll(directory)) {
            return;
        }
        if (!commit(directory)) {
            return;
        }
        if (push(directory)) {
            PrivateNotesUtil.infoLog("commitAndPush 执行成功");
        }
    }


    private static boolean commit(String directory) throws Exception {
        List<String> command = new ArrayList<>(3);
        command.add("git");
        command.add("commit");
        command.add("-m");
        command.add(" private notes 添加");
        boolean executeCmd = executeCmd(directory, command, (errMsg) -> {
            PrivateNotesUtil.errLog("commit  errMsg: " + errMsg);
        });
        return executeCmd;
    }


    private static boolean push(String directory) throws Exception {
        List<String> command = new ArrayList<>(2);
        command.add("git");
        command.add("push");
        boolean executeCmd = executeCmd(directory, command, (errMsg) -> {
            //pushEverything up-to-date
            PrivateNotesUtil.errLog("push errMsg: " + errMsg);
        });
        return executeCmd;
    }

    private static boolean addAll(String directory) throws Exception {
        List<String> command = new ArrayList<>(3);
        command.add("git");
        command.add("add");
        command.add(".");
        boolean executeCmd = executeCmd(directory, command, (errMsg) -> {
            System.out.println("addall" + errMsg);
            PrivateNotesUtil.errLog("addAll  errMsg: " + errMsg);
        });
        return executeCmd;
    }


    public static boolean pull(String directory) throws Exception {
        List<String> command = new ArrayList<>(2);
        command.add("git");
        command.add("pull");
        boolean executeCmd = executeCmd(directory, command, (errMsg) -> {
            if (errMsg.startsWith("fatal") && errMsg.endsWith(".git\n")) {
                PrivateNotesUtil.errLog("pull 执行失败,没有找到git repository,请先将存储在用户目录下的.PrivateNotes 文件导入到Idea中完成Git仓库创建 \n或者 使用Git命令创建仓库\n详细操作: https://gitee.com/Lovxy/private-notes");
            } else {
                PrivateNotesUtil.errLog("pull errMsg: " + errMsg);
            }
        });
        if (executeCmd) {
            PrivateNotesUtil.infoLog("pull 执行成功");
        }
        return executeCmd;
    }

    private static boolean executeCmd(String directory, List<String> cmd, Consumer<String> errHandle) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // setting up a working directory
        processBuilder.directory(new File(directory));
        processBuilder.command(cmd);
        Process process = processBuilder.start();
        process.waitFor();

        String errMsg = getUtf8String(IoUtil.readBytes(process.getErrorStream()));
        if (StrUtil.isNotEmpty(errMsg)) {
            errHandle.accept(errMsg);
            return false;
        }
        return true;
    }


    public static String getUtf8String(byte[] bytes) throws UnsupportedEncodingException {

        String encode = "GBK";
        boolean pdUtf = false;
        pdUtf = validUtf8(bytes);
        if (pdUtf) {
            encode = String.valueOf(StandardCharsets.UTF_8);
        }
        return new String(bytes, encode);

    }

    public static boolean validUtf8(byte[] data) {
        int i = 0;
        int count = 0;
        while (i < data.length) {
            int v = data[i];
            if (count == 0) {
                if ((v & 240) == 240 && (v & 248) == 240) {
                    count = 3;
                } else if (((v & 224) == 224) && (v & 240) == 224) {
                    count = 2;
                } else if ((v & 192) == 192 && (v & 224) == 192) {
                    count = 1;
                } else if ((v | 127) == 127) {
                    count = 0;
                } else {
                    return false;
                }
            } else {
                if ((v & 128) == 128 && (v & 192) == 128) {
                    count--;
                } else {
                    return false;
                }
            }

            i++;
        }

        return count == 0;
    }


    public static void main(String[] args) throws Exception {

        String directory = "C:\\Users\\NINGMEI\\.privateNotes";
        GitUtil.commitAndPush(directory);

    }

}
