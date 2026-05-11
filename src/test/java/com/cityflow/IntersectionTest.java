package com.cityflow;

import com.cityflow.model.Intersection;
import com.cityflow.model.TrafficSignal;
import com.cityflow.model.Vehicle;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Intersection class
 */
public class IntersectionTest {
    
    private Intersection intersection;
    
    @BeforeEach
    public void setUp() {
        intersection = new Intersection(0, 0);
    }
    
    @AfterEach
    public void tearDown() {
        if (intersection != null && intersection.isRunning()) {
            intersection.stop();
        }
    }
    
    @Test
    public void testIntersectionInitialization() {
        assertEquals("0,0", intersection.getIntersectionId());
        assertEquals(0, intersection.getX());
        assertEquals(0, intersection.getY());
        assertNotNull(intersection.getCurrentSignal());
        assertEquals(0, intersection.getQueueLength());
    }
    
    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle("TEST-1", 10);
        boolean added = intersection.addVehicle(vehicle);
        
        assertTrue(added);
        assertTrue(intersection.getQueueLength() > 0);
    }
    
    @Test
    public void testGreenDurationAdjustment() {
        int originalDuration = intersection.getGreenDuration();
        intersection.adjustGreenDuration(12000);
        
        assertEquals(12000, intersection.getGreenDuration());
        
        // Test bounds
        intersection.adjustGreenDuration(1000); // Too low
        assertEquals(3000, intersection.getGreenDuration());
        
        intersection.adjustGreenDuration(20000); // Too high
        assertEquals(15000, intersection.getGreenDuration());
    }
    
    @Test
    public void testCongestionLevel() {
        double congestion = intersection.getCongestionLevel();
        assertTrue(congestion >= 0.0 && congestion <= 1.0);
    }
    
    @Test
    public void testIntersectionThreadExecution() throws InterruptedException {
        Thread thread = new Thread(intersection);
        thread.start();
        
        Thread.sleep(1000); // Let it run for 1 second
        assertTrue(intersection.isRunning());
        
        intersection.stop();
        Thread.sleep(500);
        
        assertFalse(intersection.isRunning());
    }
}
