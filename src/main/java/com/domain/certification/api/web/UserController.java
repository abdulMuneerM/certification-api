package com.domain.certification.api.web;

import com.domain.certification.api.data.User;
import com.domain.certification.api.service.SessionService;
import com.domain.certification.api.service.UserService;
import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.util.dto.user.UserLoginDTO;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping(
            value = "/signUp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserLoginDTO> signUp(@RequestBody UserRequestDTO userRequestDTO) {
        userService.save(userRequestDTO);
        UserLoginDTO userLoginDTO = sessionService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        return new ResponseEntity<>(userLoginDTO, HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/signIn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UserLoginDTO> signIn(@RequestBody UserRequestDTO userRequestDTO ) {
        UserLoginDTO userLoginDTO =  sessionService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        return new ResponseEntity<>(userLoginDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/signOut")
    @ResponseStatus(HttpStatus.OK)
    public void signOut() {
        sessionService.logout();
    }

    private UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDTO> findOneUser(@PathVariable(name = "id") String id) {
        return new ResponseEntity<>(getUserDTO(userService.findOne(id)), HttpStatus.OK);
    }
}
