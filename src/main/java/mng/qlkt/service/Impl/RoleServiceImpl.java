package mng.qlkt.service.Impl;

import mng.qlkt.model.Role;
import mng.qlkt.model.RoleName;
import mng.qlkt.repository.RoleRepository;
import mng.qlkt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
