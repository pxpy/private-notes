package com.theblind.privatenotes.core.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrivateNotesUtil {

    public static byte[] getBytes(InputStream inputStream) throws IOException {
      byte[] bytes=new byte[inputStream.available()];
      try(BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);) {
          bufferedInputStream.read(bytes);
      }
      return bytes;
    }

}
