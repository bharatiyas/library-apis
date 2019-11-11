package com.skb.course.apis.libraryapis.user;

import com.skb.course.apis.libraryapis.model.common.Gender;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class User {

    private Integer userId;

    @Size(min = 1, max = 50, message
            = "Username must be between 1 and 50 characters")
    private String username;

    @Size(min = 8, max = 20, message
            = "Password must be between 8 and 20 characters")
    private String password;

    @Size(min = 1, max = 50, message
            = "First Name must be between 1 and 50 characters")
    private String firstName;

    @Size(min = 1, max = 50, message
            = "Last Name must be between 1 and 50 characters")
    private String lastName;

    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    private Gender gender;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}", message = "Please enter phone number in format 123-456-789")
    private String phoneNumber;

    @Email(message = "Please enter a valid EmailId")
    private String emailId;

    private Role role;

    public User() {
    }

    public User(int userId, String username, String password, String firstName, String lastName, LocalDate dateOfBirth, Gender gender,
                String phoneNumber, String emailId, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.role = role;
    }

    public User(int userId, String username, String firstName, String lastName, LocalDate dateOfBirth, Gender gender,
                String phoneNumber, String emailId, Role role) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.role = role;
    }

    public User(String username, String firstName, String lastName, LocalDate dateOfBirth, Gender gender,
                String phoneNumber, String emailId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
    }

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", role=" + role +
                '}';
    }
}
