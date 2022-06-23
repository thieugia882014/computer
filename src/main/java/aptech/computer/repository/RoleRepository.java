package aptech.computer.repository;

import aptech.computer.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String > {
    Optional<Role> findByName(String name);
    List<Role> findAllByNameIn(Iterable<String> strings);

}
