package org.springframework.samples.petclinic.owner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.visit.Visit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test class for {@link Pet}
 *
 * @author Laxman
 */
@RunWith(MockitoJUnitRunner.class)
public class PetTests {

    @Mock
    private Visit visit;

    @Mock
    private PetType petType;

    @Mock
    private Owner owner;

    private Pet pet;
    private Set<Visit> visitSet;

    @Before
    public void setup() {
        pet = new Pet();
    }

    @Test
    public void testPetMockDependencies(){
        Date testDate=null;
        SimpleDateFormat testDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        try{
            testDate = testDateFormat.parse("2018-02-24");

        }catch (ParseException e){
            System.out.println("Parse Error");
        }

        when(visit.getDate()).thenReturn(testDate);
        when(visit.getPetId()).thenReturn(123);
        when(visit.getDescription()).thenReturn("Testing Pet Class");

        pet.setId(123);
        pet.addVisit(visit);

        // Verify that Visit is called
        verify(visit).setPetId(123);

        // Test getVisitInternal
        assertEquals(1, pet.getVisitsInternal().size());
        assertEquals(testDate, pet.getVisitsInternal().iterator().next().getDate());
        assertEquals(123, pet.getVisitsInternal().iterator().next().getPetId().longValue());
        assertEquals("Testing Pet Class", pet.getVisitsInternal().iterator().next().getDescription());

        // Test getVisit
        assertEquals(1, pet.getVisits().size());
        assertEquals(testDate, pet.getVisits().iterator().next().getDate());
        assertEquals(123, pet.getVisits().iterator().next().getPetId().longValue());
        assertEquals("Testing Pet Class", pet.getVisits().iterator().next().getDescription());
    }

    @Test
    public void testPetGetterSettter(){

        Date testDate = null;
        SimpleDateFormat testDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        try{
            testDate = testDateFormat.parse("2018-02-24");

        }catch (ParseException e){
            System.out.println("Parse Error");
        }

        pet.setBirthDate(testDate);
        pet.setType(petType);
        pet.setOwner(owner);

        pet.getBirthDate();
        pet.getType();
        pet.getOwner();

        // Testing Mocks
        when(owner.getCity()).thenReturn("Laval");
        assertEquals("Laval", pet.getOwner().getCity());

        when(petType.getName()).thenReturn("Big Dogs");
        assertEquals("Big Dogs", pet.getType().getName());

        // Testing Birthdate
        assertEquals(testDate.toString(), pet.getBirthDate().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void petExceptionIsThrown() {
        pet.addVisit(visit);
        List<Visit> testVisit = pet.getVisits();
        testVisit.add(visit);
    }

}
