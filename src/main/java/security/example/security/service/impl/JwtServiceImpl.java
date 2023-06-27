package security.example.security.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import security.example.security.model.User;
import security.example.security.repository.RoleCustomRepo;
import security.example.security.repository.UserRepository;
import security.example.security.service.JwtService;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${secret.key}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleCustomRepo roleCustomRepo;
@Override
    public String generateToken(User user, Collection<SimpleGrantedAuthority>authorities){
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("phone number",user.getMobileNumber())
                .withExpiresAt(new Date(System.currentTimeMillis()+50*60*1000))
                .withClaim("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
    @Override
    public String generateRefreshToken(User user, Collection<SimpleGrantedAuthority>authorities){
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+70*60*1000))
                .sign(algorithm);
    }
}
