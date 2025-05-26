    package vn.base.edumate.token;


    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import lombok.experimental.SuperBuilder;
    import vn.base.edumate.common.base.AbstractEntity;
    import vn.base.edumate.common.util.TokenType;
    import vn.base.edumate.user.entity.User;

    @Entity
    @SuperBuilder
    @Getter
    @Setter
    @NoArgsConstructor
    @Table(name = "tbl_token")
    public class Token extends AbstractEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        public String token;

        @Enumerated(EnumType.STRING)
        public TokenType tokenType;

        public boolean revoked;

        public boolean expired;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        public User user;
    }
