package com.bandit.domain.like.controller;

import com.bandit.domain.like.service.like_music.LikeMusicCommandService;
import com.bandit.domain.like.service.like_music.LikeMusicQueryService;
import com.bandit.domain.member.entity.Member;
import com.bandit.global.annotation.api.ApiErrorCodeExample;
import com.bandit.global.annotation.auth.AuthUser;
import com.bandit.presentation.payload.code.ErrorStatus;
import com.bandit.presentation.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LikeMusic API", description = "노래 좋아요 API")
@ApiResponse(responseCode = "2000", description = "성공")
@RequestMapping("/api/likes/music")
@RequiredArgsConstructor
@RestController
public class LikeMusicApiController {
    private final LikeMusicCommandService likeMusicCommandService;
    private final LikeMusicQueryService likeMusicQueryService;

    @Operation(summary = "셑리스트 좋아요 🔑", description = "로그인한 회원이 프로모션 내 셑리스트에 좋아요를 누릅니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PostMapping("/{promotionMusicId}")
    public ApiResponseDto<Long> likeSetList(@AuthUser Member member,
                                            @PathVariable Long promotionMusicId) {
        Long likeId = likeMusicCommandService.likeMusic(promotionMusicId, member);
        return ApiResponseDto.onSuccess(likeId);
    }

    @Operation(summary = "셑리스트 좋아요 취소 🔑", description = "로그인한 회원이 프로모션 내 셑리스트의 좋아요를 취소합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.LIKE_NOT_FOUND
    })
    @DeleteMapping("/{promotionMusicId}")
    public ApiResponseDto<Boolean> unlikeSetList(@AuthUser Member member,
                                              @PathVariable Long promotionMusicId) {
        likeMusicCommandService.unlikeMusic(promotionMusicId, member);
        return ApiResponseDto.onSuccess(true);
    }

    @Operation(summary = "셑리스트 좋아요 확인 🔑", description = "로그인한 회원이 좋아요한 프로모션 내 셑리스트를 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.LIKE_NOT_FOUND
    })
    @GetMapping("/{promotionMusicId}")
    public ApiResponseDto<Boolean> checkIsLiked(@AuthUser Member member,
                                                 @PathVariable Long promotionMusicId) {
        return ApiResponseDto.onSuccess(likeMusicQueryService.isLiked(promotionMusicId, member));
    }

    @Operation(summary = "셑리스트 좋아요 확인", description = "셑리스트의 좋아요 개수를 확인합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.LIKE_NOT_FOUND
    })
    @GetMapping("/{promotionMusicId}/count")
    public ApiResponseDto<Long> countLike(@PathVariable Long promotionMusicId) {
        return ApiResponseDto.onSuccess(likeMusicQueryService.countLike(promotionMusicId));
    }
}
