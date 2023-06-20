package com.liguanqiao.grow.example.spring.cloud.logbook;

import lombok.experimental.UtilityClass;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import java.util.function.Consumer;

@UtilityClass
class DataBufferCopyUtils {

    public static final DefaultDataBufferFactory sharedInstance = new DefaultDataBufferFactory();

    Publisher<? extends DataBuffer> wrapAndBuffer(Publisher<? extends DataBuffer> body, Consumer<byte[]> copyConsumer) {
        return DataBufferUtils
                .join(body)
                .defaultIfEmpty(sharedInstance.wrap(new byte[0]))
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    DefaultDataBuffer wrappedDataBuffer = sharedInstance.wrap(bytes);
                    copyConsumer.accept(bytes);
                    return wrappedDataBuffer;
                });
    }
}
