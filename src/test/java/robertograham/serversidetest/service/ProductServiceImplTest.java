package robertograham.serversidetest.service;

import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import robertograham.serversidetest.domain.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("ProductServiceImpl.calculateProductsUnitPriceGross successful")
    void calculateProductsUnitPriceGrossSuccess() {
        final List<Product> products = new ArrayList<>();
        assertEquals(productService.calculateProductsUnitPriceGross(products), BigDecimal.ZERO);
        products.add(new Product("", 0, BigDecimal.ONE, ""));
        assertEquals(productService.calculateProductsUnitPriceGross(products), BigDecimal.ONE);
        products.add(new Product("", 0, new BigDecimal(9), ""));
        assertEquals(productService.calculateProductsUnitPriceGross(products), BigDecimal.TEN);
    }

    @Test
    @DisplayName("ProductServiceImpl.calculateProductsUnitPriceGross throws NPE")
    void calculateProductsUnitPriceGrossThrowsNPE() {
        assertThrows(
            NullPointerException.class,
            () -> productService.calculateProductsUnitPriceGross(null),
            "products cannot be null"
        );
    }

    @Test
    @DisplayName("ProductServiceImpl.calculateProductsUnitPriceVat successful")
    void calculateProductsUnitPriceVatSuccess() {
        final List<Product> products = new ArrayList<>();
        assertEquals(productService.calculateProductsUnitPriceVat(products), BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY));
        products.add(new Product("", 0, new BigDecimal(5), ""));
        assertEquals(productService.calculateProductsUnitPriceVat(products), new BigDecimal("0.83"));
        products.add(new Product("", 0, BigDecimal.ZERO, ""));
        assertEquals(productService.calculateProductsUnitPriceVat(products), new BigDecimal("0.83"));
    }

    @Test
    @DisplayName("ProductServiceImpl.calculateProductsUnitPriceVat throws NPE")
    void calculateProductsUnitPriceVatThrowsNPE() {
        assertThrows(
            NullPointerException.class,
            () -> productService.calculateProductsUnitPriceVat(null),
            "products cannot be null"
        );
    }
}
