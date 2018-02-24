/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
public class VetTests {

    @Test
    public void testSerialization() {
        Vet vet = new Vet();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setId(123);
        Vet other = (Vet) SerializationUtils
                .deserialize(SerializationUtils.serialize(vet));
        assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
        assertThat(other.getLastName()).isEqualTo(vet.getLastName());
        assertThat(other.getId()).isEqualTo(vet.getId());
    }
    
    @Test
    public void testGetSpecialtiesSorted() {
    	Vet vet = new Vet();
    	
		Specialty mockSpeciality1 = mock(Specialty.class);
		Specialty mockSpeciality2 = mock(Specialty.class);
		Specialty mockSpeciality3 = mock(Specialty.class);
		
		mockSpeciality1.setName("Dog");
		mockSpeciality2.setName("Cat");
		mockSpeciality3.setName("Bear");
		
		when(mockSpeciality1.getName()).thenReturn("Dog");
		when(mockSpeciality2.getName()).thenReturn("Cat");
		when(mockSpeciality3.getName()).thenReturn("Bear");
		
		vet.addSpecialty(mockSpeciality1);
		vet.addSpecialty(mockSpeciality2);
		vet.addSpecialty(mockSpeciality3);
		
		List<Specialty> sortedSpecialties = new ArrayList<>(3);
		sortedSpecialties.add(mockSpeciality3);
		sortedSpecialties.add(mockSpeciality2);
		sortedSpecialties.add(mockSpeciality1);
		assertEquals(sortedSpecialties, vet.getSpecialties());
    }
    
    @Test
    public void testGetNrSpecialties() {
    	Vet vet = new Vet();
    	
		Specialty mockSpeciality1 = mock(Specialty.class);
		Specialty mockSpeciality2 = mock(Specialty.class);
		Specialty mockSpeciality3 = mock(Specialty.class);
		
		vet.addSpecialty(mockSpeciality1);
		vet.addSpecialty(mockSpeciality2);
		vet.addSpecialty(mockSpeciality3);
	
		assertEquals(3, vet.getNrOfSpecialties());
    }
    
    @Test
    public void testAddSpecialty() {
    	Vet vet = new Vet();
    	
		Specialty mockSpeciality1 = mock(Specialty.class);
		assertEquals(0, vet.getNrOfSpecialties());
		
		vet.addSpecialty(mockSpeciality1);
		assertEquals(1, vet.getNrOfSpecialties());
    }
    
    @Test
    public void testGetSpecialtiesInteral() {
    	Vet vet = new Vet();
    	
		Specialty mockSpeciality1 = mock(Specialty.class);
		assertNotEquals(null, vet.getSpecialties());
		assertEquals(0, vet.getSpecialties().size());
		
		vet.addSpecialty(mockSpeciality1);
		assertEquals(mockSpeciality1, vet.getSpecialties().get(0));
    }

}
