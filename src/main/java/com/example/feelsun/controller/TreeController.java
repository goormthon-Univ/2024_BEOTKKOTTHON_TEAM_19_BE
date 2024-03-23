package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.auth.PrincipalUserLoader;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.domain.User;
import com.example.feelsun.request.TreeRequest;
import com.example.feelsun.response.TreeResponse;
import com.example.feelsun.service.TreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
public class TreeController {
    private final TreeService treeService;
    private final PrincipalUserLoader principalUserLoader;

    public TreeController(TreeService treeService, PrincipalUserLoader principalUserLoader) {
        this.treeService = treeService;
        this.principalUserLoader = principalUserLoader;
    }

    @Operation(summary = "홈 화면", description = "로그인한 유저의 나무 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "홈 화면 출력 완료",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TreeResponse.MainTreeList.class)))
    @GetMapping("/")
    public ResponseEntity<?> treeList(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        List<TreeResponse.MainTreeList> treeListDTO = treeService.treeList(principalUserDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(treeListDTO));
    }

    @Operation(summary = "나의 나무 상세 정보", description = "로그인한 유저의 나무 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저의 나무 랜덤 리스트 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TreeResponse.MainTreeList.class)))
    @GetMapping("/{treeId}")

    public ResponseEntity<?> treeDetail(@PathVariable("treeId") Integer treeId) {
        TreeResponse.TreeDetail treeDetailsDTO = treeService.getTreeDetails(treeId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(treeDetailsDTO));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "200", description = "습관 만들기 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TreeResponse.MainTreeList.class)))
    public ResponseEntity<?> createTree(@RequestBody @Valid TreeRequest.TreeCreateRequest requestDTO) {
        User user = principalUserLoader.getRequestUser();
        treeService.createTree(requestDTO, user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(requestDTO));
    }

    @PostMapping("/continuousperiod")
    @ApiResponse(responseCode = "200", description = "최장 연속 성장기간입니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TreeResponse.MainTreeList.class)))
    public ResponseEntity<?> UserContinuousPeriod(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        List<TreeResponse.MainTreeList> treeListDTO = treeService.treeList(principalUserDetails);
        TreeResponse.UserContinuousPeriod UserContinuousPeriodDTO = treeService.getUserContinuousPeriod(treeListDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(UserContinuousPeriodDTO));
    }

    @DeleteMapping("/delete/{treeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTree(@PathVariable Integer treeId) {
        treeService.deleteTree(treeId);
    }
}