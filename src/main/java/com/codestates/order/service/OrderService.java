package com.codestates.order.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import com.codestates.order.entity.Order;
import com.codestates.order.repository.OrderRepository;
import com.codestates.stamp.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;

    public OrderService(MemberService memberService,
                        OrderRepository orderRepository,
                        CoffeeService coffeeService) {
        this.memberService = memberService;
        this.orderRepository = orderRepository;
        this.coffeeService = coffeeService;
    }

    public Order createOrder(Order order) {
        verifyOrder(order);
        Order saveOrder = saveOrder(order);

        updateStamp(saveOrder);
        return  saveOrder;

        public Order updateOrder(Order order) {
            Order findOrder = findVerifiedOrder(Order.getOrderId());

            Optional.ofNullable(order.getOrderStatus())
                    .ifPresent(OrderStatus -> findOrder.setOrderStatus(orderStatus));
            return saveOrder(findOrder);
        }

        public Order findOrder(long orderId) {  return findVerifiedOrder(orderId);}

        public Page<Order> findOrders(int page, int size) {
            return orderRepository.findAll(PageRequest.of(page, size,
                    Sort.by("orderId").descending()));
        }

        public void cancelOrder(long orderId) {
            Order findOrder = findVerifiedOrder(orderId);
            int step = findOrder.getOrderCoffees().getStepNumber();

            // OrderStatus의 step 이 2 이상일 경우(ORDER_CONFIRM)에는 주문 취소가 되지 않도록 한다.
            if (step >= 2) {
                throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
            }
            findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
            orderRepository.save(findOrder);
        }

        private Order findVerifiedOrder(long orderId) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            Order findOrder =
                    optionalOrder.orElseThrow(() ->
                            new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
            return findOrder;
        }

        private void verifyOrder(Order order) {
            //회원이 존재하는지 확인
            memberService.findVerifiedMember(Order.getMember().getMemberId());

            // 커피가 존재하는지 확인
            order.getOrderCoffees().stream()
                    .forEach(orderCoffee -> coffeeService.
                            findVerifiedCoffee(orderCoffee.getCoffee(),getCoffeeId()));
        }

        private void updateStamp(Order order) {
            Member member = memberService.findMember(order,getMember().getMemberId());
            int stampCount = calculateStampCount(order);

            Stamp stamp = member.getStamp();
            stamp.setStampCount(stamp.getStampCount() + stampCount);
            member.setStamp(stamp);

            memberService.updateMember(member);
        }


        }
}
