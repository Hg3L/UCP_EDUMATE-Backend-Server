package vn.base.edumate.user.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.comment.Comment;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.post.Post;
import vn.base.edumate.role.Role;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "tbl_user")
public class User extends AbstractEntity implements UserDetails, Serializable {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "username", columnDefinition = "VARCHAR(100)")
    String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_method")
    AuthMethod authMethod;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserStatusHistory> statusHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    Role role;

    @OneToMany(mappedBy = "author",orphanRemoval = true, cascade = CascadeType.ALL)
    List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_comment_likes ",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> commentsLike = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return statusHistories.stream()
                .filter(userStatusHistory ->
                        userStatusHistory.getUserStatus().getUserStatusCode().equals(UserStatusCode.LOCKED))
                .findFirst()
                .isEmpty();
    }
}
