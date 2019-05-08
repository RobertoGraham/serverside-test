package robertograham.serversidetest;

import robertograham.serversidetest.client.Client;
import robertograham.serversidetest.client.domain.Product;

import java.io.IOException;
import java.util.List;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        try (final Client client = Client.newClient()) {
            final List<Product> products = client.getProducts();
            products.forEach(System.out::println);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}
