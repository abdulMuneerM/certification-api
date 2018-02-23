package com.domain.certification.api.web;

import com.domain.certification.api.data.User;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.service.SessionService;
import com.domain.certification.api.service.UserService;
import com.domain.certification.api.util.dto.response.PageDTO;
import com.domain.certification.api.util.dto.response.ResponseDTO;
import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.util.dto.user.UserLoginDTO;
import com.domain.certification.api.util.dto.user.UserLoginRequestDTO;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import com.domain.certification.api.web.hateoasAssembler.UserAssembler;
import com.domain.certification.api.web.validator.UserLoginValidator;
import com.domain.certification.api.web.validator.UserRegisterValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserAssembler userAssembler;
    private final UserRegisterValidator userRegisterValidator;
    private final UserLoginValidator userLoginValidator;

    public UserController(UserService userService, SessionService sessionService, UserAssembler userAssembler,
                          UserRegisterValidator userRegisterValidator, UserLoginValidator userLoginValidator) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.userAssembler = userAssembler;
        this.userRegisterValidator = userRegisterValidator;
        this.userLoginValidator = userLoginValidator;
    }

    @InitBinder("userRequestDTO")
    protected void initUserRegisterBinder(WebDataBinder binder) {
        binder.setValidator(userRegisterValidator);
    }

    @PostMapping(
            value = "/signUp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> signUp(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        userService.save(userRequestDTO);
        UserLoginDTO userLoginDTO = sessionService.login(userRequestDTO.getEmail(), userRequestDTO.getPassword());

        Map<String, UserLoginDTO> data = new HashMap<>();
        data.put("login", userLoginDTO);

        ResponseDTO<Map<String, UserLoginDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @InitBinder("userLoginRequestDTO")
    protected void initUserLoginBinder(WebDataBinder binder) {
        binder.setValidator(userLoginValidator);
    }

    @PostMapping(
            value = "/signIn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> signIn(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        UserLoginDTO userLoginDTO = sessionService.login(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());

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

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String sort
    ) {

        String sortFieldName = "id";
        String sortDir = "asc";
        if (!StringUtils.isEmpty(sort)) {
            String[] arr = sort.split(",");
            if (arr.length == 2) {
                sortFieldName = arr[0];
                sortDir = arr[1];
            }
        }

        if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
            throw new UnProcessableEntityException("Invalid sort order");
        }

        Sort sortObj = new Sort(Sort.Direction.fromString(sortDir), sortFieldName);
        Pageable pageable = new PageRequest(page, size, sortObj);

        Page<User> users = userService.findAll(search, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("users", userAssembler.toResources(users.getContent()));
        PageDTO pageDTO = new PageDTO(users.getSize(), users.getTotalElements(),
                users.getTotalPages(), users.getNumber());
        data.put("page", pageDTO);

        ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
        response.setData(data);
        ;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
