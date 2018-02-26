package org.springframework.samples.petclinic.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class BaseEntityTests {

	@Test
	public void testIdGetterSetter(){
		BaseEntity entity = new BaseEntity();
		assertEquals(null, entity.getId());
		entity.setId(0);
        assertEquals(Integer.valueOf(0), entity.getId());
        entity.setId(null);
        assertEquals(null, entity.getId());
	}
	
	@Test
	public void testIsNew(){
		BaseEntity entity = new BaseEntity();
		assertTrue(entity.isNew());
		entity.setId(1);
        assertFalse(entity.isNew());
	}
}
