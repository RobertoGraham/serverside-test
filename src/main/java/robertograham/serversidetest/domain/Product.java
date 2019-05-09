package robertograham.serversidetest.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {

    private final String title;
    private final int kiloCaloriesPerHundredGrams;
    private final BigDecimal unitPrice;
    private final String description;

    public Product(final String title,
                   final int kiloCaloriesPerHundredGrams,
                   final BigDecimal unitPrice,
                   final String description) {
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.kiloCaloriesPerHundredGrams = kiloCaloriesPerHundredGrams;
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice cannot be null");
        this.description = Objects.requireNonNull(description, "description cannot be null");
    }

    public String getTitle() {
        return title;
    }

    public int getKiloCaloriesPerHundredGrams() {
        return kiloCaloriesPerHundredGrams;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Product{" +
            "title='" + title + '\'' +
            ", kiloCaloriesPerHundredGrams=" + kiloCaloriesPerHundredGrams +
            ", unitPrice=" + unitPrice +
            ", description='" + description + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Product))
            return false;
        final Product product = (Product) object;
        return kiloCaloriesPerHundredGrams == product.kiloCaloriesPerHundredGrams &&
            title.equals(product.title) &&
            unitPrice.equals(product.unitPrice) &&
            description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, kiloCaloriesPerHundredGrams, unitPrice, description);
    }
}
