package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentOptionTest {
    @Test
    void testPaymentOptionSingle() throws Exception {
        PaymentOption option = new PaymentOption("PUNKTY", BigDecimal.valueOf(90.00), BigDecimal.valueOf(100.00));

        assertNotNull(option.getMethods());
        assertEquals(1, option.getMethods().size());
        assertEquals("PUNKTY", option.getMethods().get(0));
        assertEquals(BigDecimal.valueOf(100.00), option.getOriginalValue());
        assertEquals(BigDecimal.valueOf(90.00), option.getFinalCost());
        assertEquals(BigDecimal.valueOf(10.00), option.getDiscountAmount());
        assertEquals(BigDecimal.ZERO, option.getTraditionalSpending());
    }
    @Test
    void testPaymentOptionMultiple() throws Exception {
        PaymentOption option = new PaymentOption(List.of("PUNKTY", "mZysk"), BigDecimal.valueOf(90.00), BigDecimal.valueOf(100.00));

        assertNotNull(option.getMethods());
        assertEquals(2, option.getMethods().size());
        assertEquals("PUNKTY", option.getMethods().get(0));
        assertEquals("mZysk", option.getMethods().get(1));
        assertEquals(BigDecimal.valueOf(100.00), option.getOriginalValue());
        assertEquals(BigDecimal.valueOf(90.00), option.getFinalCost());
        assertEquals(BigDecimal.valueOf(10.00), option.getDiscountAmount());
        assertNotEquals(BigDecimal.ZERO, option.getTraditionalSpending());
    }
}
