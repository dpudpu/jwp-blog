package techcourse.myblog.articles.comments;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.articles.Article;
import techcourse.myblog.articles.BaseEntity;
import techcourse.myblog.exception.AuthException;
import techcourse.myblog.users.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_comment_to_user"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"), nullable = false)
    private Article article;

    @Builder
    public Comment(final String contents, final User user, final Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public void update(final Comment other) {
        this.contents = other.contents;
    }

    public boolean isWrittenBy(final User other) {
        if (this.user.equals(other)) {
            return true;
        }
        throw new AuthException("작성자가 아닙니다.");
    }
}
