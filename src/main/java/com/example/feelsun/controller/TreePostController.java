package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.request.TreePostRequest.*;
import com.example.feelsun.response.UserResponse.*;
import com.example.feelsun.service.TreePostService;
import com.example.feelsun.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trees")
public class TreePostController {

    private final UserService userService;
    private final TreePostService treePostService;

    @Operation(summary = "나무와 관련된 인증글 목록을 가져옵니다", description = "나무와 관련된 인증글 목록을 가져옵니다")
    @ApiResponse(responseCode = "200", description = "나무와 관련된 인증글 목록을 가져옵니다",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserTreeDetailResponse.class)))
    @GetMapping("/{treeId}/posts")
    public ResponseEntity<?> getTreePosts(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails,
                                          @PathVariable("treeId") Integer treeId,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size) {
        List<UserTreeDetailResponse> treeDTO = userService.getUserTreeDetail(principalUserDetails, treeId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(treeDTO));
    }

    @Operation(summary = "인증글 이미지를 업로드합니다", description = "인증글 이미지를 업로드합니다")
    @ApiResponse(responseCode = "200", description = "인증글 이미지 업로드 성공")
    @PostMapping(value = "/post-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadTreePostImage(@RequestPart @Valid MultipartFile file, @AuthenticationPrincipal PrincipalUserDetails principalUserDetails) throws IOException {
        String imageUrl = treePostService.uploadTreePostImage(file, principalUserDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(imageUrl));
    }

    @Operation(summary = "인증글을 작성합니다.", description = "인증글을 작성합니다.")
    @ApiResponse(responseCode = "200", description = "인증글 작성 성공")
    @PostMapping("/{treeId}/posts")
    public ResponseEntity<?> createTreePost(@RequestBody @Valid TreePostCreateRequest requestDTO,
                                            @PathVariable("treeId") Integer treeId,
                                            @AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        treePostService.createTreePost(requestDTO, treeId, principalUserDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.successWithNoContent());
    }

}