package com.example.shdemo.domain;

import javax.persistence.*;

@Entity
@NamedQueries({
		@NamedQuery(name = "worker.all", query = "Select p from Worker p"),
		@NamedQuery(name = "worker.hired", query = "Select c from Worker c where c.hired = true")
})
public class Worker {

	private Long id;
	private String name;
	private String surname;
	private Restaurant restaurant;
	private Boolean hired = false;

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

	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_restaurant", nullable = false)
	public Restaurant getRestaurant() { return restaurant; }
	public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

	public Boolean getHired() {
		return hired;
	}
	public void setHired(Boolean hired) {
		this.hired = hired;
	}
}
