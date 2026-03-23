package com.CardMaster.modules.iam.mapper;

import com.CardMaster.modules.iam.dto.UserDto;
import com.CardMaster.modules.iam.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setRole(user.getRole());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }
}
