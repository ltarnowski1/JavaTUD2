package com.example.shdemo.service;

import java.util.Date;
import java.util.List;

import com.example.shdemo.domain.Worker;
import com.example.shdemo.domain.Restaurant;

public interface SellingManager {

	//restaurant
	//CRUD
	Long addRestaurant(Restaurant restaurant);
	List<Restaurant> getAllRestaurants();
	void updateRestaurant(Restaurant restaurant, String name, String nip, Date registration, List<Worker>workers);
	void deleteRestaurant(Restaurant restaurant);

	//REST
	Restaurant findRestaurantByNip(String nip);									//find by NIP
	Restaurant findRestaurantById(Long Id);										//find by ID

	//worker
	//CRUD
	Long addNewWorker(Worker worker);
	List<Worker> getAllWorkers();
	void updateWorker(Worker worker, String name, String surname, Restaurant restaurant, Boolean hired);
	void deleteWorker(Worker worker);

	//REST
	List<Worker> getHiredWorkers();												//find by hire
	Worker findWorkerById(Long Id);												//find by ID
	List<Worker>getAllWorkersByRestaurant(Restaurant restaurant);				//get all X in Y

}
