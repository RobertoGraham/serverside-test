package robertograham.serversidetest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import robertograham.serversidetest.domain.Product;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class ExportServiceImplTest {

    private static final String PRODUCT_ONE_TITLE = "one title";
    private static final String PRODUCT_TWO_TITLE = "two title";
    private static final int PRODUCT_ONE_KCALS = 0;
    private static final int PRODUCT_TWO_KCALS = 5;
    private static final BigDecimal PRODUCT_ONE_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal PRODUCT_TWO_UNIT_PRICE = new BigDecimal(3);
    private static final String PRODUCT_ONE_DESCRIPTION = "one description";
    private static final String PRODUCT_TWO_DESCRIPTION = "two description";
    private final ExportServiceImpl exportService;

    ExportServiceImplTest() {
        exportService = new ExportServiceImpl();
    }

    @Test
    @DisplayName("ExportServiceImpl.getJsonObjectFromProducts returns correct JsonObject")
    void getProductsReturnsPopulatedList() throws IOException {
        final List<Product> products = Stream.of(
            new Product(PRODUCT_ONE_TITLE, PRODUCT_ONE_KCALS, PRODUCT_ONE_UNIT_PRICE, PRODUCT_ONE_DESCRIPTION),
            new Product(PRODUCT_TWO_TITLE, PRODUCT_TWO_KCALS, PRODUCT_TWO_UNIT_PRICE, PRODUCT_TWO_DESCRIPTION)
        )
            .collect(Collectors.toList());
        final JsonObject jsonObject = exportService.getJsonObjectFromProducts(products);
        final JsonArray results = jsonObject.getJsonArray("results");
        final JsonObject total = jsonObject.getJsonObject("total");
        assertNotNull(results);
        assertNotNull(total);
        assertEquals(results.size(), 2);
        assertEquals(total.getJsonNumber("gross")
            .bigDecimalValue(), new BigDecimal(5));
        assertEquals(total.getJsonNumber("vat")
            .bigDecimalValue(), new BigDecimal("0.83"));
        final JsonObject productOne = results.getJsonObject(0);
        assertEquals(productOne.getString("title"), PRODUCT_ONE_TITLE);
        assertNull(productOne.get("kcal_per_100g"));
        assertEquals(productOne.getJsonNumber("unit_price").bigDecimalValue(), PRODUCT_ONE_UNIT_PRICE);
        assertEquals(productOne.getString("description"), PRODUCT_ONE_DESCRIPTION);
        final JsonObject productTwo = results.getJsonObject(1);
        assertEquals(productTwo.getString("title"), PRODUCT_TWO_TITLE);
        assertEquals(productTwo.getJsonNumber("kcal_per_100g").intValue(), PRODUCT_TWO_KCALS);
        assertEquals(productTwo.getJsonNumber("unit_price").bigDecimalValue(), PRODUCT_TWO_UNIT_PRICE);
        assertEquals(productTwo.getString("description"), PRODUCT_TWO_DESCRIPTION);
    }

    @Test
    @DisplayName("ExportServiceImpl.getJsonObjectFromProducts throws NPE")
    void getJsonObjectFromProductsThrowsNPE() {
        assertThrows(
            NullPointerException.class,
            () -> exportService.getJsonObjectFromProducts(null),
            "products cannot be null"
        );
    }
}
