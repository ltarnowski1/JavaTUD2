package com.example.shdemo.service;

import java.util.List;

import com.example.shdemo.domain.Worker;
import com.example.shdemo.domain.Restaurant;

public interface SellingManager {
	//restaurant
	void addRestaurant(Restaurant restaurant);									//C
	List<Restaurant> getAllRestaurants();										//R
	void updateRestaurant(Restaurant restaurant, String name, String nip);		//U
	void deleteRestaurant(Restaurant restaurant);								//D
	Restaurant findRestaurantByNip(String nip);									//find by NIP
	Restaurant findRestaurantById(Long Id);										//find by ID
	//worker
	Long addNewWorker(Worker worker);
	List<Worker> getHiredWorkers();
	void disposeWorker(Restaurant restaurant, Worker worker);
	Worker findWorkerById(Long Id);
	List<Worker>getAllWorkersByRestaurant(Restaurant restaurant);				//get all X in Y

	void sellCar(Long personId, Long carId);

}
