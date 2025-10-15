package com.projeto_integrado_biblioteca.domains.user;

import com.projeto_integrado_biblioteca.domains.auth.AuthRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User authRegisterToUser(AuthRegisterRequest request);
}
