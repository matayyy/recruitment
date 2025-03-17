package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.dto.CustomerDTO;
import com.mataycode.recruitment.dto.CustomerDTOMapper;
import com.mataycode.recruitment.dto.CustomerUpdateRequest;
import com.mataycode.recruitment.exception.DuplicateResourceException;
import com.mataycode.recruitment.exception.RequestValidationException;
import com.mataycode.recruitment.exception.ResourceNotFoundException;
import com.mataycode.recruitment.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
    }


    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] not found".formatted(id)));
    }

    public CustomerDTO getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email [%s] not found".formatted(email)));
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerDTOMapper)
                .toList();
    }

    //todo: ADD LOGIC TO SAVE AND UPDATE CUSTOMER_PROFILE_IMAGE_ID

    //    todo: CONSIDER OF RETURNING RESPONSE_ENTITY OR JUST THROW EXCEPTION
    public ResponseEntity<Void> deleteCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build(); //HTTP 404 Not Found
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //HTTP 204 No Content
    }

    @Transactional
    public void updateCustomer(Long id, CustomerUpdateRequest updateRequest) {
        //get customer
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] not found".formatted(id)));

        boolean changes = false;

        //check field name
        if (updateRequest.name() != null && !customerToUpdate.getName().equals(updateRequest.name())) {
            customerToUpdate.setName(updateRequest.name());
            changes = true;
        }

        //check field email
        if (updateRequest.email() != null && !customerToUpdate.getEmail().equals(updateRequest.email())) {
            //email format validation checked at CustomerUpdateRequest
            if (customerRepository.findCustomerByEmail(updateRequest.email())
                    .isPresent()) {
                throw new DuplicateResourceException("Email already taken");
            }
            customerToUpdate.setEmail(updateRequest.email());
            changes = true;
        }

        //check birthDate
        if (updateRequest.birthDate() != null && !customerToUpdate.getBirthDate().equals(updateRequest.birthDate())) {
            //data not in future checked at CustomerUpdateRequest
            customerToUpdate.setBirthDate(updateRequest.birthDate());
            changes = true;
        }

        //check gender
        if (updateRequest.gender() != null && !customerToUpdate.getGender().equals(updateRequest.gender())) {
            customerToUpdate.setGender(updateRequest.gender());
            changes = true;
        }

        //update customer or throw exception
        if (changes) {
            customerRepository.save(customerToUpdate);
        } else {
            throw new RequestValidationException("No data changes found");
        }
    }

    public void enableCustomer(String email) {
        customerRepository.enableCustomer(email);
    }

    //Private methods -------------------------

//    todo: CONSIDER OF REFACTORING AND USE IN UPDATE CUSTOMER
//    if (updateField(customerToUpdate.getName(), updateRequest.name(), customerToUpdate::setName)) {
//        changes = true;
//    }
//    private boolean updateField(String current, String newValue, Consumer<String> setter) {
//        if (newValue != null && !newValue.equals(current)) {
//            setter.accept(newValue);
//            return true;
//        }
//        return false;
//    }
}
