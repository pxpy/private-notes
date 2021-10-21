package com.theblind.privatenotes.core.util;


import com.intellij.openapi.project.Project;

import java.io.*;

public class PrivateNotesUtil {

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);) {
            bufferedInputStream.read(bytes);
        }
        return bytes;
    }

    public static void errLog(Throwable throwable, Project project) {
        try (StringWriter stringWriter = new StringWriter();
             PrintWriter writer = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            IdeaApiUtil.showErrNotification(buffer.toString(), project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
