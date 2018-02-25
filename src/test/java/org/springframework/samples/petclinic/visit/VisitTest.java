package org.springframework.samples.petclinic.visit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link Visit}
 * @author rsaliba
 *
 */
@RunWith(MockitoJUnitRunner.class)

public class VisitTest {
	@Mock
	private Visit visit;

	@Before
	public void setup() {
		visit = mock(Visit.class);
	}

	@Test
	public void testDateGetterSetter() {

		Date testDate=null;
		SimpleDateFormat testDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
		try{
			testDate = testDateFormat.parse("2018-02-24");

		}catch (ParseException e){
			System.out.println("Parse Error");
		}

		when(visit.getDate()).thenReturn(testDate);
		assertEquals(testDate, visit.getDate());

	}

	@Test
	public void testDescriptionGetterSetter() {
		//Test with nothing set
		when(visit.getDescription()).thenReturn(null);
		visit.setDescription(null);
		assertEquals(visit.getDescription(), null);

		when(visit.getDescription()).thenReturn("This is a visit");

		//Test with string
		visit.setDescription("This is a visit");
		assertEquals(visit.getDescription(), "This is a visit");

	}

	@Test
	public void testPetIdGetterSetter() {
		when(visit.getPetId()).thenReturn(null);
		//Test with nothing set
		visit.setPetId(null);
		assertEquals(visit.getPetId(), null);

		//Test with object
		when(visit.getPetId()).thenReturn(23);
		visit.setPetId(23);
		assertEquals(visit.getPetId(), (Integer)23);
	}






}
