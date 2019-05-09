package robertograham.serversidetest.service;

import robertograham.serversidetest.domain.Product;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

final class ExportServiceImpl implements ExportService {

    private static final BigDecimal VAT = new BigDecimal("1.2");

    ExportServiceImpl() {
    }

    @Override
    public JsonObject getJsonObjectFromProducts(final List<Product> products) {
        Objects.requireNonNull(products, "products cannot be null");
        final JsonArrayBuilder resultsJsonArrayBuilder = Json.createArrayBuilder();
        products.stream()
            .map(this::getJsonObjectFromProduct)
            .forEach(resultsJsonArrayBuilder::add);
        return Json.createObjectBuilder()
            .add("results", resultsJsonArrayBuilder)
            .add("total", Json.createObjectBuilder()
                .add("gross", calculateProductsUnitPriceGross(products))
                .add("vat", calculateProductsUnitPriceVat(products)))
            .build();
    }

    private JsonObject getJsonObjectFromProduct(final Product product) {
        final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("title", product.getTitle());
        if (product.getKiloCaloriesPerHundredGrams() > 0)
            jsonObjectBuilder.add("kcal_per_100g", product.getKiloCaloriesPerHundredGrams());
        jsonObjectBuilder.add("unit_price", product.getUnitPrice());
        jsonObjectBuilder.add("description", product.getDescription());
        return jsonObjectBuilder.build();
    }

    private BigDecimal calculateProductsUnitPriceGross(final List<Product> products) {
        Objects.requireNonNull(products, "products cannot be null");
        return products.stream()
            .map(Product::getUnitPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateProductsUnitPriceVat(final List<Product> products) {
        Objects.requireNonNull(products, "products cannot be null");
        final BigDecimal gross = calculateProductsUnitPriceGross(products);
        return gross.subtract(gross.divide(VAT, 2, BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public String toString() {
        return "ExportServiceImpl{}";
    }
}
