package org.springframework.samples.petclinic.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Person}
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonTests {

    private Person person;

    @Before
    public void setup() {
        person = new Person();
    }

    @Test
    public void testFirstNameGetterSetter() {
        // Test with nothing set
        assertEquals(person.getFirstName(), null);

        // Test with string
        person.setFirstName("Peter");
        assertEquals(person.getFirstName(), "Peter");

        // Test with empty string
        person.setFirstName("");
        assertEquals(person.getFirstName(), "");
    }

    @Test
    public void testLastNameGetterSetter() {
        // Test with nothing set
        assertEquals(person.getLastName(), null);

        // Test with string
        person.setLastName("Rigby");
        assertEquals(person.getLastName(), "Rigby");

        // Test with empty string
        person.setLastName("");
        assertEquals(person.getLastName(), "");
    }
}
