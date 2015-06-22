package com.vish.elevatorsimulation;

/**
 * 
 * @author Vish
 *
 */
public class Passenger implements Runnable {

	private int passengerId;
	private ElevatorController elevatorController;
	private int startFloor;
	private int destFloor;

	/**
	 * 
	 * @param passengerId
	 * @param elevatorController
	 * @param startFloor
	 * @param destFloor
	 */
	public Passenger(int passengerId, ElevatorController elevatorController, int startFloor, int destFloor){
		this.passengerId = passengerId;
		this.elevatorController = elevatorController;
		this.startFloor = startFloor;
		this.destFloor = destFloor;
	}
    
	/**
	 * run method for the Passenger class
	 */
	public void run() {
		Elevator elevator;
		if (startFloor == destFloor)
			return;

		if(startFloor < destFloor){
			System.out.println("Passenger " + this.passengerId + " summons elevator to go up from floor " + startFloor + 
					" to floor " + destFloor);
			elevator = elevatorController.callElevator(startFloor, DirectionEnum.UP, passengerId);

		} else {
			System.out.println("Passenger " + this.passengerId + " summons elevator to go down from floor " + startFloor + 
					" to floor " + destFloor);
			elevator = elevatorController.callElevator(startFloor, DirectionEnum.DOWN, passengerId);
		}

		System.out.println("Elevator " + elevator.getId() + " arrived on floor " + startFloor + ", passenger " + this.passengerId + " ready to enter elevator " + elevator.getId());

		elevator.enter(this.passengerId);

		System.out.println("Passenger " + this.passengerId + " on elevator " + elevator.getId()+ " wants to go to floor " + destFloor);
		elevator.requestFloor(destFloor, this.passengerId);
		System.out.println("Passenger " + this.passengerId + " exits elevator " + elevator.getId() + " on floor " + destFloor);
		elevator.exit(this.passengerId);
	}
}
