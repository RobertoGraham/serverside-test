package robertograham.serversidetest.service;

import org.apache.http.impl.client.HttpClients;
import robertograham.serversidetest.domain.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService extends AutoCloseable {

    static ProductService newProductService() {
        return new ProductServiceImpl(HttpClients.createDefault());
    }

    @Override
    void close();

    List<Product> getProducts() throws IOException;
}
