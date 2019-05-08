package robertograham.serversidetest.client;

import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import robertograham.serversidetest.client.domain.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class ClientImplTest {

    private final ClientImpl client;

    ClientImplTest() {
        client = new ClientImpl(HttpClients.createDefault());
    }

    @Test
    @DisplayName("ClientImpl.getProducts returns populated list")
    void getProductsReturnsPopulatedList() throws IOException {
        final List<Product> products = client.getProducts();
        assertNotNull(products, "ClientImpl.getProducts returned null");
        assertFalse(products.isEmpty(), "ClientImpl.getProducts returned empty list");
    }

    @Test
    @DisplayName("ClientImpl.calculateProductsUnitPriceGross successful")
    void calculateProductsUnitPriceGrossSuccess() {
        final List<Product> products = new ArrayList<>();
        assertEquals(client.calculateProductsUnitPriceGross(products), BigDecimal.ZERO);
        products.add(new Product("", 0, BigDecimal.ONE, ""));
        assertEquals(client.calculateProductsUnitPriceGross(products), BigDecimal.ONE);
        products.add(new Product("", 0, new BigDecimal(9), ""));
        assertEquals(client.calculateProductsUnitPriceGross(products), BigDecimal.TEN);
    }

    @Test
    @DisplayName("ClientImpl.calculateProductsUnitPriceGross throws NPE")
    void calculateProductsUnitPriceGrossThrowsNPE() {
        assertThrows(
            NullPointerException.class,
            () -> client.calculateProductsUnitPriceGross(null),
            "products cannot be null"
        );
    }

    @Test
    @DisplayName("ClientImpl.calculateProductsUnitPriceVat successful")
    void calculateProductsUnitPriceVatSuccess() {
        final List<Product> products = new ArrayList<>();
        assertEquals(client.calculateProductsUnitPriceVat(products), BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY));
        products.add(new Product("", 0, new BigDecimal(5), ""));
        assertEquals(client.calculateProductsUnitPriceVat(products), new BigDecimal("0.83"));
        products.add(new Product("", 0, BigDecimal.ZERO, ""));
        assertEquals(client.calculateProductsUnitPriceVat(products), new BigDecimal("0.83"));
    }

    @Test
    @DisplayName("ClientImpl.calculateProductsUnitPriceVat throws NPE")
    void calculateProductsUnitPriceVatThrowsNPE() {
        assertThrows(
            NullPointerException.class,
            () -> client.calculateProductsUnitPriceVat(null),
            "products cannot be null"
        );
    }
}
