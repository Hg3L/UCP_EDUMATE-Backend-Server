package vn.base.edumate.common.config;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.RoleCode;
import vn.base.edumate.common.util.TagType;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.role.Role;
import vn.base.edumate.role.RoleRepository;
import vn.base.edumate.tag.Tag;
import vn.base.edumate.tag.TagRepository;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.entity.UserStatus;
import vn.base.edumate.user.entity.UserStatusHistory;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.repository.UserStatusHistoryRepository;
import vn.base.edumate.user.repository.UserStatusRepository;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class DataInitializerConfig {

    final PasswordEncoder passwordEncoder;

    @Value("${system.default.admin.account.username}")
    String adminUsername;

    @Value("${system.default.admin.account.email}")
    String adminEmail;

    @Value("${system.default.admin.account.password}")
    String adminPassword;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner init(
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final TagRepository tagRepository,
            final UserStatusRepository userStatusRepository,
            final UserStatusHistoryRepository userStatusHistoryRepository) {

        log.info("------------------ Initializing Default Data ------------------");

        return args -> {
            createRolesIfNotExist(roleRepository);
            createUserStatusIfNotExist(userStatusRepository);
            createTagsIfNotExist(tagRepository);

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                log.info("Creating default admin account...");

                Role adminRole = roleRepository
                        .findByRoleCode(RoleCode.SYSTEM_ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_EXISTED));

                User admin = User.builder()
                        .id(UUID.randomUUID().toString())
                        .username(adminUsername)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .authMethod(AuthMethod.SYSTEM)
                        .role(adminRole)
                        .build();
                userRepository.save(admin);

                UserStatus userStatus = userStatusRepository
                        .findByUserStatusCode(UserStatusCode.NORMAL)
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_STATUS_NOT_EXISTED));

                UserStatusHistory adminStatusHistory = UserStatusHistory.builder()
                        .user(admin)
                        .userStatus(userStatus)
                        .build();
                userStatusHistoryRepository.save(adminStatusHistory);
            }
        };
    }

    public void createRolesIfNotExist(RoleRepository roleRepository) {
        for (RoleCode roleCode : RoleCode.values()) {
            if (roleRepository.findByRoleCode(roleCode).isEmpty()) {
                roleRepository.save(Role.builder()
                        .roleCode(roleCode)
                        .roleName(roleCode.getRoleName())
                        .build());
                log.info("Created role: {}", roleCode.name());
            }
        }
    }

    public void createUserStatusIfNotExist(UserStatusRepository userStatusRepository) {
        for (UserStatusCode userStatusCode : UserStatusCode.values()) {
            if (userStatusRepository.findByUserStatusCode(userStatusCode).isEmpty()) {
                userStatusRepository.save(UserStatus.builder()
                        .userStatusCode(userStatusCode)
                        .description(userStatusCode.getDescription())
                        .durationInDays(userStatusCode.getDurationInDays())
                        .build());
                log.info("Created user status: {}", userStatusCode.name());
            }
        }
    }

    public void createTagsIfNotExist(TagRepository tagRepository) {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            List<Tag> newTags = Arrays.asList(
                    Tag.builder()
                            .tagType(TagType.SHARING_KNOWLEDGE)
                            .name("Mẹo học tập")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.SHARING_KNOWLEDGE)
                            .name("Phương pháp ôn thi")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.SHARING_KNOWLEDGE)
                            .name("Chia sẻ kinh nghiệm")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.HOMEWORK_SUPPORT)
                            .name("Môn toán")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.HOMEWORK_SUPPORT)
                            .name("Môn Văn")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.HOMEWORK_SUPPORT)
                            .name("Môn Tiếng Anh")
                            .build(),
                    Tag.builder()
                            .tagType(TagType.HOMEWORK_SUPPORT)
                            .name("Khác")
                            .build()


            );
            tagRepository.saveAll(newTags);
        }
    }

}
