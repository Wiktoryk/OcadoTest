package app;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethod {
    private String ID;
    private BigDecimal discount;
    private BigDecimal limit;

    @JsonCreator
    public PaymentMethod(@JsonProperty("id") String ID, @JsonProperty("discount") BigDecimal discount, @JsonProperty("limit") BigDecimal limit) {
        this.ID = ID;
        this.discount = discount;
        this.limit = limit;
    }

    public String getID() {
        return ID;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public BigDecimal getDiscountPercent() {
        return discount.divide(BigDecimal.valueOf(100));  //mnożenie przez odwrotność potencjalnie szybsze
    }

    public void subtractFromLimit(BigDecimal amount) {
        this.limit = this.limit.subtract(amount);
    }

    public boolean canAfford(BigDecimal amount) {
        return limit.compareTo(amount) >= 0;
    }
}
