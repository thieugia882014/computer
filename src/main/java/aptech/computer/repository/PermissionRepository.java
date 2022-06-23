package aptech.computer.repository;

import aptech.computer.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {
    Optional<Permission> findByName(String name);
    Optional<Permission> findByUrl(String name);
}
