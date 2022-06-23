package aptech.computer.controller;

import aptech.computer.entity.dto.AccountDTO;
import aptech.computer.entity.dto.RegisterDTO;
import aptech.computer.service.AuthenticationService;
import aptech.computer.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/v1")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<AccountDTO> register(@RequestBody RegisterDTO registerDTO) {
        AccountDTO account = authenticationService.saveAccount(registerDTO);
        return ResponseEntity.ok().body(account);
    }
    @RequestMapping(path = "getUser")
    public ResponseEntity<AccountDTO> getUser(){
        return ResponseEntity.ok().body(new AccountDTO(authenticationService.getAccount(Objects.requireNonNull(AuthenticationUtil.getCurrentUser()).getName())));
    }
}
