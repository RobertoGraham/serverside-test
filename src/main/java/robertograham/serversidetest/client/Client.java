package robertograham.serversidetest.client;

import robertograham.serversidetest.client.domain.Product;

import java.util.List;

public interface Client extends AutoCloseable {

    static Client newClient() {
        return new ClientImpl();
    }

    List<Product> getProducts();
}
