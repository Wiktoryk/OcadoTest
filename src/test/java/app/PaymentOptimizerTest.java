package app;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentOptimizerTest {
    @Test
    void testPaymentOptimizer() throws Exception {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod("PUNKTY", BigDecimal.valueOf(15.00), BigDecimal.valueOf(100.00)));
        paymentMethods.add(new PaymentMethod("bosBankrut", BigDecimal.valueOf(5.00), BigDecimal.valueOf(200.00)));
        PaymentOptimizer optimizer = new PaymentOptimizer(paymentMethods);
        Optional<PaymentOption> option = optimizer.chooseBestOption(new Order("ORDER1", BigDecimal.valueOf(100.00), null));
        assertTrue(option.isPresent());
        assertEquals(1, option.get().getMethods().size());
        assertEquals("PUNKTY", option.get().getMethods().get(0));
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), option.get().getOriginalValue());
        assertEquals(BigDecimal.valueOf(85.00).setScale(2), option.get().getFinalCost());
        assertEquals(BigDecimal.ZERO, option.get().getTraditionalSpending());
    }
}
