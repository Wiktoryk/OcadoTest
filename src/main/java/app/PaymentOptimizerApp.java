package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class PaymentOptimizerApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <orders.json> <paymentmethods.json>");
            System.exit(1);
        }

        File ordersFile = new File(args[0]);
        File methodsFile = new File(args[1]);

        ObjectMapper mapper = new ObjectMapper();

        List<Order> orders = mapper.readValue(ordersFile, new TypeReference<>() {});
        List<PaymentMethod> methods = mapper.readValue(methodsFile, new TypeReference<>() {});

        Map<String, BigDecimal> spent = new HashMap<>();

        PaymentOptimizer paymentOptimizer = new PaymentOptimizer(methods);

        for (Order order : orders) {
            Optional<PaymentOption> option = paymentOptimizer.chooseBestOption(order);
            if (option.isPresent()) {
                PaymentOption paymentOption = option.get();
                for (String method : option.get().getMethods()) {
                    if (method.equals("PUNKTY")) {
                        spent.merge(method, paymentOption.getFinalCost().subtract(paymentOption.getTraditionalSpending()), BigDecimal::add);
                    }
                    else {
                        spent.merge(method, paymentOption.getTraditionalSpending(), BigDecimal::add);
                    }
                }
            }


//            BigDecimal value = order.getValue();
//            boolean usedPoints = false;
//
//            PaymentMethod points = wallet.get("PUNKTY");
//            BigDecimal pointsUsed = BigDecimal.ZERO;
//
//            if (points != null && points.canAfford(value)) {
//                BigDecimal discount = points.getDiscountPercent();
//                BigDecimal discounted = value.multiply(BigDecimal.ONE.subtract(discount));
//                points.subtractFromLimit(value);
//                spent.merge("PUNKTY", value, BigDecimal::add);
//                continue;
//            }
//
//            if (points != null) {
//                BigDecimal tenPercent = value.multiply(BigDecimal.valueOf(0.10));
//                if (points.canAfford(tenPercent)) {
//                    pointsUsed = points.getLimit().min(value);
//                    usedPoints = pointsUsed.compareTo(tenPercent) >= 0;
//                }
//            }
//
//            PaymentMethod best = null;
//            BigDecimal bestDiscount = BigDecimal.ZERO;
//
//            for (PaymentMethod method : wallet.getAll()) {
//                if (method.getID().equals("PUNKTY")) continue;
//                if (!order.getPromotions().contains(method.getID())) continue;
//                if (!method.canAfford(value.subtract(pointsUsed))) continue;
//
//                if (method.getDiscountPercent().compareTo(bestDiscount) > 0) {
//                    best = method;
//                    bestDiscount = method.getDiscountPercent();
//                }
//            }
//
//            PaymentMethod payMethod = best;
//            BigDecimal remaining = value.subtract(pointsUsed);
//
//            if (usedPoints) {
//                // 10% discount for using points
//                remaining = remaining.multiply(BigDecimal.valueOf(0.90));
//            } else if (payMethod != null) {
//                remaining = remaining.multiply(BigDecimal.ONE.subtract(payMethod.getDiscountPercent()));
//            }
//
//            if (pointsUsed.compareTo(BigDecimal.ZERO) > 0) {
//                points.subtractFromLimit(pointsUsed);
//                spent.merge("PUNKTY", pointsUsed, BigDecimal::add);
//            }
//
//            if (payMethod != null) {
//                payMethod.subtractFromLimit(value.subtract(pointsUsed));
//                spent.merge(payMethod.getID(), value.subtract(pointsUsed), BigDecimal::add);
//            } else {
//                // No discount method found, pick any
//                for (PaymentMethod fallback : wallet.getAll()) {
//                    if (fallback.getID().equals("PUNKTY")) continue;
//                    if (fallback.canAfford(value.subtract(pointsUsed))) {
//                        fallback.subtractFromLimit(value.subtract(pointsUsed));
//                        spent.merge(fallback.getID(), value.subtract(pointsUsed), BigDecimal::add);
//                        break;
//                    }
//                }
//            }
        }

        spent.forEach((method, amount) -> System.out.printf("%s %.2f%n", method, amount));
    }
}