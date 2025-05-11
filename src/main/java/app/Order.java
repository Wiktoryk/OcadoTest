package app;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private final String ID;
    private final BigDecimal value;
    private final List<String> promotions;

    @JsonCreator
    public Order(@JsonProperty("id") String id, @JsonProperty("value") BigDecimal value, @JsonProperty("promotions") List<String> promotions) {
        this.ID = id;
        this.value = value;
        this.promotions = promotions == null ? Collections.emptyList() : promotions;
    }

    public String getID() {
        return ID;
    }

    public BigDecimal getValue() {
        return value;
    }

    public List<String> getPromotions() {
        return promotions;
    }
}
