package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.errors.exception.Exception400;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.request.UserRequest.*;
import com.example.feelsun.response.UserResponse.*;
import com.example.feelsun.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginResponseWithToken.class)))
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserSignUpRequest requestDTO, Errors errors) {
        userService.signup(requestDTO);

        UserLoginResponseWithToken signupDTO = userService.generateToken(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(signupDTO));
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserLoginResponseWithToken.class)))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest requestDTO, Errors errors) {
        UserLoginResponseWithToken loginDTO = userService.login(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(loginDTO));
    }

    @Operation(summary = "유저 아이디 중복 체크", description = "유저 아이디 중복 체크를 진행합니다.")
    @ApiResponse(responseCode = "200", description = "사용 가능한 아이디입니다.")
    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestBody @Valid UserCheckUsernameRequest requestDTO, Errors errors) {
        boolean isExists = userService.checkUsername(requestDTO);

        if (isExists) {
            throw new Exception400(null, "이미 사용중인 아이디입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success("사용 가능한 아이디입니다."));
    }

    @Operation(summary = "유저 닉네임 중복 체크", description = "유저 닉네임 중복 체크를 진행합니다.")
    @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임입니다.")
    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody @Valid UserCheckNicknameRequest requestDTO, Errors errors) {
        boolean isExists = userService.checkNickname(requestDTO);

        if (isExists) {
            throw new Exception400(null, "이미 사용중인 닉네임입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success("사용 가능한 닉네임입니다."));
    }

    @Operation(summary = "둘러보기 페이지에서 유저의 나무 랜덤 리스트 조회", description = "둘러보기 페이지에서 유저의 나무 랜덤 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저의 나무 랜덤 리스트 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTreeListResponse.class)))
    @GetMapping("/tree-list")
    public ResponseEntity<?> getUserTreeList(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "5") int size) {
        List<UserTreeListResponse> treeListDTO = userService.getUserTreeList(principalUserDetails, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(treeListDTO));
    }

    @Operation(summary = "redis 안에 저장된 키 삭제", description = "redis 안에 저장된 키를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "키 삭제 성공")
    @DeleteMapping("/redis-keys")
    public ResponseEntity<?> deleteKeys(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        userService.deleteKeys(principalUserDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.successWithNoContent());
    }

    @Operation(summary = "유저의 히스토리 인증글 목록 조회", description = "유저의 히스토리 인증글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "인증글 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserHistoryListResponse.class)))
    @GetMapping("/histories")
    public ResponseEntity<?> getUserHistories(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "5") int size) {
        List<UserHistoryListResponse> historyListDTO = userService.getUserHistories(principalUserDetails, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(historyListDTO));
    }

    @Operation(summary = "유저의 공유 정보 조회", description = "유저의 공유 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저의 공유 정보 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserShareResponse.class)))
    @GetMapping("/{userId}/share")
    public ResponseEntity<?> getUserShare(@PathVariable("userId") Integer userId) {
        UserShareResponse shareDTO = userService.getUserShare(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(shareDTO));
    }


}
