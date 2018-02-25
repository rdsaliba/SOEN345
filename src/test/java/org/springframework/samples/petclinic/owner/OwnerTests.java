package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test class for {@link Owner}
 *
 * @author Mazen
 */
public class OwnerTests {
	
	 private Owner owner;

	    @Before
	    public void setup() {
	        owner = new Owner();
	    }

	    @Test
	    public void testAddressGetterSetter() {
	        // Test with nothing set
	        assertEquals(owner.getAddress(), null);

	        // Test with string
	        owner.setAddress("hello");
	        assertEquals(owner.getAddress(), "hello");

	        // Test with empty string
	        owner.setAddress("");
	        assertEquals(owner.getAddress(), "");
	    }

	    @Test
	    public void testCityGetterSetter() {
	        // Test with nothing set
	        assertEquals(owner.getCity(), null);

	        // Test with string
	        owner.setCity("Montreal");
	        assertEquals(owner.getCity(), "Montreal");

	        // Test with empty string
	        owner.setCity("");
	        assertEquals(owner.getCity(), "");
	    }
	    
	    @Test
	    public void testTelephoneGetterSetter() {
	        // Test with nothing set
	        assertEquals(owner.getTelephone(), null);

	        // Test with string
	        owner.setTelephone("514-111-1111");
	        assertEquals(owner.getTelephone(), "514-111-1111");

	        // Test with empty string
	        owner.setTelephone("");
	        assertEquals(owner.getTelephone(), "");
	    }
}
