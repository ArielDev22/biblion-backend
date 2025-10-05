package com.projeto_integrado_biblioteca.user;

import com.projeto_integrado_biblioteca.auth.AuthRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User authRegisterToUser(AuthRegisterRequest request);
}
