package aptech.computer.service;

import aptech.computer.entity.Permission;
import aptech.computer.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;
    public Optional<Permission> findByName(String name){
        return permissionRepository.findByName(name);
    }
    public List<Permission> findAll(){
        return permissionRepository.findAll();
    }
}
