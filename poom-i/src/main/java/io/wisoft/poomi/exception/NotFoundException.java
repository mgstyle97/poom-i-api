package io.wisoft.poomi.exception;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{

    private final String message = "Data Not Found";

}
