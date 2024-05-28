package com.rockoon.domain.member.controller;

import com.rockoon.domain.member.converter.MemberConverter;
import com.rockoon.domain.member.dto.MemberRequest.MemberModifyDto;
import com.rockoon.domain.member.dto.MemberRequest.MemberRegisterDto;
import com.rockoon.domain.member.dto.MemberResponse;
import com.rockoon.domain.member.entity.Member;
import com.rockoon.domain.member.service.MemberCommandService;
import com.rockoon.domain.member.service.MemberQueryService;
import com.rockoon.global.annotation.api.ApiErrorCodeExample;
import com.rockoon.global.annotation.auth.AuthUser;
import com.rockoon.presentation.payload.code.ErrorStatus;
import com.rockoon.presentation.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원 API")
@ApiResponse(responseCode = "2000", description = "성공")
@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "회원가입", description = "회원정보를 통해 서버 내 회원가입을 진행합니다. " +
            "kakaoEmail, profileImg, nickname, name의 정보를 받습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping
    public ApiResponseDto<Long> registerMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        return ApiResponseDto.onSuccess(memberCommandService.registerMember(memberRegisterDto));
    }

    @Operation(summary = "회원 정보 수정 🔑", description = "로그인한 회원의 정보를 수정합니다. " +
            "profileImg, nickname, name을 변경할 수 있습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PutMapping
    public ApiResponseDto<Long> modifyMemberInfo(@AuthUser Member member,
                                                 @RequestBody MemberModifyDto memberModifyDto) {
        return ApiResponseDto.onSuccess(memberCommandService.modifyMemberInfo(member, memberModifyDto));
    }

    @Operation(summary = "회원 정보 조회 🔑", description = "PK를 통해 사용자의 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @GetMapping("/{memberId}")
    public ApiResponseDto<MemberResponse> getMemberInfo(@PathVariable Long memberId) {
        return ApiResponseDto.onSuccess(
                MemberConverter.toResponse(
                        memberQueryService.getByMemberId(memberId)
                )
        );
    }
}
