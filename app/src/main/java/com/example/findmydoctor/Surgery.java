package com.example.findmydoctor;

import java.util.ArrayList;
import java.util.List;

public class Surgery {
    private String firstName;
    private String lastName;

    private String profilePictureUrl;

    private String surgeryName;
    private String address;
    private String experience;
    private String language;
    private String specialist;
    private String insuranceAccepted;
    private String telemedicine;
    private String description;
    private double consultationFees;
    private List<String> pdfDownloadUrls; // Assuming you want to store multiple PDF download URLs
    private String userId;
    private String surgeryId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(String surgeryId) {
        this.surgeryId = surgeryId;
    }

    // Constructors
    public Surgery() {
        // Default constructor
    }

    public Surgery(String firstName,String lastName,String profilePictureUrl,String surgeryName, String address, String experience, String language, String specialist, String insuranceAccepted,
                   String telemedicine, String description, double consultationFees, List<String> pdfDownloadUrls) {

        this.firstName =firstName;
        this.lastName =lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.surgeryName = surgeryName;
        this.address = address;
        this.experience = experience;
        this.language = language;
        this.specialist = specialist;
        this.insuranceAccepted = insuranceAccepted;
        this.telemedicine = telemedicine;
        this.description = description;
        this.consultationFees = consultationFees;
        this.pdfDownloadUrls = pdfDownloadUrls;
    }

    // Getters and setters for each property
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getSurgeryName() {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLanguage() {
        return language;
    }



    public void setLanguage(String language) {
        this.language = language;
    }
    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }
    public String getInsuranceAccepted() {
        return insuranceAccepted;
    }

    public void setInsuranceAccepted(String insuranceAccepted) {
        this.insuranceAccepted = insuranceAccepted;
    }

    public String getTelemedicine() {
        return telemedicine;
    }

    public void setTelemedicine(String telemedicine) {
        this.telemedicine = telemedicine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getConsultationFees() {
        return consultationFees;
    }

    public void setConsultationFees(double consultationFees) {
        this.consultationFees = consultationFees;
    }

    public void addPdfDownloadUrl(String pdfUrl) {
        if (pdfDownloadUrls == null) {
            pdfDownloadUrls = new ArrayList<>();
        }
        pdfDownloadUrls.add(pdfUrl);
    }

    // Define a getter method to retrieve the list of PDF download URLs
    public List<String> getPdfDownloadUrls() {
        return pdfDownloadUrls;
    }

    public void setPdfDownloadUrls(List<String> pdfDownloadUrls) {
        this.pdfDownloadUrls = pdfDownloadUrls;
    }

    // Additional methods as needed
}
