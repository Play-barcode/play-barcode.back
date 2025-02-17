package com.bandit.domain.ticket.service.ticket;

import com.bandit.domain.member.entity.Member;
import com.bandit.domain.ticket.entity.Ticket;

public interface TicketQueryService {

    Ticket findTicketByPromotionId(Long promotionId, Member member);
}
