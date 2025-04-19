package com.jpacourse.dto;

import com.jpacourse.persistance.enums.TreatmentType;

import java.time.LocalDateTime;
import java.util.List;

public class VisitTO {

    private Long id;
    private LocalDateTime visitDateTime;
    private String doctorFirstName;
    private String doctorLastName;
    private List<TreatmentType> treatmentTypes;

    // Nowy konstruktor
    public VisitTO(Long id, LocalDateTime visitDateTime, String doctorFirstName, String doctorLastName, List<TreatmentType> treatmentTypes) {
        this.id = id;
        this.visitDateTime = visitDateTime;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
        this.treatmentTypes = treatmentTypes;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(LocalDateTime visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public List<TreatmentType> getTreatmentTypes() {
        return treatmentTypes;
    }

    public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
        this.treatmentTypes = treatmentTypes;
    }
}