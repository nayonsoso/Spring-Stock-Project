package com.example.stockproject.service;

import com.example.stockproject.model.Auth;
import com.example.stockproject.model.MemberEntity;
import com.example.stockproject.persist.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // spring security 에서 제공하는 기능을 사용하기 위해선 아래 함수를 구현해야 함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->"+ username));
    }

    public MemberEntity register(Auth.SignUp member){ // 회원가입
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if(exists){
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }

        // 사용자가 입력한 정보가 인코딩되어 저장됨
        // PasswordEncoder 를 AppConfig 에서 빈으로 등록해놨기 때문에 사용할 수 있는 것임
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    // 로그인 검증 : 일치하는 아이디와 패스워드가 존재하는가
    public MemberEntity authenticate(Auth.SignIn member){
        // 사용자 이름으로 MemberEntity 탐색
        var user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));

        // 리턴된 user의 비번은 인코딩된 상태이므로 인코딩되지 않은 member의 비번과 비교하기 위해서 passwordEncorder.matches 사용
        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
