package com.jpacourse.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visit")
public class VisitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "description")
	private String description;

	@Column(name = "visit_date_time")
	private LocalDateTime visitDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private DoctorEntity doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private PatientEntity patient;

	@OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MedicalTreatmentEntity> treatments = new ArrayList<>();

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public LocalDateTime getVisitDateTime() { return visitDateTime; }
	public void setVisitDateTime(LocalDateTime visitDateTime) { this.visitDateTime = visitDateTime; }
	public DoctorEntity getDoctor() { return doctor; }
	public void setDoctor(DoctorEntity doctor) { this.doctor = doctor; }
	public PatientEntity getPatient() { return patient; }
	public void setPatient(PatientEntity patient) { this.patient = patient; }
	public List<MedicalTreatmentEntity> getTreatments() { return treatments; }
	public void setTreatments(List<MedicalTreatmentEntity> treatments) { this.treatments = treatments; }
}