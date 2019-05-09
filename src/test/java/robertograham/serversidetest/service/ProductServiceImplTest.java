package robertograham.serversidetest.service;

import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import robertograham.serversidetest.domain.Product;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class ProductServiceImplTest {

    private final ProductServiceImpl productService;

    ProductServiceImplTest() {
        productService = new ProductServiceImpl(HttpClients.createDefault());
    }

    @Test
    @DisplayName("ProductServiceImpl.getProducts returns populated list")
    void getProductsReturnsPopulatedList() throws IOException {
        final List<Product> products = productService.getProducts();
        assertNotNull(products, "ProductServiceImpl.getProducts returned null");
        assertFalse(products.isEmpty(), "ProductServiceImpl.getProducts returned empty list");
    }
}
