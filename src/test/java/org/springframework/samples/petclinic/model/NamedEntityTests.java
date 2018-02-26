package org.springframework.samples.petclinic.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class NamedEntityTests {

	@Test
	public void testNameGetterSetter(){
		NamedEntity entity = new NamedEntity();
		assertEquals(null, entity.getName());
		entity.setName("John");
        assertEquals("John", entity.getName());
        entity.setName("");
        assertEquals("", entity.getName());
	}
	
	@Test
	public void testToString(){
		NamedEntity entity = new NamedEntity();
		entity.setName("John");
        assertEquals("John", entity.toString());
	}
}
