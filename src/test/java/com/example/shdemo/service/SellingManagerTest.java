package com.example.shdemo.service;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.shdemo.domain.Restaurant;
import com.example.shdemo.domain.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional

public class SellingManagerTest {

	@Autowired
	SellingManager sellingManager;
//Restaurant
	private final String NAME_1 = "Smak";
	private final String NIP_1 = "1234";
	private final Date DATE_1 = new Date();

	private final String NAME_2 = "Pierog";
	private final String NIP_2 = "4321";
	private final Date DATE_2 = new Date();
//Worker
	private final String NAME_3 = "Lukasz";
	private final String SURNAME_1 = "Klin";

	private final String NAME_4 = "Kasia";
	private final String SURNAME_2 = "Nowak";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
	Restaurant restaurant = new Restaurant();
	Worker worker = new Worker();

	@Before
	public void before()
	{
		restaurant.setName(NAME_1);
		restaurant.setNip(NIP_1);
		restaurant.setRegistrationDate(DATE_1);

		worker.setName(NAME_3);
		worker.setSurname(SURNAME_1);
		worker.setRestaurant(restaurant);
		worker.setHired(true);
	}
	@After
	public void after()
	{
		List<Worker> workers = sellingManager.getAllWorkers();
		List<Restaurant> restaurants = sellingManager.getAllRestaurants();

		for (Worker w : workers)
		{
			if(w.getId() == worker.getId())
			sellingManager.deleteWorker(worker);
		}

		for (Restaurant r : restaurants)
		{
			if(r.getId() == restaurant.getId())
				sellingManager.deleteRestaurant(restaurant);
		}
	}

	@Test
	public void addRestaurantCheck() {

		sellingManager.addRestaurant(restaurant);

		Restaurant retrievedRestaurant = sellingManager.findRestaurantByNip(NIP_1);

		assertEquals(NAME_1, retrievedRestaurant.getName());
		assertEquals(NIP_1, retrievedRestaurant.getNip());
		// ... check other properties here
	}

	@Test
	public void getAllRestaurantsCheck()
	{
		List<Restaurant> restaurants = sellingManager.getAllRestaurants();
		int count = restaurants.size();
		sellingManager.addRestaurant(restaurant);
		restaurants = sellingManager.getAllRestaurants();
		assertEquals(count+1, restaurants.size());
	}

	@Test
	public void addNewWorkerCheck()
	{
		sellingManager.addRestaurant(restaurant);
		Long workerId = sellingManager.addNewWorker(worker);

		Worker retrievedWorker = sellingManager.findWorkerById(workerId);
		assertEquals(NAME_3, retrievedWorker.getName());
		assertEquals(SURNAME_1, retrievedWorker.getSurname());
		// ... check other properties here
	}

	@Test
	public void findRestaurantByIdCheck() {
		Long id = sellingManager.addRestaurant(restaurant);
		Restaurant restaurant = sellingManager.findRestaurantById(id);
		assertEquals(id, restaurant.getId());
		assertEquals(NAME_1, restaurant.getName());
		assertEquals(NIP_1, restaurant.getNip());
	}

	@Test
	public void getHiredWorkersCheck() {
		int size = sellingManager.getHiredWorkers().size();
		sellingManager.addRestaurant(restaurant);
		sellingManager.addNewWorker(worker);
		assertEquals(size + 1, sellingManager.getHiredWorkers().size());
		List<Worker> workersH = sellingManager.getHiredWorkers();
		for (Worker wH : workersH)
			assertEquals(true, wH.getHired());
	}

	@Test
	public void getAllWorkersByRestaurantCheck()
	{
		List<Worker> workers = new ArrayList<Worker>();
		workers.add(worker);
		restaurant.setWorkers(workers);
		sellingManager.addRestaurant(restaurant);

		List<Worker> retrievedWorkers = sellingManager.getAllWorkersByRestaurant(restaurant);
		assertEquals(1, retrievedWorkers.size());
	}

	@Test
	public void deleteRestaurantCheck()
	{
		sellingManager.addRestaurant(restaurant);
		int count = sellingManager.getAllRestaurants().size();
		sellingManager.deleteRestaurant(restaurant);
		int count2 = sellingManager.getAllRestaurants().size();
		assertEquals(count-1, count2);
		assertNull(sellingManager.findRestaurantById(restaurant.getId()));
	}

	@Test
	public void updateRestaurantCheck()
	{
		List<Worker> workers = new ArrayList<Worker>();
		workers.add(worker);
		restaurant.setWorkers(workers);
		sellingManager.addRestaurant(restaurant);
		sellingManager.updateRestaurant(restaurant, NAME_2, NIP_2, DATE_2, workers);
		assertEquals(NAME_2, restaurant.getName());
		assertEquals(NIP_2, restaurant.getNip());
		assertEquals(DATE_2, restaurant.getRegistrationDate());
		assertEquals(workers, restaurant.getWorkers());
        List<Restaurant> restaurants = sellingManager.getAllRestaurants();
        for (Restaurant r : restaurants) {
            if (!r.equals(restaurant)) {
                assertThat(r.getName(), not(NAME_2));
                assertThat(r.getNip(), not(NIP_2));
                assertThat(r.getRegistrationDate(), not(DATE_2));
                assertThat(r.getWorkers(), not(workers));
            }
        }
	}
}
