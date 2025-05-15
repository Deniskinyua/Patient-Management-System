package com.pms.pmsmodule.DTO;

import com.pms.pmsmodule.DTO.Validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class PatientRequestDT0 {

    @Getter
    @Setter
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Getter
    @Setter
    @NotBlank(message = "Email is required")
    @Size(message = "Email should be valid")
    private String email;

    @Getter
    @Setter
    @NotBlank(message = "Address is required")
    private String address;

    @Getter
    @Setter
    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @Getter
    @Setter
    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    private String registeredDate;
}
