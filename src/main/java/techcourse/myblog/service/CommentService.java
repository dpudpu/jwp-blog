package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentDto.Response save(final CommentDto.Create commentDto, final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 아닙니다."));

        Article article = findArticleById(commentDto.getArticleId());
        Comment comment = commentDto.toComment(article, user);
        comment = commentRepository.save(comment);
        return CommentDto.Response.createByComment(comment);
    }

    public CommentDto.Response update(final CommentDto.Update commentDto, final Long userId) {
        final Comment comment = findCommentById(commentDto.getId());

        comment.isWrittenBy(userId);
        comment.update(commentDto.toComment());

        return CommentDto.Response.createByComment(comment);
    }

    public void delete(final Long commentId, final Long userId) {
        final Comment comment = findCommentById(commentId);

        comment.isWrittenBy(userId);
        commentRepository.delete(comment);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 아닙니다."));
    }

    private Article findArticleById(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 글이 아닙니다."));
    }

    public List<CommentDto.Response> findAllByArticleId(final Long articleId) {
        //TODO findAllByArticle(article) article 조회 안하는 방법
        Article article = findArticleById(articleId);
        List<Comment> comments = commentRepository.findAllByArticle(article);
        return comments.stream()
                .map(CommentDto.Response::createByComment)
                .collect(Collectors.toList());
    }
}
