package com.xiaodao.batch.service;

import com.xiaodao.batch.migrate.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname CustomerService
 * @Version 1.0.0
 * @Date 2024-08-25 22:45
 * @Created by xiaodaojiang
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public <T> void saveAll(List<? extends T> items) {
        customerRepository.saveAll((List) items);
    }
}
