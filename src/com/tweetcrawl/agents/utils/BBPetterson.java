package com.tweetcrawl.agents.utils;

/**
 * Class used as a mutual exclusion for {@code Processor} agents. The instance
 * should be created by the {@code Launcher} agent.
 */
public class BBPetterson {

	private int tour;
	private boolean[] requests;

	private static BBPetterson instance = null;

	/**
	 * Allows to get the instance of the class.
	 * 
	 * @return the instance of the {@code BBPetterson} class.
	 * @throws BBPettersonException if the class has not been previously
	 *                              instanciated in the program
	 */
	public static BBPetterson getInstance() throws BBPettersonException {
		if (instance == null) {
			throw new BBPettersonException("BBPetterson has not been instanciated.");
		}
		return instance;
	}

	/**
	 * Allows to create the instance of the class.
	 * 
	 * @param nbConcurrent the number of concurrent {@code Processor} agents that
	 *                     will use the instance
	 * @throws BBPettersonException if the class has already been instanciated in
	 *                              the program
	 */
	public static synchronized void createInstance(int nbConcurrent) throws BBPettersonException {
		if (instance == null) {
			instance = new BBPetterson(nbConcurrent);
		} else {
			throw new BBPettersonException("BBPetterson has already been instanciated.");
		}
	}

	/**
	 * Private constructor of the class.
	 * 
	 * @param n the number of concurrent {@code Processor} agents that will use the
	 *          instance
	 */
	private BBPetterson(int n) {
		this.requests = new boolean[n];
		for (int i = 0; i < n; i++) {
			this.requests[i] = false;
		}
	}

	/**
	 * Allows to get the current {@code Processor} agent tour as its corresponding
	 * id.
	 * 
	 * @return the id of the current {@code Processor} agent tour
	 */
	public synchronized int getTour() {
		return this.tour;
	}

	/**
	 * Allows to define the current {@code Processor} agent tour as its
	 * corresponding id.
	 * 
	 * @param tour the id of the current {@code Processor} agent tour
	 */
	public synchronized void setTour(int tour) {
		this.tour = tour;
	}

	/**
	 * Allows to get a {@code Processor} agent current request state.
	 * 
	 * @param id the id of the {@code Processor} agent
	 * @return {@code true} if the {@code Processor} agent is currently requesting
	 *         the access, {@code false} otherwise
	 */
	public synchronized boolean getRequest(int id) {
		return this.requests[id - 1];
	}

	/**
	 * Allows to define a {@code Processor} agent current request state.
	 * 
	 * @param id    the id of the {@code Processor} agent
	 * @param value {@code true} if the {@code Processor} agent wants to request the
	 *              access, {@code false} if he wants to remove it
	 */
	public synchronized void setRequest(int id, boolean value) {
		this.requests[id - 1] = value;
	}

	/**
	 * Allows to get the total request number possible as the number of concurrent
	 * {@code Processor} agents.
	 * 
	 * @return the total request number possible
	 */
	public synchronized int getNbRequests() {
		return this.requests.length;
	}

}
