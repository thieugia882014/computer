package aptech.computer.entity.dto;


import aptech.computer.entity.Account;
import aptech.computer.entity.Role;
import aptech.computer.enumAll.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccountDTO {
    private String id;
    private String username;
    private List<String > roles;
    private AccountStatusEnum status;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        roles = account.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        this.status = account.getStatus();
    }
}