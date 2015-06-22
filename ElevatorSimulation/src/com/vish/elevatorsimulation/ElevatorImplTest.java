package com.vish.elevatorsimulation;

import org.junit.Before;

public class ElevatorImplTest {

	public static final int NUM_FLOORS = 4;	
	public static final int NUM_ELEVATORS = 1;
	public static final int ELEVATOR_CAPACITY = 10;
	Elevator elevator;
	ElevatorController ec;
	
	@Before
	public void setUp() throws Exception {
		ec = new ElevatorControllerImpl(NUM_FLOORS, NUM_ELEVATORS, ELEVATOR_CAPACITY);
		elevator = new Elevator(2, NUM_FLOORS, ELEVATOR_CAPACITY, ec);
	}
}
