package com.projeto_integrado_biblioteca.domains.auth;

import com.projeto_integrado_biblioteca.domains.user.User;
import com.projeto_integrado_biblioteca.domains.user.UserRole;
import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.domains.user.UserMapper;
import com.projeto_integrado_biblioteca.domains.user.UserRepository;
import com.projeto_integrado_biblioteca.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager manager;

    @Transactional
    public String registerUser(AuthRegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Já existe uma conta com este email.");
        }

        String hashPassword = encoder.encode(request.password());
        UserRole role = UserRole.valueOf(request.role());

        User newUser = userMapper.authRegisterToUser(request);
        newUser.setPassword(hashPassword);
        newUser.setRole(role);

        userRepository.save(newUser);

        return "Usuário cadastrado com sucesso";
    }

    public AuthResponse authenticateUser(AuthLoginRequest request) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            var authentication = manager.authenticate(authenticationToken);

            var user = (User) authentication.getPrincipal();

            String token = tokenService.createToken(user);

            String message = "Login realizado com sucesso";

            return new AuthResponse(token, message);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("E-mail ou senha incorretos");
        }
    }
}
