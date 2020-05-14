import java.util.*;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {
    private Map<Integer, Computer> computerByNumber;

    public MicrosystemImpl() {
        this.computerByNumber = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        int number = computer.getNumber();

        boolean contains = this.contains(number);

        if (contains) {
            throw new IllegalArgumentException();
        }

        // Fill the computerByBrand Collection
        this.computerByNumber.put(number, computer);
    }

    @Override
    public boolean contains(int number) {
        return this.computerByNumber.containsKey(number);
    }

    @Override
    public int count() {
        return this.computerByNumber.size();
    }

    @Override
    public Computer getComputer(int number) {
        Computer computer = this.computerByNumber.get(number);

        if (computer == null) {
            throw new IllegalArgumentException();
        }

        return computer;
    }

    @Override
    public void remove(int number) {
        // remove computer from computerByNumber
        Computer removedComputer = this.computerByNumber.remove(number);

        if (removedComputer == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void removeWithBrand(Brand brand) {
         this.computerByNumber.values().stream()
                .filter(computer -> computer.getBrand() == brand)
                .findAny().orElseThrow(IllegalArgumentException::new);

        this.computerByNumber = this.computerByNumber.entrySet().stream()
                .filter(computerEntry -> computerEntry.getValue().getBrand() != brand)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = this.getComputer(number);

        if (computer.getRAM() < ram) {
            computer.setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computerByNumber.values().stream()
                .filter(computer -> computer.getBrand() == brand)
                .sorted(Comparator.comparingDouble(Computer::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computerByNumber.values()
                .stream()
                .filter(computer -> computer.getScreenSize() == screenSize)
                .sorted(Comparator.comparingInt(Computer::getNumber).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computerByNumber.values()
                .stream()
                .filter(computer -> computer.getColor().equals(color))
                .sorted(Comparator.comparingDouble(Computer::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computerByNumber.values()
                .stream()
                .filter(computer -> computer.getPrice() >= minPrice && computer.getPrice() <= maxPrice)
                .sorted(Comparator.comparingDouble(Computer::getPrice).reversed())
                .collect(Collectors.toList());
    }
}
