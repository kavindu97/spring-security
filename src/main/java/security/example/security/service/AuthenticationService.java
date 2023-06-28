package security.example.security.service;


import security.example.security.auth.dto.AuthenticationRequest;
import security.example.security.auth.dto.RegisterRequest;
import security.example.security.dto.ResponseDto;

public interface AuthenticationService {
    ResponseDto register(RegisterRequest registerRequest);
    ResponseDto registerAdmin(RegisterRequest registerRequest);
   ResponseDto authenticate(AuthenticationRequest authenticationRequest);
}
