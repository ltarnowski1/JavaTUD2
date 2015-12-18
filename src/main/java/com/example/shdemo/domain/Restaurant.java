package com.example.shdemo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "restaurant.all", query = "Select p from Restaurant p"),
	@NamedQuery(name = "restaurant.byNip", query = "Select p from Restaurant p where p.nip = :nip")
})
public class Restaurant {

	private Long id;

	private String name = "unknown";
	private String nip = "";
	private Date registrationDate = new Date();

	private List<Worker> workers = new ArrayList<Worker>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true, nullable = false)
	public String getNip() {
		return nip;
	}
	public void setNip(String nip) {
		this.nip = nip;
	}

	@Temporal(TemporalType.DATE)
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	// Be careful here, both with lazy and eager fetch type
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Worker> getWorkers() {
		return workers;
	}
	public void setWorkers(List<Worker> workers) {
		this.workers = workers;
	}
}
