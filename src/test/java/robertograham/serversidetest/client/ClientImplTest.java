package robertograham.serversidetest.client;

import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import robertograham.serversidetest.client.domain.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class ClientImplTest {

    private final ClientImpl client;

    ClientImplTest() {
        client = new ClientImpl(HttpClients.createDefault());
    }

    @Test
    @DisplayName("ClientImpl.getProducts returns populated list")
    void getProductsReturnsPopulatedList() {
        final List<Product> products = client.getProducts();
        assertNotNull(products, "ClientImpl.getProducts returned null");
        assertFalse(products.isEmpty(), "ClientImpl.getProducts returned empty list");
    }
}
