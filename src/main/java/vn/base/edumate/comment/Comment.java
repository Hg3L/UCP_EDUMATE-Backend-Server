package vn.base.edumate.comment;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.commentlike.CommentLike;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.image.Image;
import vn.base.edumate.post.Post;
import vn.base.edumate.user.entity.User;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_comment")
public class Comment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private int likes = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;
}
