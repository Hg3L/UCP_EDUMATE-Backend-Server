package vn.base.edumate.user;

import org.springframework.data.jpa.domain.Specification;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.RoleCode;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.entity.User;

public class UserSpecification {

    public static Specification<User> hasRole(RoleCode roleCode) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("role").get("roleCode"), roleCode);
    }

    public static Specification<User> hasAuthMethod(AuthMethod authMethod) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("authMethod"), authMethod);
    }

    public static Specification<User> hasUserStatus(UserStatusCode userStatusCode) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("status"), userStatusCode);
    }

    public static Specification<User> hasUserStatusNot(UserStatusCode userStatusCode) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.notEqual(root.get("status"), userStatusCode);
    }

    public static Specification<User> matchesKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("username")), likePattern),
                cb.like(cb.lower(root.get("email")), likePattern)
            );
        };
    }
}
