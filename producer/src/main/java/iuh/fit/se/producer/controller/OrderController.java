package iuh.fit.se.producer.controller;

import iuh.fit.se.producer.model.Order;
import iuh.fit.se.producer.service.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderProducer orderProducer;

    @PostMapping("/send")
    public ResponseEntity<?> sendOrder(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String orderId = request.get("orderId");

        if (message == null || orderId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "message or orderId is required"));
        }

        orderProducer.sendOrder(message, orderId);

        Order dataSent = new Order(message, orderId, LocalDateTime.now().toString());
        return ResponseEntity.ok(Map.of(
                "status", "sent",
                "dataSent", dataSent
        ));
    }
}
