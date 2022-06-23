package aptech.computer.entity;

import aptech.computer.entity.baseEntity.BaseEntity;
import aptech.computer.enumAll.AccountStatusEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "accounts")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(generator = "custom-name")
    @GenericGenerator(name = "custom-name",strategy = "aptech.computer.util.CustomId",parameters = @Parameter(name = "prefix",value = "USER"))
    private String id;
    @Column(unique = true)
    private String username;
    private String hashPass;
    private String name;
    private String avatar;
    private AccountStatusEnum status;
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REMOVE,CascadeType.REFRESH}
            ,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns =@JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @JsonManagedReference
    private Set<Role> roles;
}
