package com.xiaodao.jta;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.xiaodao.jta.entity.member.Member;
import com.xiaodao.jta.entity.order.Order;
import com.xiaodao.jta.repository.member.MemberRepository;
import com.xiaodao.jta.repository.order.OrderRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
public class JtaAtomikosApplication {

    public static void main(String[] args) {
        SpringApplication.run(JtaAtomikosApplication.class, args);
    }


    @RestController
    public static class MallController {

        @Autowired
        private MemberRepository memberRepository;


        @GetMapping("members")
        public List<Member> members() {
            return memberRepository.findAll();
        }

        @PutMapping("member")
        public Member member(Member member) {
            return memberRepository.save(member);
        }

    }


    @RestController
    public static class OrderController {

        @Autowired
        private OrderRepository orderRepository;


        @GetMapping("orders")
        public List<Order> orders() {
            return orderRepository.findAll();
        }

        @PutMapping("order")
        public Order order(Order order) {
            return orderRepository.save(order);
        }

    }

    @RestController
    public static class MemberOrderController {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        @Qualifier("entityManagerPrimary")
        private EntityManager entityManager;


        @GetMapping("memberOrders")
        public List<MemberOrder> memberOrders() {
            final List<Order> orders = orderRepository.findAll();
            final List<Member> members = memberRepository.findAll();

            return members.stream().map(member -> {
                final MemberOrder memberOrder = new MemberOrder();
                memberOrder.setMember(member);
                memberOrder.setOrder(orders.stream().filter(order ->
                        order.getId().equals(member.getId())).findFirst().orElse(null));
                return memberOrder;
            }).collect(Collectors.toList());
        }

        @PutMapping("memberOrder")
        @Transactional(transactionManager = "jtaTransactionManager")
        public MemberOrder memberOrder(@RequestBody MemberOrder memberOrder, @RequestParam Optional<Boolean> rollback) {
            final Member member = memberRepository.save(memberOrder.getMember());
            final Order order = orderRepository.save(memberOrder.getOrder());

            /**
             *
             * 本地事务下，基于不同 connection，不同 entityManager(sessionImpl) 是不可见（事务可见性，mvcc）
             * jta 事务下，不同 entityManager(sessionImpl) 是可见的，
             *
             */
            // final Member findByEM = entityManager.find(Member.class, member.getId());
            // Assert.isTrue(findByEM.equals(member), "member not equals");

            if (rollback.orElse(false)) {
                throw new RuntimeException("rollback");
            }
            return memberOrder;
        }

    }

    @Data
    public static class MemberOrder {
        private Member member;
        private Order order;

        @JsonUnwrapped
        public Member getMember() {
            return member;
        }

        @JsonUnwrapped
        public Order getOrder() {
            return order;
        }
    }
}
