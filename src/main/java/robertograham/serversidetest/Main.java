package robertograham.serversidetest;

import robertograham.serversidetest.client.Client;
import robertograham.serversidetest.client.domain.Product;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        final Map<String, Boolean> jsonWriterFactoryProperties = Stream.of(new SimpleEntry<>(JsonGenerator.PRETTY_PRINTING, true))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        final JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(jsonWriterFactoryProperties);
        try (final Client client = Client.newClient();
             final StringWriter stringWriter = new StringWriter();
             final JsonWriter jsonWriter = jsonWriterFactory.createWriter(stringWriter)) {
            final List<Product> products = client.getProducts();
            final JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            products.stream()
                .map(product -> {
                    final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                    jsonObjectBuilder.add("title", product.getTitle());
                    if (product.getKiloCaloriesPerHundredGrams() > 0)
                        jsonObjectBuilder.add("kcal_per_100g", product.getKiloCaloriesPerHundredGrams());
                    jsonObjectBuilder.add("unit_price", product.getUnitPrice());
                    jsonObjectBuilder.add("description", product.getDescription());
                    return jsonObjectBuilder;
                })
                .forEach(jsonArrayBuilder::add);
            final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("results", jsonArrayBuilder);
            jsonObjectBuilder.add("total", Json.createObjectBuilder()
                .add("gross", client.calculateProductsUnitPriceGross(products))
                .add("vat", client.calculateProductsUnitPriceVat(products)));
            jsonWriter.writeObject(jsonObjectBuilder.build());
            System.out.println(stringWriter.toString());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}