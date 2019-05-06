package robertograham.serversidetest.client;

import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import robertograham.serversidetest.client.domain.Product;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class ClientImpl implements Client {

    private final CloseableHttpClient httpClient;

    ClientImpl(final CloseableHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient cannot be null");
    }

    @Override
    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Override
    public List<Product> getProducts() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "ClientImpl{" +
            "httpClient=" + httpClient +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof ClientImpl))
            return false;
        final ClientImpl client = (ClientImpl) object;
        return httpClient.equals(client.httpClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpClient);
    }
}
