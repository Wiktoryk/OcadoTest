package app;

import java.math.BigDecimal;
import java.util.*;

public class PaymentOptimizer {
    private Map<String, PaymentMethod> availableMethods;

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
        if (points != null && points.canAfford(originalValue.multiply(BigDecimal.ONE.subtract(points.getDiscountPercent())))) {
            BigDecimal discount = points.getDiscountPercent();
            BigDecimal finalPrice = originalValue.multiply(BigDecimal.ONE.subtract(discount));
            options.add(new PaymentOption("PUNKTY", finalPrice.setScale(2), originalValue.setScale(2), finalPrice.setScale(2)));
        }

        //mieszane
        else if (points != null && points.getLimit().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal tenPercent = originalValue.multiply(BigDecimal.valueOf(0.09));
            if (points.canAfford(tenPercent)) {
                for (PaymentMethod method : availableMethods.values()) {
                    if (method.getID().equals("PUNKTY")) continue;
                    BigDecimal pointsPart = points.getLimit().min(originalValue.multiply(BigDecimal.valueOf(0.9)));
                    BigDecimal restToPay = originalValue.multiply(BigDecimal.valueOf(0.9)).subtract(pointsPart);
                    if (restToPay.compareTo(BigDecimal.ZERO) < 0) continue;
                    if (method.canAfford(restToPay)) {
                        BigDecimal totalPaid = pointsPart.add(restToPay);
                        options.add(new PaymentOption(
                                Arrays.asList("PUNKTY", method.getID()),totalPaid.setScale(2), originalValue.setScale(2), pointsPart.setScale(2)));
                    }
                }
            }
        }

        //karta
        for (PaymentMethod method : availableMethods.values()) {
            if (method.getID().equals("PUNKTY")) continue;
            if (method.canAfford(originalValue)) {
                BigDecimal discount = BigDecimal.ZERO;
                if (order.getPromotions().contains(method.getID())) {
                    discount = method.getDiscountPercent();
                }
                BigDecimal finalPrice = originalValue.multiply(BigDecimal.ONE.subtract(discount));
                options.add(new PaymentOption(method.getID(), finalPrice.setScale(2), originalValue.setScale(2), BigDecimal.ZERO));
            }
        }
        return options.stream().sorted(Comparator.comparing(PaymentOption::getDiscountAmount).reversed().thenComparing(PaymentOption::getTraditionalSpending)).findFirst();
    }

    public void subtractLimit(String id, BigDecimal amount) {
        availableMethods.get(id).subtractFromLimit(amount);
    }
}
