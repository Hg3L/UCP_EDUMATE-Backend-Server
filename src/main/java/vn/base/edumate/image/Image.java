package vn.base.edumate.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.post.Post;

import java.sql.Blob;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class Image extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(nullable = false)
    private Blob imageBytes;
    @Column(nullable = false,name = "image_url")
    private String imageUrl;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "post_id")
    private Post post;
}
