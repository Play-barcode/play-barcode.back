package com.bandit.domain.board.entity;

import com.bandit.domain.board.dto.promotion.PromotionRequest;
import com.bandit.domain.member.entity.Member;
import com.bandit.domain.music.entity.PromotionMusic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_promotion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "promotion")
public class Promotion extends Board {
    private int maxAudience;
    private String team;
    private LocalDate showDate;
    private String showTime;
    private String showLocation;
    private String bankName;
    private String account;
    private String accountHolder;
    private String refundInfo;


    @Builder.Default
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PromotionMusic> promotionMusicList = new ArrayList<>();

    public static Promotion of(Member member, PromotionRequest request) {
        return Promotion.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .maxAudience(request.getMaxAudience())
                .team(request.getTeam())
                .writer(member)
                .build();
    }

    public void update(PromotionRequest updateRequest) {
        this.maxAudience = updateRequest.getMaxAudience();
        this.team = updateRequest.getTeam();
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
    }
}
