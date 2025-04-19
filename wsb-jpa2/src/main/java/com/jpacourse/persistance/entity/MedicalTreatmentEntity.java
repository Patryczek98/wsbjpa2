package com.jpacourse.persistance.entity;

import com.jpacourse.persistance.enums.TreatmentType;
import jakarta.persistence.*;

@Entity
@Table(name = "medical_treatment")
public class MedicalTreatmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private TreatmentType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_id")
	private VisitEntity visit;

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public TreatmentType getType() { return type; }
	public void setType(TreatmentType type) { this.type = type; }
	public VisitEntity getVisit() { return visit; }
	public void setVisit(VisitEntity visit) { this.visit = visit; }
}