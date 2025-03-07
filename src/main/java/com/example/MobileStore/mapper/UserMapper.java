package com.example.MobileStore.mapper;

import com.example.MobileStore.dto.RegisterDTO;
import com.example.MobileStore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "Spring")
@Component
public interface UserMapper {
    @Mapping(target = "role",source = "role.id")
    RegisterDTO toDTO(User user);

    @Mapping(target = "role.id",source = "role")
    User toEntity(RegisterDTO registerDTO);
}
