package aptech.computer.entity;

import aptech.computer.entity.baseEntity.BaseEntity;
import aptech.computer.enumAll.MethodEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private MethodEnum method;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

}
