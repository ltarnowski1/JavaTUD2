package com.example.shdemo.service;

import java.util.ArrayList;
import java.util.List;


import com.example.shdemo.domain.Restaurant;
import com.example.shdemo.domain.Worker;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SellingMangerHibernateImpl implements SellingManager {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void addRestaurant(Restaurant restaurant) {
		restaurant.setId(null);
		sessionFactory.getCurrentSession().persist(restaurant);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Restaurant> getAllRestaurants() {
		return sessionFactory.getCurrentSession().getNamedQuery("restaurant.all")
				.list();
	}

	@Override
	public void updateRestaurant(Restaurant restaurant, String name, String nip)
	{
		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, restaurant.getId());
		restaurant.setName(name);
		restaurant.setNip(nip);

	}

	@Override
	public void deleteRestaurant(Restaurant restaurant) {
		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class,
				restaurant.getId());
		
		// lazy loading here
		for (Worker worker : restaurant.getWorkers()) {
			worker.setHired(false);
			sessionFactory.getCurrentSession().update(worker);
		}
		sessionFactory.getCurrentSession().delete(restaurant);
	}

	@Override
	public Restaurant findRestaurantByNip(String nip) {
		return (Restaurant) sessionFactory.getCurrentSession().getNamedQuery("restaurant.byNip").setString("nip", nip).uniqueResult();
	}

	@Override
	public Worker findWorkerById(Long Id) {
		return (Worker) sessionFactory.getCurrentSession().get(Worker.class, Id);
	}

	@Override
	public Long addNewWorker(Worker worker) {
		worker.setId(null);
		return (Long) sessionFactory.getCurrentSession().save(worker);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Worker> getHiredWorkers() {
		return sessionFactory.getCurrentSession().getNamedQuery("worker.hired")
				.list();
	}

	@Override
	public void disposeWorker(Restaurant restaurant, Worker worker) {

		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class,
				restaurant.getId());
		worker = (Worker) sessionFactory.getCurrentSession().get(Worker.class,
				worker.getId());

		Worker toRemove = null;
		// lazy loading here (restaurant.getWorkers)
		for (Worker aWorker : restaurant.getWorkers())
			if (aWorker.getId().compareTo(worker.getId()) == 0) {
				toRemove = aWorker;
				break;
			}

		if (toRemove != null)
			restaurant.getWorkers().remove(toRemove);

		worker.setHired(false);
	}

	@Override
	public List<Worker> getAllWorkersByRestaurant(Restaurant restaurant)
	{
		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class,
				restaurant.getId());
		// lazy loading here - try this code without (shallow) copying
		List<Worker> workers = new ArrayList<Worker>(restaurant.getWorkers());
		return workers;
	}

	@Override
	public void sellCar(Long personId, Long carId) {
		Restaurant restaurant = (Restaurant) sessionFactory.getCurrentSession().get(
				Restaurant.class, personId);
		Worker worker = (Worker) sessionFactory.getCurrentSession()
				.get(Worker.class, carId);
		worker.setHired(true);
		restaurant.getWorkers().add(worker);
	}

	@Override
	public Restaurant findRestaurantById(Long Id) { return (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, Id); }

}
