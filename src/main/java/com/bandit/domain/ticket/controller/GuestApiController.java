package com.bandit.domain.ticket.controller;

import com.bandit.domain.member.entity.Member;
import com.bandit.domain.ticket.dto.guest.GuestRequest;
import com.bandit.domain.ticket.entity.Guest;
import com.bandit.domain.ticket.service.guest.GuestCommandService;
import com.bandit.domain.ticket.service.guest.GuestQueryService;
import com.bandit.global.annotation.api.ApiErrorCodeExample;
import com.bandit.global.annotation.auth.AuthUser;
import com.bandit.presentation.payload.code.ErrorStatus;
import com.bandit.presentation.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Guest API", description = "게스트 관련 API")
@ApiResponse(responseCode = "2000", description = "성공")
@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestApiController {

    private final GuestCommandService guestCommandService;
    private final GuestQueryService guestQueryService;

    @Operation(summary = "게스트 생성 🔑", description = "프로모션 ID와 멤버 정보, 이름을 받아 새로운 게스트를 생성합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @PostMapping("{promotionId}")
    public ApiResponseDto<Long> createGuest(
            @PathVariable Long promotionId,
            @RequestBody GuestRequest request,
            @AuthUser Member member) {
        Long guestId = guestCommandService.createGuest(promotionId, member, request);
        return ApiResponseDto.onSuccess(guestId);
    }

    @Operation(summary = "게스트 수정 🔑", description = "게스트 ID와 게스트 정보를 받아 기존 게스트 정보를 수정합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @PutMapping("/{guestId}")
    public ApiResponseDto<Long> updateGuest(
            @PathVariable Long guestId,
            @AuthUser Member member,
            @RequestBody @Valid GuestRequest request) {
        return ApiResponseDto.onSuccess(guestCommandService.updateGuest(guestId, member, request));
    }

    @Operation(summary = "게스트 삭제 🔑", description = "게스트 ID를 받아 해당 게스트를 삭제합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @DeleteMapping("/{guestId}")
    public ApiResponseDto<Boolean> deleteGuest(@PathVariable Long guestId,
                                            @AuthUser Member member) {
        guestCommandService.deleteGuest(guestId, member);
        return ApiResponseDto.onSuccess(true);
    }

    @Operation(summary = "모든 게스트 조회", description = "모든 게스트 정보를 조회합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping
    public ApiResponseDto<List<Guest>> getAllGuests() {
        List<Guest> guests = guestQueryService.findAllGuests();
        return ApiResponseDto.onSuccess(guests);
    }

    @Operation(summary = "게스트 조회", description = "게스트 ID를 받아 해당 게스트 정보를 조회합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/{guestId}")
    public ApiResponseDto<Guest> getGuestById(@PathVariable Long guestId) {
        Guest guest = guestQueryService.findGuestById(guestId);
        return ApiResponseDto.onSuccess(guest);
    }

    @Operation(summary = "프로모션 ID로 게스트 조회", description = "프로모션 ID를 받아 해당 프로모션에 속한 모든 게스트 정보를 조회합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/promotions/{promotionId}")
    public ApiResponseDto<List<Guest>> getGuestsByPromotionId(@PathVariable Long promotionId) {
        List<Guest> guests = guestQueryService.findGuestsByPromotionId(promotionId);
        return ApiResponseDto.onSuccess(guests);
    }

    @Operation(summary = "프로모션 ID로 게스트 페이징 조회", description = "프로모션 ID를 받아 해당 프로모션에 속한 게스트 정보를 페이지별로 조회합니다.")
    @ApiErrorCodeExample(
            {ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/promotions/{promotionId}/page")
    public ApiResponseDto<Page<Guest>> getGuestsByPromotionIdPaged(
            @PathVariable Long promotionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Guest> guestPage = guestQueryService.findGuestsByPromotionId(promotionId, pageable);
        return ApiResponseDto.onSuccess(guestPage);
    }
}