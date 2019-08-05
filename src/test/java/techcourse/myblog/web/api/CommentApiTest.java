package techcourse.myblog.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.web.controller.BaseControllerTests;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentApiTest extends BaseControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    private String jSessionId;

    @BeforeEach
    void setUp() {
        final String userPassword = "P@ssw0rd";
        final String userEmail = "emailArticle@gamil.com";

        addUser("name", userEmail, userPassword);
        jSessionId = getJSessionId(userEmail, userPassword);
    }

    @Test
    public void create() throws IOException {

        final String contents = "changed contents";
        CommentDto.Create commentDto = new CommentDto.Create();
        commentDto.setContents(contents);

        final EntityExchangeResult<byte[]> entityExchangeResult = webTestClient.post().uri("/api/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie(JSESSIONID, jSessionId)
                .body(Mono.just(commentDto), CommentDto.Create.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().returnResult();

        String body = new String(Objects.requireNonNull(entityExchangeResult.getResponseBody()));

        List<CommentDto.Response> comments = new ObjectMapper().readValue(body, List.class);

        assertThat(comments).isNotEmpty();
    }
}