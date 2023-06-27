package security.example.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.example.security.model.User;

import java.util.Collection;

public interface JwtService {
    String generateToken(User user, Collection<SimpleGrantedAuthority> authorities);
    String generateRefreshToken(User user, Collection<SimpleGrantedAuthority>authorities);
}
