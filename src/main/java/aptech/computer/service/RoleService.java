package aptech.computer.service;

import aptech.computer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

}
