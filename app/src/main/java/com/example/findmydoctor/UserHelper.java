package com.example.findmydoctor;

public class UserHelper {
    public String firstName;
    public String lastName;
    public String userAddress;
    public String phoneNumber;
    public String role;
    public String profilePictureUrl;

    public UserHelper() {

    }
    public UserHelper(String firstName, String lastName, String userAddress, String phoneNumber, String role, String profilePictureUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userAddress = userAddress;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getuserAddress() {
        return userAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
