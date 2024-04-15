package com.xiaodao.multids;

import com.xiaodao.multids.entity.member.Member;
import com.xiaodao.multids.entity.order.Order;
import com.xiaodao.multids.repository.member.MemberRepository;
import com.xiaodao.multids.repository.order.OrderRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
public class JpaMultiDsPureApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaMultiDsPureApplication.class, args);
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
        @Transactional
        public MemberOrder memberOrder(@RequestBody MemberOrder memberOrder, @RequestParam Optional<Boolean> rollback) {
            final Member member = memberRepository.save(memberOrder.getMember());
            final Order order = orderRepository.save(memberOrder.getOrder());

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
    }
}
