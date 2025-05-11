package app;

import java.math.BigDecimal;
import java.util.List;

public class PaymentOption {
    private final List<String> methods;
    private final BigDecimal finalCost;
    private final BigDecimal originalValue;
    private final BigDecimal pointsSpent;

    public PaymentOption(String method, BigDecimal finalPrice, BigDecimal originalValue, BigDecimal pointsSpent) {
        this.methods = List.of(method);
        this.finalCost = finalPrice;
        this.originalValue = originalValue;
        this.pointsSpent = pointsSpent;
    }

    public PaymentOption(List<String> methods, BigDecimal finalPrice, BigDecimal originalValue, BigDecimal pointsSpent) {
        this.methods = methods;
        this.finalCost = finalPrice;
        this.originalValue = originalValue;
        this.pointsSpent = pointsSpent;
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

    public BigDecimal getPointsSpent() {
        return pointsSpent;
    }

    public BigDecimal getTraditionalSpending() {
        if (methods.contains("PUNKTY") && methods.size() == 2) {
            return finalCost.subtract(pointsSpent);
        }
        else if (methods.contains("PUNKTY")) {
            return BigDecimal.ZERO;
        }
        return finalCost;
    }
}
