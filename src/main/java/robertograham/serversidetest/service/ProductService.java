package robertograham.serversidetest.service;

import org.apache.http.impl.client.HttpClients;
import robertograham.serversidetest.domain.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends AutoCloseable {

    static ProductService newProductService() {
        return new ProductServiceImpl(HttpClients.createDefault());
    }

    @Override
    void close();

    List<Product> getProducts() throws IOException;

    BigDecimal calculateProductsUnitPriceGross(final List<Product> products);

    BigDecimal calculateProductsUnitPriceVat(final List<Product> products);
}
