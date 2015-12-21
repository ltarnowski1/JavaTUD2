package com.example.shdemo.service;

import java.util.ArrayList;
import java.util.Date;
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

	// CREATE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	@Override
	public Long addRestaurant(Restaurant restaurant) {
		//restaurant.setId(null);
		Long id = (Long) sessionFactory.getCurrentSession().save(restaurant);
		restaurant.setId(id);
		return id;
	}

	@Override
	public Long addNewWorker(Worker worker) {
		//worker.setId(null);
		Long id = (Long) sessionFactory.getCurrentSession().save(worker);
		worker.setId(id);
		Restaurant restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, worker.getRestaurant().getId());
		restaurant.getWorkers().add(worker);
		return id;
	}

	// READE ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ALL ---

	@Override
	@SuppressWarnings("unchecked")
	public List<Restaurant> getAllRestaurants() {
		return sessionFactory.getCurrentSession().getNamedQuery("restaurant.all")
				.list();
	}

	@Override
	public List<Worker> getAllWorkers()
	{
		return sessionFactory.getCurrentSession().getNamedQuery("worker.all")
				.list();
	}

	// BY ID ---

	@Override
	public Restaurant findRestaurantById(Long Id)
	{
		return (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, Id);
	}

	@Override
	public Worker findWorkerById(Long Id)
	{
		return (Worker) sessionFactory.getCurrentSession().get(Worker.class, Id);
	}

	// BY SOMETHING UNIQUE ---

	@Override
	public Restaurant findRestaurantByNip(String nip)
	{
		return (Restaurant) sessionFactory.getCurrentSession().getNamedQuery("restaurant.byNip").setString("nip", nip).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Worker> getHiredWorkers() {
		return sessionFactory.getCurrentSession().getNamedQuery("worker.hired")
				.list();
	}

	// ALL X IN Y ---

	@Override
	public List<Worker> getAllWorkersByRestaurant(Restaurant restaurant)
	{
		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class,
				restaurant.getId());
		// lazy loading here - try this code without (shallow) copying
		List<Worker> workers = new ArrayList<Worker>(restaurant.getWorkers());
		return workers;
	}

	// UPDATE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	@Override
	public void updateRestaurant(Restaurant restaurant, String name, String nip, Date registration, List<Worker> workers)
	{
		restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, restaurant.getId());
		restaurant.setName(name);
		restaurant.setNip(nip);
		restaurant.setRegistrationDate(registration);
		restaurant.setWorkers(workers);
		sessionFactory.getCurrentSession().update(restaurant);
	}

	@Override
	public void updateWorker(Worker worker, String name, String surname, Restaurant restaurant, Boolean hired)
	{
		worker = (Worker) sessionFactory.getCurrentSession().get(Worker.class, worker.getId());
		worker.setName(name);
		worker.setSurname(surname);
		worker.setRestaurant(restaurant);
		worker.setHired(hired);
		sessionFactory.getCurrentSession().update(worker);
	}

	// DELETE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
	public void deleteWorker(Worker worker)
	{
		worker = (Worker) sessionFactory.getCurrentSession().get(Worker.class, worker.getId());
		Restaurant restaurant = (Restaurant) sessionFactory.getCurrentSession().get(Restaurant.class, worker.getRestaurant().getId());
		restaurant.getWorkers().remove(worker);
		sessionFactory.getCurrentSession().delete(worker);
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




}
