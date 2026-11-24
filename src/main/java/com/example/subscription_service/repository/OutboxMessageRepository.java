package com.example.subscription_service.repository;

import com.example.subscription_service.domain.OutboxMessage;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface OutboxMessageRepository
        extends JpaRepository<OutboxMessage, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select message
            from OutboxMessage message
            where message.publishedAt is null
            order by message.createdAt
            """)
    List<OutboxMessage> findUnpublishedForUpdate(Pageable pageable);
}