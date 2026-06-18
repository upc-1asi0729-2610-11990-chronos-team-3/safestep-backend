package com.safestep.platform.commerce.interfaces.rest;

import com.safestep.platform.commerce.application.commandservices.CommerceCommandService;
import com.safestep.platform.commerce.application.queryservices.CommerceQueryService;
import com.safestep.platform.commerce.domain.model.commands.AddCartItemCommand;
import com.safestep.platform.commerce.domain.model.commands.CreateOrderCommand;
import com.safestep.platform.commerce.domain.model.commands.UpdateCartItemCommand;
import com.safestep.platform.commerce.domain.model.queries.*;
import com.safestep.platform.commerce.interfaces.rest.resources.AddCartItemResource;
import com.safestep.platform.commerce.interfaces.rest.resources.CreateOrderResource;
import com.safestep.platform.commerce.interfaces.rest.resources.UpdateCartItemResource;
import com.safestep.platform.commerce.interfaces.rest.transform.CommerceResourceAssembler;
import com.safestep.platform.shared.application.services.CurrentUserService;
import com.safestep.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/commerce", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Commerce Operations", description = "Cart, order, address and payment endpoints")
public class CommerceOperationsController {

    private final CommerceQueryService commerceQueryService;
    private final CommerceCommandService commerceCommandService;
    private final CurrentUserService currentUserService;

    public CommerceOperationsController(CommerceQueryService commerceQueryService,
            CommerceCommandService commerceCommandService, CurrentUserService currentUserService) {
        this.commerceQueryService = commerceQueryService;
        this.commerceCommandService = commerceCommandService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/cart/me")
    @Operation(summary = "Get current user cart")
    public ResponseEntity<?> getMyCart() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetCartByUsernameQuery(currentUserService.username()))
                .stream().map(CommerceResourceAssembler::toResource).toList());
    }

    @PostMapping("/cart/items")
    @Operation(summary = "Add item to current user cart")
    public ResponseEntity<?> addCartItem(@RequestBody AddCartItemResource payload) {
        var result = commerceCommandService
                .handle(new AddCartItemCommand(currentUserService.username(), payload.productId(), payload.quantity()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, CommerceResourceAssembler::toResource,
                HttpStatus.CREATED);
    }

    @PutMapping("/cart/items/{itemId}")
    @Operation(summary = "Update current user cart item")
    public ResponseEntity<?> updateCartItem(@PathVariable String itemId, @RequestBody UpdateCartItemResource payload) {
        var result = commerceCommandService
                .handle(new UpdateCartItemCommand(currentUserService.username(), itemId, payload.quantity()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, CommerceResourceAssembler::toResource,
                HttpStatus.OK);
    }

    @DeleteMapping("/cart/items/{itemId}")
    @Operation(summary = "Delete current user cart item")
    public ResponseEntity<?> deleteCartItem(@PathVariable String itemId) {
        commerceCommandService.deleteCartItem(currentUserService.username(), itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/me")
    @Operation(summary = "Get current user orders")
    public ResponseEntity<?> getMyOrders() {
        return ResponseEntity.ok(commerceQueryService
                .handle(new GetOrdersByUsernameQuery(currentUserService.username())).stream()
                .map(CommerceResourceAssembler::toResource).toList());
    }

    @PostMapping("/orders")
    @Operation(summary = "Create current user order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderResource payload) {
        var result = commerceCommandService
                .handle(new CreateOrderCommand(currentUserService.username(), payload.status()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, CommerceResourceAssembler::toResource,
                HttpStatus.CREATED);
    }

    @GetMapping("/shipping-addresses/me")
    @Operation(summary = "Get current user shipping addresses")
    public ResponseEntity<?> getMyShippingAddresses() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetShippingAddressesQuery(currentUserService.username())));
    }

    @GetMapping("/payment-methods")
    @Operation(summary = "Get available payment methods")
    public ResponseEntity<?> getPaymentMethods() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetPaymentMethodsQuery()));
    }
}
