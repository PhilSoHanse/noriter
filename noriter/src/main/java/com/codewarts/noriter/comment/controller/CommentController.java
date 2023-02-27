package com.codewarts.noriter.comment.controller;

import com.codewarts.noriter.comment.dto.comment.CommentCreateRequest;
import com.codewarts.noriter.comment.dto.comment.CommentUpdateRequest;
import com.codewarts.noriter.comment.dto.recomment.ReCommentUpdateRequest;
import com.codewarts.noriter.comment.dto.recomment.ReCommentCreateRequest;
import com.codewarts.noriter.comment.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/{articleId}/comment")
@Validated
@RestController
public class CommentController {

    private final CommentService commentService;
    @PostMapping
    public void registerComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @RequestBody @Valid CommentCreateRequest commentRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        commentService.createComment(memberId, articleId, commentRequest);
    }

    @PutMapping("/{commentId}")
    public void editComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        @RequestBody @Valid CommentUpdateRequest commentRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        commentService.updateComment(memberId, articleId, commentId, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void removeComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        commentService.deleteComment(memberId, articleId, commentId);
    }

    @PostMapping("/{commentId}/recomment")
    public void registerReComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        @RequestBody @Valid ReCommentCreateRequest reCommentCreateRequest,
        HttpServletRequest request) {

        Long memberId = getMemberId(request);
        commentService.createReComment(articleId, commentId, memberId, reCommentCreateRequest);
    }

    @PutMapping("/{commentId}/recomment/{recommentId}")
    public void editReComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        @NotNull(message = "ID가 비어있습니다.")
        @PathVariable(required = false)
        @Positive(message = "대댓글 ID는 양수이어야 합니다.") Long recommentId,
        @RequestBody @Valid ReCommentUpdateRequest reCommentUpdateRequest,
        HttpServletRequest request) {

        Long memberId = getMemberId(request);
        commentService.updateReComment(
            articleId, commentId, recommentId, memberId, reCommentUpdateRequest);
    }

    @DeleteMapping("/{commentId}/recomment/{recommentId}")
    public void removeReComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        @NotNull(message = "ID가 비어있습니다.")
        @PathVariable(required = false)
        @Positive(message = "대댓글 ID는 양수이어야 합니다.") Long recommentId,
        HttpServletRequest request) {

        Long memberId = getMemberId(request);
        commentService.deleteRecomment(articleId, commentId, recommentId, memberId);

    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
