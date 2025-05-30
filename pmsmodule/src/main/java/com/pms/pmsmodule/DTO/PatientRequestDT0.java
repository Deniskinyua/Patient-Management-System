package com.pms.pmsmodule.DTO;

import com.pms.pmsmodule.DTO.Validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PatientRequestDT0 {


    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;


    @NotBlank(message = "Email is required")
    @Size(message = "Email should be valid")
    private String email;


    @NotBlank(message = "Address is required")
    private String address;


    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;


    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    private String registeredDate;

    private boolean deleted;

    public PatientRequestDT0() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PatientRequestDT0;
    }

}
