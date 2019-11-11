package com.skb.course.apis.libraryapis.user;


import com.skb.course.apis.libraryapis.model.common.Gender;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USER")
public class UserEntity {

    @Column(name = "User_Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userId_generator")
    @SequenceGenerator(name="userId_generator", sequenceName = "user_sequence", allocationSize=1)
    private int userId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Date_Of_Birth")
    private LocalDate dateOfBirth;

    @Column(name = "Gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "Phone_Number")
    private String phoneNumber;

    @Column(name = "Email_Id")
    private String emailId;

    @Column(name = "Role")
    private String role;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String firstName, String lastName, LocalDate dateOfBirth, Gender gender,
                      String phoneNumber, String emailId, String role) {
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

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
