package com.safestep.platform.commerce.interfaces.rest;

import com.safestep.platform.commerce.application.queryservices.CommerceQueryService;
import com.safestep.platform.commerce.domain.model.queries.*;
import com.safestep.platform.commerce.interfaces.rest.transform.CommerceResourceAssembler;
import com.safestep.platform.shared.application.services.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/commerce", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Commerce Catalog", description = "Products, categories, kits and commerce recommendation endpoints")
public class CommerceCatalogController {

    private final CommerceQueryService commerceQueryService;
    private final CurrentUserService currentUserService;

    public CommerceCatalogController(CommerceQueryService commerceQueryService, CurrentUserService currentUserService) {
        this.commerceQueryService = commerceQueryService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/products")
    @Operation(summary = "Get all store products")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity
                .ok(commerceQueryService.handle(new GetProductsQuery()).stream()
                        .map(CommerceResourceAssembler::toResource).toList());
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get store product by identifier")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return commerceQueryService.handle(new GetProductByIdQuery(productId))
                .map(CommerceResourceAssembler::toResource)
                .<ResponseEntity<?>> map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    @Operation(summary = "Get product categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetCategoriesQuery()));
    }

    @GetMapping("/kits")
    @Operation(summary = "Get emergency kits")
    public ResponseEntity<?> getEmergencyKits() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetEmergencyKitsQuery()));
    }

    @GetMapping("/coupons")
    @Operation(summary = "Get redeemable coupons")
    public ResponseEntity<?> getCoupons() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetCouponsQuery()));
    }

    @GetMapping("/recommendations/me")
    @Operation(summary = "Get current user personalized product recommendations")
    public ResponseEntity<?> getMyRecommendations() {
        return ResponseEntity.ok(commerceQueryService.handle(new GetRecommendationsQuery(currentUserService.username())));
    }
}
