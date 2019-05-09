package robertograham.serversidetest.service;

import robertograham.serversidetest.domain.Product;

import javax.json.JsonObject;
import java.util.List;
import java.util.Objects;

final class ExportServiceImpl implements ExportService {

    ExportServiceImpl() {
    }

    @Override
    public JsonObject getJsonObjectFromProducts(final List<Product> products) {
        Objects.requireNonNull(products, "products cannot be null");
        return JsonObject.EMPTY_JSON_OBJECT;
    }

    @Override
    public String toString() {
        return "ExportServiceImpl{}";
    }
}
