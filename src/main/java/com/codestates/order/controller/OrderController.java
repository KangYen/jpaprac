package com.codestates.order.controller;

import com.codestates.member.service.MemberService;
import com.codestates.order.dto.OrderPatchDto;
import com.codestates.order.dto.OrderPostDto;
import com.codestates.order.mapper.OrderMapper;
import com.codestates.order.service.OrderService;
import com.codestates.response.SingleResponseDto;
import com.codestates.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Order;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/v11/orders")
public class OrderController {
    private final static String ORDER_DEFAULT_URL = "/v11/orders";
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final MemberService memberService;

    public OrderController(OrderService orderService,
                           OrderMapper mapper, MemberService memberService) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto) {
        Order order = orderService.createOrder(mapper.orderPostDtoToOrder(orderPostDto));
        //requestBody로 받는 건 orderPostDto, 근데 서비스계층인 createOrder()에서 전달 받는 것은 order이기 때문에
        //orderpostdto 를 order로 바꾸어 주는 것
        URI location = UriCreator.createUri(ORDER_DEFAULT_URL, order.getOrderId());

        return ResponseEntity.created(location).build();
        // 리턴 타입이 ResponseEntity니까 ResponseEntity 객체를 만들려면 build()가 있어야함
        // ResponseEntity 반환 객체를 생성해 줍니다. created() 메소드는 반환 객체에 대한 response 타입을 결정하는데, created로 할 경우
        // 201 코드를 반환해 줍니다. 201 코드는 POST 요청과 같이 서버의 리소스를 추가했을 때에 대한 응답 코드이며,
        // 201 응답 코드를 클라이언트 단에서 받게 되면, 정상 처리 되었다는 것을 알수 있게 됩니다.


    }

    @PatchMapping("/{order-id}")
    public ResponseEntity patchOrder (@PathVariable("order-id") @Positive long orderId,
                                      @Valid @RequestBody OrderPatchDto orderPatchDto) {
        orderPatchDto.setOrderId(orderId);
        Order order = orderService.updateOrder(mapper.orderPatchDtoToOrder(orderPatchDto));
        //postOrder(...)에서 request body로 받는게 OrderPostDto인데 서비스 계층인 createOrder()에서 전달 받는건 Order이기 때문에
        // mapper.orderPostDtoToOrder(orderPostDto)를 이용해서
        // OrderPostDto를 Order로 바꾸어 주는거에요.
        //Order order = orderService.createOrder(mapper.orderPostDtoToOrder(orderPostDto));
        //--> 이걸 풀어서 써보면
        //Order convertedOrder = mapper.orderPostDtoToOrder(orderPostDto);
        //Order order = orderService.createOrder(convertedOrder);
        //요렇게 볼 수 있는데 줄여서 저렇게 썼다고 생각하면 되어요
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(order)), HttpStatus.OK);

        // 여기에서 orderId를 세팅해주는 이유는 >
    }
}
