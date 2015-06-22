package com.vish.elevatorsimulation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ElevatorControllerImplTest {
	
	public static final int NUM_FLOORS = 4;	
	public static final int NUM_ELEVATORS = 1;
	public static final int ELEVATOR_CAPACITY = 10;
	ElevatorController ec;
	
	@Before
	public void setUp() throws Exception {
		ec = new ElevatorControllerImpl(NUM_FLOORS, NUM_ELEVATORS, ELEVATOR_CAPACITY);
	}

	@Test
	public void testCallElevator() {
		int floorNum = 6;
		int passengerId = 1;
		assertTrue((ec.callElevator(floorNum, DirectionEnum.DOWN, passengerId)) instanceof Elevator);
	}

}
