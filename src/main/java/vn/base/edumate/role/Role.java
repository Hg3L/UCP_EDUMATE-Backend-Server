package vn.base.edumate.role;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.common.util.RoleCode;
import vn.base.edumate.user.entity.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "tbl_role")
public class Role extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role_type")
    RoleCode roleCode;

    @Column(name = "role_name")
    String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> userEntities = new HashSet<>();
}
