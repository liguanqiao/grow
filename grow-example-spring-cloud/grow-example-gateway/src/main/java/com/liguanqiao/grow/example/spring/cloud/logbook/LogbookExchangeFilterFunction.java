package com.liguanqiao.grow.example.spring.cloud.logbook;

import lombok.RequiredArgsConstructor;
import org.apiguardian.api.API;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.zalando.logbook.Logbook;
import reactor.core.publisher.Mono;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.zalando.fauxpas.FauxPas.*;

@RequiredArgsConstructor
@API(status = EXPERIMENTAL)
@SuppressWarnings({"BlockingMethodInNonBlockingContext", "NullableProblems"})
public class LogbookExchangeFilterFunction implements ExchangeFilterFunction {

    private final Logbook logbook;

    @Override
    public Mono<org.springframework.web.reactive.function.client.ClientResponse> filter(org.springframework.web.reactive.function.client.ClientRequest request, ExchangeFunction next) {
        ClientRequest clientRequest = new ClientRequest(request);
        Logbook.RequestWritingStage requestWritingStage = throwingSupplier(() -> logbook.process(clientRequest)).get();

        return next
                .exchange(org.springframework.web.reactive.function.client.ClientRequest
                        .from(request)
                        .body((outputMessage, context) -> request.body().insert(new BufferingClientHttpRequest(outputMessage, clientRequest), context))
                        .build()
                )
                .flatMap(throwingFunction(response -> {
                    Logbook.ResponseProcessingStage responseProcessingStage = requestWritingStage.write();

                    ClientResponse clientResponse = new ClientResponse(response);
                    Logbook.ResponseWritingStage responseWritingStage = responseProcessingStage.process(clientResponse);

                    return Mono
                            .just(response)
                            .flatMap(it -> {
                                if (clientResponse.shouldBuffer() && response.headers().contentLength().orElse(0) > 0) {
                                    return it
                                            .bodyToMono(byte[].class)
                                            .doOnNext(clientResponse::buffer)
                                            .map(b -> org.springframework.web.reactive.function.client.ClientResponse.from(response)
                                                    .body(new String(b, clientResponse.getCharset())).build());
                                } else {
                                    return Mono.just(it);
                                }
                            })
                            .doOnNext(throwingConsumer(b -> responseWritingStage.write()));
                }));
    }
}
