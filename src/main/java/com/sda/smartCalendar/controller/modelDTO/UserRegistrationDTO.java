package com.sda.smartCalendar.controller.modelDTO;

import com.sda.smartCalendar.validation.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;


@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "passwordConfirm"
        )
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotEmpty(message = "Pole nie może być puste")
    @Size(min = 3, max = 30, message = "Pole może zawierać od 3 do 30 znaków")
    private String email;

    @NotEmpty(message = "Pole nie może być puste")
    @Size(min = 3, max = 30, message = "Pole może zawierać od 3 do 30 znaków")
    private String firstName;

    @NotEmpty(message = "Pole nie może być puste")
    @Size(min = 3, max = 30, message = "Pole może zawierać od 3 do 30 znaków")
    private String lastName;

    @NotEmpty(message = "Pole nie może być puste")
    @Size(min = 6, message = "Pole może zawierać minimum 6 znaków")
    private String password;

    @NotEmpty(message = "Pole nie może być puste")
    @Size(min = 6, message = "Pole może zawierać minimum 6 znaków")
    private String passwordConfirm;

    private String provider;

    @AssertTrue(message = "Nie wyraziłeś zgody")
    private Boolean terms;

    private String phoneNumber;
}

