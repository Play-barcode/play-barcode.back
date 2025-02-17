package com.bandit.domain.ticket.repository;

import com.bandit.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByUuid(String uuid);

    Optional<Ticket> findByPromotionId(Long promotionId);

    void deleteByPromotionId(Long promotionId);
}
