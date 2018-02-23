package com.domain.certification.api.security;

import com.domain.certification.api.util.dto.response.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

@Component
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseDTO.setError(com.domain.certification.api.exception.AuthenticationException.class.getSimpleName());
        responseDTO.setMessage("Access denied.");
        responseDTO.setTimestamp(Instant.now().getEpochSecond());

        ObjectMapper objectMapper = new ObjectMapper();
        String responseString = objectMapper.writeValueAsString(responseDTO);

        PrintWriter writer = response.getWriter();
        writer.write(responseString);
        writer.flush();
        writer.close();
    }
}