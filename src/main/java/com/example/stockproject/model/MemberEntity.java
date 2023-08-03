package com.example.stockproject.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="MEMBER")
public class MemberEntity implements UserDetails { // spring security 기능을 위해서 UserDetdils를 상속
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ElementCollection // 왜 이걸 추가하니 에러가 사라지는걸까
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // rolse의 각 요소와 SimpleGrantedAuthority 를 매핑시키는 이유는
        // spring에서 제공하는 role 기능을 사용하기 위해서
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
