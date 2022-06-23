package aptech.computer.config;

import aptech.computer.entity.Permission;
import aptech.computer.repository.PermissionRepository;
import aptech.computer.repository.RoleRepository;
import aptech.computer.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String register = "/api/v1/register**", login = "/api/v1/login**", all = "/api/v1/**", admin = "/api/v1/admin/**", user = "/api/v1/user/**", refreshToken = "/api/v1/token/refresh**";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationService authenticationService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiAuthenticationFilter authenticationFilter = new ApiAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl(login);
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/**").permitAll();
//        List<Permission> permissions = permissionRepository.findAll();
//        http.authorizeRequests().antMatchers(login,register,refreshToken).permitAll();
//        for (Permission permission : permissions) {
//            switch (permission.getMethod()) {
//
//                case POST:
//                    http.authorizeRequests().antMatchers(HttpMethod.POST, permission.getUrl()).hasAuthority(permission.getName());
//                    break;
//                case PUT:
//                    http.authorizeRequests().antMatchers(HttpMethod.PUT, permission.getUrl()).hasAuthority(permission.getName());
//                    break;
//
//                case DELETE:
//                    http.authorizeRequests().antMatchers(HttpMethod.DELETE, permission.getUrl()).hasAuthority(permission.getName());
//                    break;
//
//                case GET:
//                    http.authorizeRequests().antMatchers(HttpMethod.GET, permission.getUrl()).hasAuthority(permission.getName());
//                    break;
//
//                default:
//                    http.authorizeRequests().antMatchers(permission.getUrl()).hasAuthority(permission.getName());
//                    break;
//            }
//
//        }
//        http.authorizeRequests().antMatchers(admin).hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(user).hasAnyAuthority("user", "admin");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new ApiAuthorizationFilter(roleRepository), UsernamePasswordAuthenticationFilter.class);
    }
}
