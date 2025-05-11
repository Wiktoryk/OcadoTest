package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    @Test
    void testOrderDeserialization() throws Exception {
        String json = "{\n" +
                "  \"id\": \"ORDER1\",\n" +
                "  \"value\": 150.00,\n" +
                "  \"promotions\": [\"mZysk\", \"BosBankrut\"]\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(json, Order.class);

        assertNotNull(order);
        assertEquals("ORDER1", order.getID());
        assertEquals(new BigDecimal("150.00"), order.getValue());
        assertNotNull(order.getPromotions());
        assertEquals(2, order.getPromotions().size());
        assertTrue(order.getPromotions().contains("mZysk"));
        assertTrue(order.getPromotions().contains("BosBankrut"));
    }
}
