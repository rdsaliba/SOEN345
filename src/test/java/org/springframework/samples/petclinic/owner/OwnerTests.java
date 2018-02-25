package org.springframework.samples.petclinic.owner;

import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
	    
	    @Test
	    public void testPetsIntervalGetterSetter() {
	    	
	    		// Mock Pets
	    		Pet mockPet1 = mock(Pet.class);
	    		Pet mockPet2 = mock(Pet.class);
	    		Pet mockPet3 = mock(Pet.class);
	    		Set<Pet> petSet = new HashSet<>();
	    		
	        // Test with nothing in set
	        assertEquals(owner.getPetsInternal(), petSet);

	        // Test with Pets in set
	        petSet.add(mockPet1);
	    		petSet.add(mockPet2);
	    		petSet.add(mockPet3);
	    		owner.setPetsInternal(petSet);
	    		assertEquals(owner.getPetsInternal(), petSet);
	    		
		}
		
		@Test
		public void testgetPets(){
			Owner Owner = new Owner();
    	
			Pet mockPet1 = mock(Pet.class);
			Pet mockPet2 = mock(Pet.class);
			Pet mockPet3 = mock(Pet.class);
			
			mockPet1.setName("Kanye");
			mockPet2.setName("Jay");
			mockPet3.setName("Pac");
			
			when(mockPet1.isNew()).thenReturn(true);
			when(mockPet2.isNew()).thenReturn(true);
			when(mockPet3.isNew()).thenReturn(true);
			
			when(mockPet1.getName()).thenReturn("Kanye");
			when(mockPet2.getName()).thenReturn("Jay");
			when(mockPet3.getName()).thenReturn("Pac");

			Owner.addPet(mockPet1);
			Owner.addPet(mockPet2);
			Owner.addPet(mockPet3);
		
			List<Pet> sortedPets = new ArrayList<>(3);
			sortedPets.add(mockPet2);
			sortedPets.add(mockPet1);
			sortedPets.add(mockPet3);
			System.out.println(sortedPets);
			System.out.println(Owner.getPets());
			assertEquals(sortedPets, Owner.getPets());
			
		}
}