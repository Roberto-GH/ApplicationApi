package co.com.pragma.sqs.sender;

import co.com.pragma.model.application.enums.QueueAlias;
import co.com.pragma.model.application.gateways.SenderGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
public class Sender implements SenderGateway {

  private final SQSSenderProperties properties;
  private final SqsAsyncClient client;
  private static final Gson gson = new Gson();

  public Sender(SQSSenderProperties properties, SqsAsyncClient client) {
    this.properties = properties;
    this.client = client;
  }

  @Override
  public <T> Mono<String> send(T messageObject, QueueAlias destination) {
    String queueAlias = destination.getAlias();
    String queueUrl = properties.queues().get(queueAlias);
    String jsonMessage = gson.toJson(messageObject);
    if (queueUrl == null) {
      return Mono.error(new IllegalArgumentException("SQS queue alias not configured: " + queueAlias));
    }
    return Mono
      .fromCallable(() -> buildRequest(jsonMessage, queueUrl))
      .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
      .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
      .map(SendMessageResponse::messageId);
  }

  private SendMessageRequest buildRequest(String message, String queueUrl) {
    return SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message).build();
  }

}
