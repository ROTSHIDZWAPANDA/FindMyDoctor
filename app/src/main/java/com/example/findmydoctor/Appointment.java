package com.example.findmydoctor;

public class Appointment {
    private String userId;
    private String doctorId;
    private String userName;
    private String doctorName;
    private String surgeryName;
    private String date;
    private String status;
    private String time;
    private String consultationType;
    private double consultationFee;
    private boolean paidStatus;
    private String problemDescription;
    private boolean doctorApproval;
    private boolean rescheduleRequest;

    private String appointmentID;

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Appointment() {
        // Default constructor required for DataSnapshot.getValue(Appointment.class)
    }

    public Appointment(String userId, String doctorId, String userName, String doctorName, String surgeryName,
                       String date, String status, String time, String consultationType, double consultationFee,
                       boolean paidStatus, String problemDescription) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.userName = userName;
        this.doctorName = doctorName;
        this.surgeryName = surgeryName;
        this.date = date;
        this.status = status;
        this.time = time;
        this.consultationType = consultationType;
        this.consultationFee = consultationFee;
        this.paidStatus = paidStatus;
        this.problemDescription = problemDescription;

        // Default values for doctor approval and reschedule request
        this.doctorApproval = false;
        this.rescheduleRequest = false;
    }

    // Getters and setters for the additional attributes (doctor approval and reschedule request)

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSurgeryName() {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public boolean isPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(boolean paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public boolean isDoctorApproval() {
        return doctorApproval;
    }

    public void setDoctorApproval(boolean doctorApproval) {
        this.doctorApproval = doctorApproval;
    }

    public boolean isRescheduleRequest() {
        return rescheduleRequest;
    }

    public void setRescheduleRequest(boolean rescheduleRequest) {
        this.rescheduleRequest = rescheduleRequest;
    }
}