package com.domain.certification.api.web.hateoasAssembler;

import com.domain.certification.api.data.User;
import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.web.UserController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler extends ResourceAssemblerSupport<User, UserDTO> {

    public UserAssembler() {
        super(UserController.class, UserDTO.class);
    }

    @Override
    public UserDTO toResource(User user) {
        UserDTO userDTO = null;
        if (user != null) {
            userDTO = createResourceWithId(user.getId(), user);
            userDTO.removeLinks();
        }

        return userDTO;
    }

    @Override
    public UserDTO instantiateResource(User user) {
        UserDTO userDTO = null;
        if (user != null) {
            userDTO = new UserDTO();
            userDTO.setUserId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setCreatedAt(user.getCreatedAt());
        }

        return userDTO;
    }
}
