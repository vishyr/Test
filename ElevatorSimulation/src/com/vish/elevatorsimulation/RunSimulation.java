package com.vish.elevatorsimulation;

import java.util.ArrayList;
import java.util.List;
/**
 * This class instantiates Elevators, Passengers and ElevatorController class
 * and calls start method on them
 * 
 * @author Vish
 *
 */
import java.util.Random;

public class RunSimulation {
	public static final int NUM_ELEVATORS = 1;
	public static final int NUM_FLOORS = 10;
	public static final int NUM_PASSENGERS = 1;	 
    public static int ELEVATOR_CAPACITY = 10;
    
	public static void main (String []args) {
		// create Elevator controller
		ElevatorController elevatorController = new ElevatorControllerImpl(NUM_FLOORS, NUM_ELEVATORS, ELEVATOR_CAPACITY);

		// generate random start and stop floors		
		Random startFloor = new Random();
		Random destFloor = new Random();
		
		List<Thread> passengerThreadList = new ArrayList<>();
		
		// create Passengers and start them
		for (int i = 1; i <= NUM_PASSENGERS; i++) {
			System.out.println ("Creating new passenger, id = " + i);
			Passenger passenger = new Passenger(i, elevatorController, startFloor.nextInt(NUM_FLOORS), destFloor.nextInt(NUM_FLOORS));    
			Thread p = new Thread(passenger);
			passengerThreadList.add(p);
			p.start();
		}

		// create Elevators and start them
		for (Elevator elevator : elevatorController.getElevators()) {    
			Thread e = new Thread(elevator);
			e.start();
		}

	}
}
