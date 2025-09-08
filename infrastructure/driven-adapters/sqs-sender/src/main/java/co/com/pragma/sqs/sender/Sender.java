package co.com.pragma.sqs.sender;

import co.com.pragma.model.application.MessageBody;
import co.com.pragma.model.application.gateways.SenderGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class Sender implements SenderGateway {

  private final SQSSenderProperties properties;
  private final SqsAsyncClient client;

  public Mono<String> send(MessageBody message) {
    return Mono
      .fromCallable(() -> buildRequest(new Gson().toJson(message)))
      .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
      .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
      .map(SendMessageResponse::messageId);
  }

  private SendMessageRequest buildRequest(String message) {
    return SendMessageRequest.builder().queueUrl(properties.queueUrl()).messageBody(message).build();
  }

}
