package robertograham.serversidetest.client;

import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import robertograham.serversidetest.client.domain.Product;

import java.util.Collections;
import java.util.List;

final class ClientImpl implements Client {

    private final CloseableHttpClient httpClient;

    ClientImpl(final CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Override
    public List<Product> getProducts() {
        return Collections.emptyList();
    }
}
