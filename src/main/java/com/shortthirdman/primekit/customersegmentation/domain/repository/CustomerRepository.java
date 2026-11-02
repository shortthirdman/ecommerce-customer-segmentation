package com.shortthirdman.primekit.customersegmentation.domain.repository;

import com.shortthirdman.primekit.customersegmentation.domain.entity.Customer;
import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;

@Repository
public interface CustomerRepository extends BasicRepository<Customer, String> {
}
