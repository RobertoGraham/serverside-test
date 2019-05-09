package robertograham.serversidetest.service;

import robertograham.serversidetest.domain.Product;

import javax.json.JsonObject;
import java.util.List;

public interface ExportService {

    static ExportService newExportService() {
        return new ExportServiceImpl();
    }

    JsonObject getJsonObjectFromProducts(final List<Product> products);
}
