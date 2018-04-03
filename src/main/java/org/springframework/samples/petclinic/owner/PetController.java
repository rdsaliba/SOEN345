/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.owner;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.database.ConsistencyChecker;
import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.HashGenerationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    private final PetRepository pets;
    private final OwnerRepository owners;

    @Autowired
    public PetController(PetRepository pets, OwnerRepository owners) {
        this.pets = pets;
        this.owners = owners;
    }

    @Autowired
    PetService petService;
    
    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() throws HashGenerationException {
        // find owners by last name
//        List<PetType> petTypes = petService.findPetTypes(Database.PRIMARY);
        // Shadow read
        List<PetType> petTypes2 = petService.findPetTypes(Database.SECONDARY);
//        System.out.println(petTypes);
//        System.out.println(petTypes2);
//        ConsistencyChecker cc = new ConsistencyChecker("Types");
//        cc.checkReadConsistency(petTypes, petTypes2, "Types");
        return this.pets.findPetTypes();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int ownerId) {
        return this.owners.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("pet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PetValidator());
    }

    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, ModelMap model) {
        Pet pet = new Pet();
        owner.addPet(pet);
        model.put("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) throws HashGenerationException {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.addPet(pet);
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            //Regular write
//            petService.saveNew(Database.PRIMARY, pet);
            //Shadow write
            petService.saveNew(Database.SECONDARY, pet);
//            ConsistencyChecker cc = new ConsistencyChecker("Pets");
//            cc.checkConsistency("Pets");
            return "redirect:/owners/{ownerId}";
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) throws HashGenerationException {
        // find owners by last name
//        Pet pet1 = petService.findById(Database.PRIMARY, petId);
        // Shadow read
        Pet pet2 = petService.findById(Database.SECONDARY, petId);
//        ConsistencyChecker cc = new ConsistencyChecker("Pets");
//        cc.checkReadConsistency(pet1, pet2, "Pets");
        
        Pet pet = this.pets.findById(petId);
        model.put("pet", pet);
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model) throws HashGenerationException {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            owner.addPet(pet);
            //Regular write
//            petService.update(Database.PRIMARY, pet);
            //Shadow write
            petService.update(Database.SECONDARY, pet);
//            ConsistencyChecker cc = new ConsistencyChecker("Pets");
//            cc.checkConsistency("Pets");
            return "redirect:/owners/{ownerId}";
        }
    }

}
