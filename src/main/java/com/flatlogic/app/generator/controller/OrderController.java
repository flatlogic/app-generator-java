package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.OrderRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.OrderDto;
import com.flatlogic.app.generator.entity.Order;
import com.flatlogic.app.generator.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * OrderController REST controller.
 */
@RestController
@RequestMapping("orders")
public class OrderController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    /**
     * OrderService instance.
     */
    @Autowired
    private OrderService orderService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get orders.
     *
     * @param modelAttribute GetModelAttribute
     * @return Order RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<OrderDto>> getOrders(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get orders.");
        RowsData<OrderDto> rowsData = new RowsData<>();
        List<Order> orders = orderService.getOrders(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<OrderDto> orderDtos = orders.stream().map(order -> defaultConversionService
                .convert(order, OrderDto.class)).collect(Collectors.toList());
        rowsData.setRows(orderDtos);
        rowsData.setCount(orderDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get order by id.
     *
     * @param id Order Id
     * @return Order
     */
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        LOGGER.info("Get order by id.");
        return Optional.ofNullable(orderService.getOrderById(id))
                .map(order -> new ResponseEntity<>(defaultConversionService.convert(order,
                        OrderDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save order.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Order
     */
    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@RequestBody RequestData<OrderRequest> requestData,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save order.");
        Order order = orderService.saveOrder(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(order, OrderDto.class), HttpStatus.OK);
    }

    /**
     * Update order.
     *
     * @param id          Order id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Order
     */
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable UUID id, @RequestBody RequestData<OrderRequest> requestData,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update order.");
        Order order = orderService.updateOrder(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(order, OrderDto.class), HttpStatus.OK);
    }

    /**
     * Delete order.
     *
     * @param id Order id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        LOGGER.info("Delete order.");
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
