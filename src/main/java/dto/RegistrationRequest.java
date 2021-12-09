package dto;

import data.models.Gender;

public record RegistrationRequest(String firstName, String lastName, String password, String matricNo, Gender gender) {
}
