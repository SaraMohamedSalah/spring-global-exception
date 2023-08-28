package com.demo.HandlingException.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}