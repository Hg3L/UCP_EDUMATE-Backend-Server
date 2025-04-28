package vn.base.edumate.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.RoleCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleType(RoleCode roleCode) {
        return roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_EXISTED));
    }
}
