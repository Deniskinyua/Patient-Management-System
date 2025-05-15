package com.pms.pmsmodule.ExceptionHandlers;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
