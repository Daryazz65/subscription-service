package com.example.subscription_service.messaging;

import com.example.subscription_service.config.AppProperties;
import com.example.subscription_service.domain.OutboxMessage;
import com.example.subscription_service.exception.OutboxPublishingException;
import com.example.subscription_service.repository.OutboxMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxMessageRepository outboxMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AppProperties properties;

    @Scheduled(fixedDelayString = "${app.outbox.fixed-delay}")
    @SchedulerLock(
            name = "subscription-outbox-poller",
            lockAtLeastFor = "PT1S",
            lockAtMostFor = "PT30S"
    )
    @Transactional
    public void publish() {
        List<OutboxMessage> messages =
                outboxMessageRepository.findUnpublishedForUpdate(
                        PageRequest.of(
                                0,
                                properties.outbox().batchSize()
                        )
                );

        for (OutboxMessage message : messages) {
            publishMessage(message);
        }
    }

    private void publishMessage(OutboxMessage message) {
        try {
            kafkaTemplate.send(
                    message.getTopic(),
                    message.getId().toString(),
                    message.getPayload()
            ).get();

            message.markPublished();
        } catch (Exception exception) {
            throw new OutboxPublishingException(
                    "Cannot publish outbox message " + message.getId(),
                    exception
            );
        }
    }
}