package dto;

import data.models.Gender;

public record Register(String firstName, String lastName, String password, String matricNo, Gender gender) {
}
