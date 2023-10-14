package com.example.findmydoctor;

public class PendingSurgeryItem {
    private String surgeryName;
    private String doctorName;
    private String status;

    public PendingSurgeryItem(String surgeryName, String doctorName, String status) {
        this.surgeryName = surgeryName;
        this.doctorName = doctorName;
        this.status = status;
    }

    public String getSurgeryName() {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

