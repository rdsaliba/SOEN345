package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import static org.junit.Assert.*;

import java.util.Date;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link PetValidator}
 *
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class PetValidatorTests {

    @Test
    public void testValidate() {

        PetValidator petValidator = new PetValidator();
        Errors errors = mock(Errors.class);

        Pet mockPet1 = mock(Pet.class);
        Pet mockPet2 = mock(Pet.class);

        // Test with no errors
        when(mockPet1.getName()).thenReturn("Dover");
        when(mockPet1.isNew()).thenReturn(false);
        when(mockPet1.getType()).thenReturn(new PetType());
        when(mockPet1.getBirthDate()).thenReturn(new Date());

        petValidator.validate(mockPet1, errors);
        verify(errors, times(0)).rejectValue("name", "required", "required");
        verify(errors, times(0)).rejectValue("type", "required", "required");
        verify(errors, times(0)).rejectValue("birthDate", "required", "required");

        // Test with incorrect parameters
        when(mockPet2.getName()).thenReturn(null);
        when(mockPet2.isNew()).thenReturn(true);
        when(mockPet2.getType()).thenReturn(null);
        when(mockPet2.getBirthDate()).thenReturn(null);

        petValidator.validate(mockPet2, errors);
        verify(errors, times(1)).rejectValue("name", "required", "required");
        verify(errors, times(1)).rejectValue("type", "required", "required");
        verify(errors, times(1)).rejectValue("birthDate", "required", "required");
    }

    @Test
    public void testSupport() {
        // Only pet instances are accepts through the supports method.
        PetValidator petValidator = new PetValidator();
        assertFalse(petValidator.supports(Owner.class));
        assertTrue(petValidator.supports(Pet.class));
    }

}
