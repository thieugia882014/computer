package aptech.computer.service;

import aptech.computer.entity.Account;
import aptech.computer.entity.Role;
import aptech.computer.entity.dto.AccountDTO;
import aptech.computer.entity.dto.CredentialDTO;
import aptech.computer.entity.dto.RegisterDTO;
import aptech.computer.enumAll.AccountStatusEnum;
import aptech.computer.repository.AccountRepository;
import aptech.computer.repository.RoleRepository;
import aptech.computer.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    private static final String USER_ROLE = "user";

    // authorize là quyền, authentication là xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optional = accountRepository.findByUsername(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("Username not exists!!");
        }
        Account account = optional.get();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : account.getRoles()
        ) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(account.getUsername(), account.getHashPass(), authorities);
    }

    public AccountDTO saveAccount(RegisterDTO registerDTO) {
        //create new user role if not exist
        Optional<Role> userRoleOptional = roleRepository.findByName(USER_ROLE);
        Role userRole = userRoleOptional.orElse(null);
        if (userRole == null) {
            //create new role
            userRole = roleRepository.save(Role.builder().name(USER_ROLE).build());
        }
        //check if username has exist
        Optional<Account> byUsername = accountRepository.findByUsername(registerDTO.getUsername());
        if (byUsername.isPresent()) {
            throw new DataIntegrityViolationException("username had exits");
        }
        Account account = new Account();

        account.setUsername(registerDTO.getUsername());
        account.setHashPass(passwordEncoder.encode(registerDTO.getPassword()));
        account.setStatus(AccountStatusEnum.ACTIVE);
        account.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        Account save = accountRepository.save(account);
        return new AccountDTO(save);
    }

    public Account getAccount(String username) {
        Optional<Account> byUsername = accountRepository.findByUsername(username);
        return byUsername.orElse(null);
    }

    public CredentialDTO refreshToken(String authorizationHeader, HttpServletRequest request){
        String token = authorizationHeader.replace("Bearer", "").trim();
        DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
        String username = decodedJWT.getSubject();
        //load account in the token
        Account account = getAccount(username);
        if (account == null) {
            return null;
        }
        //now return new token
        //generate tokens
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: account.getRoles()
        ) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));

        }
        List<String > roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String accessToken = JwtUtil.generateToken(
                account.getUsername(),
                roles,
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 7);

        String refreshToken = JwtUtil.generateToken(
                account.getUsername(),
                roles,
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 14);

        return new CredentialDTO(accessToken, refreshToken);
    }
}
