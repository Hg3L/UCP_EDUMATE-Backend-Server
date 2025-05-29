package vn.base.edumate.user.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.comment.Comment;
import vn.base.edumate.commentlike.CommentLike;
import vn.base.edumate.common.base.AbstractEntity;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.UserStatusCode;

import vn.base.edumate.post.Post;
import vn.base.edumate.postlike.PostLike;
import vn.base.edumate.role.Role;
import vn.base.edumate.token.Token;

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

    @Column(name = "violation_count")
    private Integer violationCount = 0;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
    @Column(name ="introduction")
    private String introduction;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusCode status = UserStatusCode.NORMAL;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserStatusHistory> statusHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tbl_user_post_hidden",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> hiddenPosts = new ArrayList<>();

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
