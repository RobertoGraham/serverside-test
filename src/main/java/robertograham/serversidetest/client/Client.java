package robertograham.serversidetest.client;

import org.apache.http.impl.client.HttpClients;
import robertograham.serversidetest.client.domain.Product;

import java.util.List;

public interface Client extends AutoCloseable {

    static Client newClient() {
        return new ClientImpl(HttpClients.createDefault());
    }

    List<Product> getProducts();
}
