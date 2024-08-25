package com.xiaodao.batch.migrate.repository;

import com.xiaodao.batch.migrate.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaodaojiang
 * @Classname CustomerRepository
 * @Version 1.0.0
 * @Date 2024-08-25 22:46
 * @Created by xiaodaojiang
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
