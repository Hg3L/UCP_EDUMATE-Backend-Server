package vn.base.edumate.history.ai;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.user.entity.User;

import java.sql.Blob;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_history_ai_search")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AISearch extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @Column
    Blob imageBytes;

    @Column(columnDefinition = "TEXT")
    String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
