package com.pms.pmsmodule.DTO;

import lombok.Data;

@Data
public class PatientResponseDTO {
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String patientAddress;
    private String dateOfBirth;
    /*Getters & Setters **/
}
