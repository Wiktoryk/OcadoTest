package app;

import java.math.BigDecimal;
import java.util.List;

public class PaymentOption {
    private final List<String> methods;
    private final BigDecimal finalCost;
    private final BigDecimal originalValue;

    public PaymentOption(String method, BigDecimal finalPrice, BigDecimal originalValue) {
        this.methods = List.of(method);
        this.finalCost = finalPrice;
        this.originalValue = originalValue;
    }

    public PaymentOption(List<String> methods, BigDecimal finalPrice, BigDecimal originalValue) {
        this.methods = methods;
        this.finalCost = finalPrice;
        this.originalValue = originalValue;
    }

    public BigDecimal getDiscountAmount() {
        return originalValue.subtract(finalCost);
    }

    public List<String> getMethods() {
        return methods;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public BigDecimal getOriginalValue() {
        return originalValue;
    }

    public BigDecimal getTraditionalSpending() {
        if (methods.contains("PUNKTY") && methods.size() == 2) {
            return finalCost.subtract(originalValue.multiply(BigDecimal.valueOf(0.1)));
        }
        else if (methods.contains("PUNKTY")) {
            return BigDecimal.ZERO;
        }
        return finalCost;
    }
}
