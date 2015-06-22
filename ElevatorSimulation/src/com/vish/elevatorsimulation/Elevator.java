package com.vish.elevatorsimulation;

/**
 * @author Vish
 * 
 * Elevator class - simulates an elevator
 * 
 */
import java.util.TreeSet;

public class Elevator implements Runnable {
	public static int DOOR_OPEN_TIME = 2000;

	private ElevatorController elevatorController;
	private boolean full = false;
	private int capacity;
	private int numPassengers = 0;
	private int id;
	private int currentFloor;
	private DirectionEnum currentDirection;
	private boolean isDoorOpen;

	// up/down requests maintained in sorted tree set
	private TreeSet<Integer> requestsToGoUp;
	private TreeSet<Integer> requestsToGoDown;

	public Elevator(int id, int numFloors, int capacity, ElevatorController elevatorController){
		this.elevatorController = elevatorController;
		full = false;
		this.capacity = capacity;
		this.id = id;	
		this.requestsToGoUp = new TreeSet<Integer>();
		this.requestsToGoDown = new TreeSet<Integer>();
		reset();
	}

	// go back to first floor and wait
	public void reset() {
		currentFloor = 0;
		currentDirection = DirectionEnum.UP;
	}

	/**
	 * Indicates if elevator is full or not
	 * 
	 * @return boolean
	 */
	public synchronized boolean hasRoom(){
		return !full;
	}

	/**
	 * get id of elevator
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * simulates passenger entering the elevator
	 * and updates passenger count
	 * 
	 * @param passengerId
	 */
	public void enter(int passengerId){
		synchronized(this) {
			numPassengers++;
			System.out.println("Passenger " + passengerId + " enters elevator on floor " + this.currentFloor);
		}
	}

	/**
	 * Passenger exits elevator
	 * 
	 * @param passengerId - passenger id
	 */	
	public void exit(int passengerId){
		System.out.println("Passenger " + passengerId + " has exited elevator on floor " + this.currentFloor);
	}

	/**
	 * Method to request elevator to a particular floor
	 * 
	 * @param floor - floor passenger wants to move to
	 * @param passengerId - passenger id
	 * 
	 */
	public void requestFloor (int floor, int passengerId) {
		if (floor == this.currentFloor) // you are on the floor you want to be
			return;
		boolean goingUp = floor > this.currentFloor;
		addFloorRequests(goingUp, floor, passengerId);
		waitForRequestFloor(floor, goingUp, passengerId);
	}

	/**
	 * This method adds the passenger service requests to the TreeSet
	 * @param goingUp - up or down
	 * @param floorRequested - requested floor
	 * @param passengerId - id of the passenger 
	 */

	public void addFloorRequests(boolean goingUp, int floorRequested, int passengerId) {
		synchronized(this) {
			if (goingUp) {
				this.requestsToGoUp.add(floorRequested);
				System.out.println("Elevator " + this.id + " receives request from passenger " + passengerId + " to go up to floor " + floorRequested);
			} else {
				this.requestsToGoDown.add(floorRequested);
				System.out.println("Elevator " + this.id + " receives request from passenger " + passengerId + " to go down to floor " + floorRequested);
			}
			notifyAll();
		}
	}

	/**
	 * Wait till elevator reaches floor. Then notify all threads 
	 * that the floor has been reached
	 * 
	 * @param floorRequested
	 * @param goingUp
	 * @param passengerId
	 */
	public void waitForRequestFloor (int floorRequested, boolean goingUp, int passengerId){
		synchronized (this){
			// wait till elevator reaches floor
			while (currentFloor != floorRequested) {
				try {
					wait();
				} catch (InterruptedException ie) {

				}
			}
			notifyAll();
		}

	}

	/**
	 * Method directing elevator to go to a particular floor
	 * @param floor
	 * 
	 */
	public synchronized void visitFloor(int floor){
		if (currentDirection == DirectionEnum.UP){
			this.requestsToGoUp.remove(floor);
		} else {
			this.requestsToGoDown.remove(floor);
		}
		this.currentFloor = floor;
		if(currentDirection == DirectionEnum.UP){
			System.out.println("Elevator " + this.id + " arrives on floor " + this.currentFloor);
		} else {
			System.out.println("Elevator " + this.id + " arrives on floor " + this.currentFloor);
		}
		this.numPassengers--;
		notifyAll();
	}

	/**
	 * Elevator gets the floor of its next request
	 * @return nearest floor of the next request
	 */
	private synchronized int getNextFloor(){
		if (currentDirection == DirectionEnum.UP) {
			Integer next = this.requestsToGoUp.higher(this.currentFloor);
			boolean hasNext = next != null;
			if (hasNext) {
				System.out.println("Elevator " + this.id + " going up, processes request from passenger to go up to floor " + next);
				return next;
			} else { // look for requests in the down direction
				currentDirection = DirectionEnum.DOWN;
				next = this.requestsToGoDown.lower(this.currentFloor);
				boolean hasNextRequestFromLowerFloor = next != null;
				if (hasNextRequestFromLowerFloor){
					System.out.println("Elevator " + this.id + " going down, processes request from passenger to go down to floor " + next);
					return next;
				} else {
					return -1;
				}
			}
		} else {
			Integer next = this.requestsToGoDown.lower(this.currentFloor);
			boolean hasNext = next != null;
			if (hasNext){
				System.out.println("Elevator " + this.id + " going down, processes request from passenger to go down to floor " + next);
				return next;
			} else {
				currentDirection = DirectionEnum.UP;
				this.currentFloor = 0;
				next = this.requestsToGoUp.higher(this.currentFloor);
				boolean hasNextRequestFromHigherFloor = next != null;
				if (hasNextRequestFromHigherFloor){
					System.out.println("Elevator " + this.id + "  going up, processes request from passenger to go up to floor " + next);
					return next;
				} else {
					return -1;
				}
			}
		}
	}

	/**
	 * Method to simulate opening the elevator door
	 * 
	 */
	public void openDoor(){
		synchronized(this) {
			this.isDoorOpen = true;
			System.out.println("Elevator opens doors on floor " + this.currentFloor);
			try {
				Thread.sleep(DOOR_OPEN_TIME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to simulate closing the elevator door
	 * 
	 */
	public void closeDoor(){
		synchronized(this) {
			this.isDoorOpen = false;
			System.out.println("Elevator " + this.id +" doors closed on " + this.currentFloor);
		}
	}

	/**
	 * run method for the Elevator class
	 */
	public void run() {
		while(true){
			if(Thread.interrupted()) return;

			int nextFloor = getNextFloor();
			boolean noMoreRequests = this.requestsToGoUp.isEmpty() && this.requestsToGoDown.isEmpty();

			if (noMoreRequests){
				synchronized (this){
					this.currentFloor = 0;
					System.out.println ("No more requests, current floor="  + currentFloor);
					try {
						System.out.println("Elevator " + this.id + " is waiting for passenger requests");
						wait();
					} catch (InterruptedException e){
						return;
					}
				}
			} else if (nextFloor != -1){
				System.out.println("Elevator " + this.id + " moving up to floor " + nextFloor);
				visitFloor(nextFloor);
				openDoor();
				closeDoor();
			}
		}
	}

} 