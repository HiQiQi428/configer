package org.luncert.configer.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOHelper {

    /**
     * 读输入流
     * @return 内容
     */
    public static byte[] read(InputStream inputStream) throws IOException {
		BufferedInputStream buffer = null;
		DataInputStream dataIn = null;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] bArray = null;
		try {
			buffer = new BufferedInputStream(inputStream);
			dataIn = new DataInputStream(buffer);
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			byte[] buf = new byte[1024];
			while (true) {
				int len = dataIn.read(buf);
				if (len < 0)
					break;
				dos.write(buf, 0, len);
			}
            bArray = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (dataIn != null) dataIn.close();
			if (buffer != null) buffer.close();
			if (bos != null) bos.close();
			if (dos != null) dos.close();
		}
		return bArray;
    }

    /**
     * 读文件
     * @return 文件内容
     */
    public static byte[] read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    /**
     * 存储数据到磁盘
     * @param inputStream 数据输入流
     * @param storePath 目标存储路径
     */
    public static void save(InputStream inputStream, String storePath) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(storePath));
        int len;
        byte[] bs = new byte[1024];
        while ((len = inputStream.read(bs)) != -1) bos.write(bs, 0, len);
        bos.flush();
        bos.close();
    }
    
}