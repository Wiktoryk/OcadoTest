package app;

import java.math.BigDecimal;
import java.util.*;

public class PaymentOptimizer {
    private final Map<String, PaymentMethod> availableMethods;

    public PaymentOptimizer(List<PaymentMethod> methods) {
        this.availableMethods = new HashMap<>();
        for (PaymentMethod method : methods) {
            availableMethods.put(method.getID(), method);
        }
    }

    public Optional<PaymentOption> chooseBestOption(Order order) {
        BigDecimal originalValue = order.getValue();
        List<PaymentOption> options = new ArrayList<>();

        //tylko pkt
        PaymentMethod points = availableMethods.get("PUNKTY");
        if (points != null && points.getLimit().compareTo(originalValue) >= 0) {
            BigDecimal discount = points.getDiscountPercent();
            BigDecimal finalPrice = originalValue.multiply(BigDecimal.ONE.subtract(discount));
            options.add(new PaymentOption("PUNKTY", finalPrice, originalValue));
        }

        //mieszane
        if (points != null) {
            BigDecimal tenPercent = originalValue.multiply(BigDecimal.valueOf(0.1));
            if (points.getLimit().compareTo(tenPercent) >= 0) {
                for (PaymentMethod method : availableMethods.values()) {
                    if (method.getID().equals("PUNKTY")) continue;
                    BigDecimal afterDiscount = originalValue.multiply(BigDecimal.valueOf(0.9));
                    if (method.getLimit().compareTo(afterDiscount.subtract(tenPercent)) >= 0) {
                        options.add(new PaymentOption(
                                Arrays.asList("PUNKTY", method.getID()),originalValue.multiply(BigDecimal.valueOf(0.9)), originalValue));
                    }
                }
            }
        }

        //karta
        for (PaymentMethod method : availableMethods.values()) {
            if (method.getID().equals("PUNKTY")) continue;
            if (method.getLimit().compareTo(originalValue) >= 0) {
                BigDecimal discount = BigDecimal.ZERO;
                if (order.getPromotions().contains(method.getID())) {
                    discount = method.getDiscountPercent();
                }
                BigDecimal finalPrice = originalValue.multiply(BigDecimal.ONE.subtract(discount));
                options.add(new PaymentOption(method.getID(), finalPrice, originalValue));
            }
        }
        return options.stream().sorted(Comparator.comparing(PaymentOption::getDiscountAmount).reversed().thenComparing(PaymentOption::getTraditionalSpending)).findFirst();
    }
}
