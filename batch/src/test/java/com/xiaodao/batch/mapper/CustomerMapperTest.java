package com.xiaodao.batch.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.Collections;

import com.xiaodao.batch.dto.*;
import org.junit.jupiter.api.Test;

public class CustomerMapperTest {

    @Test
    public void testMapDtoToEntity() {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(10L);
        customerDto.setCustomerName("Filip");
        OrderItemDto order1 = new OrderItemDto();
        order1.setName("Table");
        order1.setQuantity(2L);
        customerDto.setOrders(new ArrayList<>(Collections.singleton(order1)));

        Customer customer = CustomerMapper.MAPPER.toCustomer( customerDto );

        assertThat( customer.getId() ).isEqualTo( 10 );
        assertThat( customer.getName() ).isEqualTo( "Filip" );
        assertThat( customer.getOrderItems() )
            .extracting( "name", "quantity" )
            .containsExactly( tuple( "Table", 2L ) );
    }

    @Test
    public void testMapDtoToEntityNoSetter() {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(10L);
        customerDto.setCustomerName("Filip");
        OrderItemDto order1 = new OrderItemDto();
        order1.setName("Table");
        order1.setQuantity(2L);
        customerDto.setOrders(new ArrayList<>(Collections.singleton(order1)));

        NoGetterSetterCustomer customer = CustomerMapper.MAPPER.toNoSetterCustomer( customerDto );

        assertThat( customer.id ).isEqualTo( 10 );
        assertThat( customer.name ).isEqualTo( "Filip" );
        assertThat( customer.orderItems )
                .extracting( "name", "quantity" )
                .containsExactly( tuple( "Table", 2L ) );
    }

    @Test
    public void testEntityDtoToDto() {

        Customer customer = new Customer();
        customer.setId( 10L );
        customer.setName( "Filip" );
        OrderItem order1 = new OrderItem();
        order1.setName( "Table" );
        order1.setQuantity( 2L );
        customer.setOrderItems( Collections.singleton( order1 ) );

        CustomerDto customerDto = CustomerMapper.MAPPER.fromCustomer( customer );

        assertThat(customerDto.getId()).isEqualTo( 10 );
        assertThat(customerDto.getCustomerName()).isEqualTo( "Filip" );
        assertThat(customerDto.getOrders())
            .extracting( "name", "quantity" )
            .containsExactly( tuple( "Table", 2L ) );
    }
}