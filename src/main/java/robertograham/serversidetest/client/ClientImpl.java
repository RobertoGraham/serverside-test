package robertograham.serversidetest.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import robertograham.serversidetest.client.domain.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

final class ClientImpl implements Client {

    private static final URI PRODUCT_LIST_URI = URI.create("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");
    private final CloseableHttpClient httpClient;

    ClientImpl(final CloseableHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient cannot be null");
    }

    @Override
    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Override
    public List<Product> getProducts() throws IOException {
        final String productsPageHtml = getProductsPageHtml();
        final Document productsDocument = Jsoup.parse(productsPageHtml);
        final Elements productElements = productsDocument.select(".product");
        return productElements.stream()
            .map(this::getProductFromProductElement)
            .collect(Collectors.toList());
    }

    private Product getProductFromProductElement(final Element productElement) {
        final BigDecimal unitPrice = getUnitPriceFromProductElement(productElement);
        final String title = getTitleFromProductElement(productElement);
        final String productPageLink = getProductPageLinkFromProductElement(productElement);
        try {
            final String productPageHtml = getResponseBody(PRODUCT_LIST_URI.resolve(productPageLink));
            final Document productDocument = Jsoup.parse(productPageHtml);
            final int kiloCaloriesPerHundredGrams = getKiloCaloriesPerHundredGramsFromProductDocument(productDocument);
            final String description = getDescriptionFromProductDocument(productDocument);
            return new Product(title, kiloCaloriesPerHundredGrams, unitPrice, description);
        } catch (final IOException exception) {
            throw new IllegalStateException("Could extract html for product: " + title, exception);
        }
    }

    private BigDecimal getUnitPriceFromProductElement(final Element productElement) {
        final String unitPriceString = Optional.ofNullable(productElement.selectFirst(".pricePerUnit"))
            .map(Element::text)
            .orElse("£0.00/unit");
        return new BigDecimal(unitPriceString.substring(1, unitPriceString.indexOf("/")));
    }

    private String getTitleFromProductElement(final Element productElement) {
        return Optional.ofNullable(productElement.selectFirst(".productNameAndPromotions"))
            .map(Element::text)
            .orElse("");
    }

    private String getProductPageLinkFromProductElement(final Element productElement) {
        return Optional.ofNullable(productElement.selectFirst(".productNameAndPromotions a"))
            .map(productPageAnchorElement -> productPageAnchorElement.attr("href"))
            .orElse("");
    }

    private String getProductsPageHtml() throws IOException {
        return getResponseBody(PRODUCT_LIST_URI);
    }

    private int getKiloCaloriesPerHundredGramsFromProductDocument(final Document productDocument) {
        final String kiloCaloriesPerHundredGrams = Optional.ofNullable(productDocument.selectFirst(".tableRow0 .nutritionLevel1"))
            .map(Element::text)
            .orElse("0kcal");
        return new Integer(kiloCaloriesPerHundredGrams.substring(0, kiloCaloriesPerHundredGrams.indexOf("kcal")));
    }

    private String getDescriptionFromProductDocument(final Document productDocument) {
        return Optional.ofNullable(productDocument.selectFirst(".productText p"))
            .map(Element::text)
            .orElse("");
    }

    private String getResponseBody(final URI uri) throws IOException {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(RequestBuilder.get(uri)
            .build())) {
            final StatusLine statusLine = httpResponse.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            final HttpEntity httpEntity = httpResponse.getEntity();
            if (statusCode == HttpStatus.SC_OK)
                return httpEntity == null ?
                    ""
                    : Optional.ofNullable(EntityUtils.toString(httpEntity))
                    .orElse("");
            throw new HttpResponseException(statusCode, Optional.ofNullable(statusLine.getReasonPhrase())
                .orElse(""));
        }
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
