package com.codewarts.noriter.article.docs.recomment;

import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile({"test"})
@Sql("classpath:/data.sql")
public class ReCommentCreateTest {

    @LocalServerPort
    int port;

    @Autowired
    protected JwtProvider jwtProvider;

    protected RequestSpecification documentationSpec;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    void 대댓을_등록한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 필드값이_비어있을_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "잘 보셨다니 다행입니다.");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()))
            .body("detail.secret", equalTo("내용은 필수입니다."));
    }

    @Test
    void articleId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", " ")
            .pathParam("commentId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.articleId: ID가 비어있습니다."));
    }

    @Test
    void articleId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", -1)
            .pathParam("commentId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.articleId: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void commentId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", " ")
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.commentId: ID가 비어있습니다."));
    }

    @Test
    void commentId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", -1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.commentId: 댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void 삭제된_게시글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 13)
            .pathParam("commentId", 6)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 삭제된_댓글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 10)
            .pathParam("commentId", 6)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment/{commentId}/recomment")

            .then()
            .statusCode(CommentExceptionType.COMMENT_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.COMMENT_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.COMMENT_NOT_FOUND.getErrorMessage()));
    }
}