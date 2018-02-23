package com.domain.certification.api.web;

import com.domain.certification.api.data.User;
import com.domain.certification.api.service.SessionService;
import com.domain.certification.api.service.UserService;
import com.domain.certification.api.util.dto.response.ResponseDTO;
import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.util.dto.user.UserLoginDTO;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import com.domain.certification.api.web.hateoasAssembler.UserAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserAssembler userAssembler;

    public UserController(UserService userService, SessionService sessionService, UserAssembler userAssembler) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.userAssembler = userAssembler;
    }

    @PostMapping(
            value = "/signUp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> signUp(@RequestBody UserRequestDTO userRequestDTO) {
        userService.save(userRequestDTO);
        UserLoginDTO userLoginDTO = sessionService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());

        Map<String, UserLoginDTO> data = new HashMap<>();
        data.put("login", userLoginDTO);

        ResponseDTO<Map<String, UserLoginDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/signIn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> signIn(@RequestBody UserRequestDTO userRequestDTO) {
        UserLoginDTO userLoginDTO = sessionService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());

        Map<String, UserLoginDTO> data = new HashMap<>();
        data.put("login", userLoginDTO);

        ResponseDTO<Map<String, UserLoginDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    ResponseEntity<ResponseDTO> findOneUser(@PathVariable(name = "id") String id) {
        User user = userService.findOne(id);

        Map<String, UserDTO> data = new HashMap<>();
        data.put("user", userAssembler.toResource(user));

        ResponseDTO<Map<String, UserDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
