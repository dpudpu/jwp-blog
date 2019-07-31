package techcourse.myblog.articles.comments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.articles.Article;
import techcourse.myblog.articles.ArticleService;
import techcourse.myblog.users.UserDto;
import techcourse.myblog.users.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    private Long userId;
    private Long articleId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        UserDto.Register userDto = UserDto.Register.builder()
                .email("email@gmail.com")
                .name("name")
                .password("P@ssw0rd")
                .confirmPassword("P@ssw0rd")
                .build();

        userId = userService.save(userDto);

        Article article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        articleId = articleService.save(userId, article).getId();

        CommentDto.Create commentDto = new CommentDto.Create();
        commentDto.setArticleId(articleId);
        commentDto.setContents("contents");

        commentId = commentService.save(commentDto, userId);
    }

    @Test
    void update() {
        final String modifiedContents = "updateContents";

        CommentDto.Update commentDto = new CommentDto.Update();
        commentDto.setId(commentId);
        commentDto.setArticleId(articleId);
        commentDto.setContents(modifiedContents);

        final CommentDto.Response modifiedComment = commentService.update(commentDto, userId);

        assertThat(modifiedContents).isEqualTo(modifiedComment.getContents());
    }
}