package robertograham.serversidetest.client;

import org.apache.http.impl.client.HttpClients;
import robertograham.serversidetest.client.domain.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface Client extends AutoCloseable {

    static Client newClient() {
        return new ClientImpl(HttpClients.createDefault());
    }

    @Override
    void close();

    List<Product> getProducts() throws IOException;

    BigDecimal calculateProductsUnitPriceGross(final List<Product> products);

    BigDecimal calculateProductsUnitPriceVat(final List<Product> products);
}
