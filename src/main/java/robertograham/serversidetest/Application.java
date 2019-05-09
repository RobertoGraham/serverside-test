package robertograham.serversidetest;

import robertograham.serversidetest.domain.Product;
import robertograham.serversidetest.service.ExportService;
import robertograham.serversidetest.service.ProductService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Application {

    private Application() {
    }

    public static void main(final String[] args) {
        final JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(Stream.of(new SimpleEntry<>(JsonGenerator.PRETTY_PRINTING, true))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
        try (final ProductService productService = ProductService.newProductService();
             final StringWriter stringWriter = new StringWriter();
             final JsonWriter jsonWriter = jsonWriterFactory.createWriter(stringWriter)) {
            final List<Product> products = productService.getProducts();
            final ExportService exportService = ExportService.newExportService();
            final JsonObject exportJsonObject = exportService.getJsonObjectFromProducts(products);
            jsonWriter.writeObject(exportJsonObject);
            System.out.println(stringWriter.toString());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}