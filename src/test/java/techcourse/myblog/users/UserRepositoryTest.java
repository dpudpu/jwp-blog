package techcourse.myblog.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.articles.Article;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findById() {
        User user = User.builder()
                .email("powlsnm@asd")
                .name("asd")
                .password("P@ssw0rd")
                .build();

        User persistUser = testEntityManager.persist(user);

        Article article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        article.setAuthor(persistUser);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

    @Test
    public void findById2() {
        Article article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        Article persistArticle = testEntityManager.persist(article);

        User user = User.builder()
                .email("powlsnm@asdasd")
                .name("asd")
                .password("P@ssw0rd")
                .build();

        user.addArticle(persistArticle);
        User persistUser = testEntityManager.persist(user);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

}