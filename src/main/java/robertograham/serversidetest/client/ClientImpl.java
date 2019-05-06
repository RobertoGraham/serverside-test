package robertograham.serversidetest.client;

import robertograham.serversidetest.client.domain.Product;

import java.util.Collections;
import java.util.List;

final class ClientImpl implements Client {

    @Override
    public void close() {

    }

    @Override
    public List<Product> getProducts() {
        return Collections.emptyList();
    }
}
