package aptech.computer.entity;


import aptech.computer.entity.baseEntity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "roles")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(generator = "custom-name")
    @GenericGenerator(name = "custom-name",
            strategy = "aptech.computer.util.CustomId",
            parameters = @Parameter(name = "prefix",value = "ROLE"))
    private String id;
    private String name;
    @ManyToMany(mappedBy = "roles",cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.REFRESH}
            )
    @JsonBackReference
    private Set<Account> members;
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.REFRESH}
            ,fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "permissionId")
    )
    private Set<Permission> permissions;
}
