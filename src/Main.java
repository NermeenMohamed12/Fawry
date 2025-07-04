import ecommerce.core.Cart;
import ecommerce.core.Customer;
import ecommerce.interfaces.Shippable;
import ecommerce.models.*;

import ecommerce.services.ShippingService;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Product cheese = new ShippableProduct("Cheese 400g", 100, 5, 0.2);
        Product biscuits = new ShippableProduct("Biscuits 700g", 150, 2, 0.7);

        Customer customer = new Customer("Nermeen", 1000);
        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        checkout(customer, cart);
    }

    public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) throw new IllegalStateException("Cart is empty.");

        double subtotal = 0, shippingFee = 0;
        List<Shippable> shippables = new ArrayList<>();
        Map<String, Integer> shipCounts = new HashMap<>();

        for (var item : cart.getItems()) {
            if (item.product.isExpired())
                throw new IllegalStateException("Product expired: " + item.product.getName());

            if (item.quantity > item.product.getQuantity())
                throw new IllegalStateException("Insufficient stock for: " + item.product.getName());

            if (item.product instanceof Shippable s) {
                shippables.add(s);
                shipCounts.put(item.product.getName(),
                        shipCounts.getOrDefault(item.product.getName(), 0) + item.quantity);
            }
            subtotal += item.getTotalPrice();
        }

        if (!shippables.isEmpty()) {
            shippingFee = 30;
            ShippingService.ship(shippables, shipCounts);
        }

        double total = subtotal + shippingFee;
        if (customer.getBalance() < total)
            throw new IllegalStateException("Insufficient balance.");

        System.out.println("** Checkout receipt **");
        for (var item : cart.getItems()) {
            System.out.println(item.quantity + "x " + item.product.getName() + " " + item.getTotalPrice());
            item.product.reduceQuantity(item.quantity);
        }

        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f%n", subtotal);
        System.out.printf("Shipping %.0f%n", shippingFee);
        System.out.printf("Amount %.0f%n", total);

    }
}
