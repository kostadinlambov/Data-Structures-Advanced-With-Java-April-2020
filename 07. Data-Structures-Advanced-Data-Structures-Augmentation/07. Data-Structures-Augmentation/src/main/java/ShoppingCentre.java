import java.util.*;

public class ShoppingCentre {
    private Map<String, Set<Product>> productMapByName;
    private Map<String, Set<Product>> productMapByProducer;
    private Map<Double, Set<Product>> productMapByDouble;

    public ShoppingCentre() {
        this.productMapByName = new LinkedHashMap<>();
        this.productMapByProducer = new LinkedHashMap<>();
        this.productMapByDouble = new LinkedHashMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);

        this.productMapByName.putIfAbsent(name, new TreeSet<>(Product::compareTo));
        this.productMapByName.get(name).add(product);

        this.productMapByProducer.putIfAbsent(producer, new TreeSet<>());
        this.productMapByProducer.get(producer).add(product);

        this.productMapByDouble.putIfAbsent(price, new TreeSet<>());
        this.productMapByDouble.get(price).add(product);

        return String.format("Product added%n");
    }

    public String delete(String name, String producer) {
        Set<Product> products = this.productMapByProducer.get(producer);

        if (products == null || products.isEmpty()) {
            return String.format("No products found%n");
        }

        for (Product product : products) {
            if (product.getName().equals(name)) {
                this.productMapByName.remove(product.getName());
                this.productMapByProducer.remove(product.getProducer());
                this.productMapByDouble.remove(product.getPrice());
            }
        }

        return String.format("%d products deleted%n", products.size());
    }

    public String delete(String producer) {
        Set<Product> products = this.productMapByProducer.get(producer);

        if (products == null || products.isEmpty()) {
            return String.format("No products found%n");
        }

        for (Product product : products) {
            this.productMapByName.remove(product.getName());
            this.productMapByProducer.remove(product.getProducer());
            this.productMapByDouble.remove(product.getPrice());
        }

        return String.format("%d products deleted%n", products.size());
    }

    public String findProductsByName(String name) {
        Set<Product> products = this.productMapByName.get(name);

        if (products == null || products.isEmpty()) {
            return String.format("No products found%n");
        }

        StringBuilder sb = new StringBuilder();

        products
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .forEach(product -> sb
                        .append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())
                                .replace(',', '.'))
                        .append(System.lineSeparator())
                );

        return sb.toString();
    }

    public String findProductsByProducer(String producer) {
        Set<Product> products = this.productMapByProducer.get(producer);

        if (products == null || products.isEmpty()) {
            return String.format("No products found%n");
        }

        StringBuilder sb = new StringBuilder();

        products
                .stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .forEach(product -> sb
                        .append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())
                                .replace(',', '.'))
                        .append(System.lineSeparator())
                );

        return sb.toString();
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        List<Product> result = new ArrayList<>();

        this.productMapByDouble.entrySet().stream()
                .filter(kvp -> kvp.getKey() >= priceFrom && kvp.getKey() <= priceTo)
                .forEach(entry -> {
                    result.addAll(entry.getValue());
                });

        if (result.isEmpty()) {
            return String.format("No products found%n");
        }

        StringBuilder sb = new StringBuilder();

        result.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .forEach(product -> sb
                        .append(String.format("{%s;%s;%.2f}", product.getName(), product.getProducer(), product.getPrice())
                                .replace(',', '.'))
                        .append(System.lineSeparator())
                );

        return sb.toString();
    }
}
