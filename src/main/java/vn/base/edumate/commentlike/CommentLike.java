package vn.base.edumate.commentlike;

import jakarta.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.*;
import vn.base.edumate.comment.Comment;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.user.entity.User;

@Entity
@Table(name = "tbl_comment_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLike extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // 🔥 Cái này sẽ sinh ON DELETE CASCADE
    private Comment comment;
}
