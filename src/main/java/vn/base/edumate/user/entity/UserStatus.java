package vn.base.edumate.user.entity;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.common.util.UserStatusCode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "tbl_user_status")
public class UserStatus extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code")
    private UserStatusCode userStatusCode;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_days")
    private int durationInDays;
}
