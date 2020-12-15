package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.OrdersRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.OrdersDto;
import com.flatlogic.app.generator.entity.Orders;
import com.flatlogic.app.generator.service.OrdersService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * OrdersController REST controller.
 */
@RestController
@RequestMapping("orders")
public class OrdersController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    /**
     * OrdersService instance.
     */
    @Autowired
    private OrdersService ordersService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get orders.
     *
     * @param modelAttribute GetModelAttribute
     * @return Orders RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<OrdersDto>> getOrders(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get orders.");
        RowsData<OrdersDto> rowsData = new RowsData<>();
        List<Orders> orders = ordersService.getOrders(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<OrdersDto> ordersDtos = orders.stream().map(__orders -> defaultConversionService.
                convert(__orders, OrdersDto.class)).collect(Collectors.toList());
        rowsData.setRows(ordersDtos);
        rowsData.setCount(ordersDtos.size());
        return new ResponseEntity<>(rowsData, HttpStatus.OK);
    }

    /**
     * Get orders.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Orders
     */
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getOrdersAutocomplete(@RequestParam String query,
                                                                          @RequestParam Integer limit) {
        LOGGER.info("Get orders (autocomplete).");
        List<Orders> orders = ordersService.getOrders(query, limit);
        List<AutocompleteData> wrappers = orders.stream().map(__orders ->
                new AutocompleteData(__orders.getId(), __orders.getImportHash())).collect(Collectors.toList());
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    /**
     * Get orders by id.
     *
     * @param id Orders Id
     * @return Orders
     */
    @GetMapping("{id}")
    public ResponseEntity<OrdersDto> getOrdersById(@PathVariable UUID id) {
        LOGGER.info("Get orders by id.");
        return Optional.ofNullable(ordersService.getOrdersById(id))
                .map(orders -> new ResponseEntity<>(defaultConversionService
                        .convert(orders, OrdersDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save orders.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Orders
     */
    @PostMapping
    public ResponseEntity<OrdersDto> saveOrders(@RequestBody RequestData<OrdersRequest> requestData,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save orders.");
        Orders orders = ordersService.saveOrders(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(orders, OrdersDto.class), HttpStatus.OK);
    }

    /**
     * Update orders.
     *
     * @param id          Orders id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Orders
     */
    @PutMapping("{id}")
    public ResponseEntity<OrdersDto> updateOrders(@PathVariable UUID id,
                                                    @RequestBody RequestData<OrdersRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update orders.");
        Orders orders = ordersService.updateOrders(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(orders, OrdersDto.class), HttpStatus.OK);
    }

    /**
     * Delete orders.
     *
     * @param id Orders id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrders(@PathVariable UUID id) {
        LOGGER.info("Delete orders.");
        ordersService.deleteOrders(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
