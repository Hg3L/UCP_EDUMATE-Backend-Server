package vn.base.edumate.role;

import vn.base.edumate.common.util.RoleCode;

public interface RoleService {
    /**
     * Used by controller
     */

    /**
     * Used by another service
     */
    Role getRoleByRoleType(RoleCode roleCode);
}
