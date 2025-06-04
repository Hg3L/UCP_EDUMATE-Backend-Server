package vn.base.edumate.history.semantic;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.image.Image;
import vn.base.edumate.user.entity.User;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_history_semantic_search")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemanticSearch extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @Column
    Blob imageBytes;

    @OneToMany(mappedBy = "semanticSearch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Image> similarImages = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
