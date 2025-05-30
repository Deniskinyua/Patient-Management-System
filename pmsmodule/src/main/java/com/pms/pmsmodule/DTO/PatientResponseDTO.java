package com.pms.pmsmodule.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
public class PatientResponseDTO {
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String patientAddress;
    private String dateOfBirth;
}
