package com.domain.certification.api.config;

import com.domain.certification.api.util.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Component
public class ErrorResponseBuilder {

    public ResponseEntity<ResponseDTO> createErrorResponse(String message, int statusCode, String error) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(statusCode);
        responseDTO.setError(error);
        if (!StringUtils.isEmpty(message)) {
            responseDTO.setMessage(message);
        } else {
            responseDTO.setMessage("Oops! something went wrong, Please try again.");
        }
        responseDTO.setTimestamp(Instant.now().getEpochSecond());
        return new ResponseEntity<>(responseDTO, HttpStatus.valueOf(statusCode));
    }
}
