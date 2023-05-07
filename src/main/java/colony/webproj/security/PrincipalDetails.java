package colony.webproj.security;

import colony.webproj.dto.MemberDto;
import colony.webproj.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class PrincipalDetails implements UserDetails {

    private String loginId;
    private String password;
    private Role role;

    public PrincipalDetails(String loginId, String password, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(this.role.toString())); //권한
        return auth;
    }

    @Override
    public String getPassword() {
        return this.getPassword();
    }
    //dfssdfsf

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
