package com.vish.elevatorsimulation;

/**
 * This class is used to simulate the Elevator Controller
 * 
 * @author Vish
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElevatorControllerImpl implements ElevatorController {
	private List<Elevator> elevatorList;
    private int numFloors;
    private int numElevators;
    private int elevatorCapacity;
    
    /**
     * 
     * @param numFloors
     * @param numElevators
     * @param elevatorCapacity
     */
	public ElevatorControllerImpl(int numFloors, int numElevators, int elevatorCapacity){
		this.elevatorList = new ArrayList<Elevator>();
		this.numFloors = numFloors;
		this.numElevators = numElevators;
	
        for (int i = 1; i <= numElevators; i++){
            elevatorList.add(new Elevator(i, this.numFloors, this.elevatorCapacity, this));
        }
	}
	
    /**
     * {@inheritDoc}
     */
	public List<Elevator> getElevators() {
		return this.elevatorList;
	}
	
    /**
     * {@inheritDoc}
     */
	public Elevator callElevator(int floor, DirectionEnum direction, int passengerId) {
		synchronized(this) {
			// pick random elevator
			int elevatorNum = new Random().nextInt(numElevators);
			// summon elevator to floor
			Elevator elevator = elevatorList.get(elevatorNum);
			elevator.requestFloor(floor, passengerId);
			return elevator;	
		}
	}
}
