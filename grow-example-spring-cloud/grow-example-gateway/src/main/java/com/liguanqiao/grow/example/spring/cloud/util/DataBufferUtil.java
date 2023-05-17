package com.liguanqiao.grow.example.spring.cloud.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;

import java.nio.charset.Charset;

/**
 * @author liguanqiao
 * @since 2023/5/12
 **/
@UtilityClass
public class DataBufferUtil {

    public static byte[] readBytes(DataBuffer buffer, int length) {
        byte[] content = new byte[length];
        buffer.read(content);
        return content;
    }

    public static String readBytesToString(DataBuffer buffer, int length) {
        return new String(readBytes(buffer, length));
    }

    public static String readBytesToString(DataBuffer buffer, int length, Charset charset) {
        return new String(readBytes(buffer, length), charset);
    }

    public static String readAllBytesToString(DataBuffer buffer) {
        return readBytesToString(buffer, buffer.readableByteCount());
    }

    public static String readAllBytesToString(DataBuffer buffer, Charset charset) {
        return readBytesToString(buffer, buffer.readableByteCount(), charset);
    }

}
