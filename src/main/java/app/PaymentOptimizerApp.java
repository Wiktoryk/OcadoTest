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
                        spent.merge(method, paymentOption.getPointsSpent(), BigDecimal::add);
                        paymentOptimizer.subtractLimit(method, paymentOption.getPointsSpent());
                    }
                    else {
                        spent.merge(method, paymentOption.getTraditionalSpending(), BigDecimal::add);
                        paymentOptimizer.subtractLimit(method, paymentOption.getTraditionalSpending());
                    }
                }
            }
        }

        spent.forEach((method, amount) -> System.out.printf("%s %.2f%n", method, amount));
    }
}