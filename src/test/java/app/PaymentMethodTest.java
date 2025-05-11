package app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodTest {
    @Test
    void testPaymentMethodDeserialization() throws Exception {
        String json = "{\n" +
                "  \"id\": \"PUNKTY\",\n" +
                "  \"discount\": \"15\",\n" +
                "  \"limit\": \"100.00\"\n" +
                "},";

        ObjectMapper objectMapper = new ObjectMapper();
        PaymentMethod paymentMethod = objectMapper.readValue(json, PaymentMethod.class);

        assertNotNull(paymentMethod);
        assertEquals("PUNKTY", paymentMethod.getID());
        assertEquals(new BigDecimal("100.00"), paymentMethod.getLimit());
        assertEquals(BigDecimal.valueOf(0.15), paymentMethod.getDiscountPercent());
        assertTrue(paymentMethod.canAfford(BigDecimal.valueOf(10)));
    }

    @Test
    void testPaymentMethodDeserializationMultiple() throws Exception {
        String json = "[\n{\n" +
                "  \"id\": \"PUNKTY\",\n" +
                "  \"discount\": \"15\",\n" +
                "  \"limit\": \"100.00\"\n" +
                "}," +
                "{\n" +
                "  \"id\": \"mZysk\",\n" +
                "  \"discount\": \"10\",\n" +
                "  \"limit\": \"180.00\"\n" +
                "}\n]";
        ObjectMapper objectMapper = new ObjectMapper();
        List<PaymentMethod> methods = objectMapper.readValue(json, new TypeReference<List<PaymentMethod>>() {});

        assertNotNull(methods);
        assertEquals(2, methods.size());
        assertEquals("PUNKTY", methods.get(0).getID());
        assertEquals("mZysk", methods.get(1).getID());
        assertEquals(new BigDecimal("100.00"), methods.get(0).getLimit());
        assertEquals(BigDecimal.valueOf(0.15), methods.get(0).getDiscountPercent());
        assertTrue(methods.get(0).canAfford(BigDecimal.valueOf(10)));
        assertEquals(new BigDecimal("180.00"), methods.get(1).getLimit());
        assertEquals(BigDecimal.valueOf(0.1), methods.get(1).getDiscountPercent());
        assertTrue(methods.get(1).canAfford(BigDecimal.valueOf(10)));
    }
}
