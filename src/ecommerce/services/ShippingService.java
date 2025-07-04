package ecommerce.services;

import ecommerce.interfaces.Shippable;

import java.util.List;
import java.util.Map;

public class  ShippingService {
    public static void ship(List<Shippable> items, Map<String, Integer> counts) {
        double totalWeight = 0;
        System.out.println("** Shipment notice **");
        for (Shippable s : items) {
            int count = counts.get(s.getName());
            System.out.println(count + "x " + s.getName());
            totalWeight += s.getWeight() * count;
        }
        System.out.printf("Total package weight %.1fkg\n", totalWeight);
    }
}
