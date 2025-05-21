package vn.base.edumate.post_report;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.post.Post;
import vn.base.edumate.user.entity.User;



@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostReport extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String reason;


}
