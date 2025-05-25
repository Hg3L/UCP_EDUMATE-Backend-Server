package vn.base.edumate.image;

import java.sql.Blob;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.comment.Comment;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.post.Post;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_image")
public class Image extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private Blob imageBytes;

    @Column
    private String fileName;

    @Column
    private String fileType;

    @Column(nullable = true, name = "image_url")
    private String imageUrl;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Post post;

    @OneToOne(mappedBy = "image")
    private Comment comment;
}
