package mng.qlkt.service;

import mng.qlkt.model.Role;
import mng.qlkt.model.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(RoleName name);
}
