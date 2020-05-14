import java.util.*;
import java.util.stream.Collectors;

public class PersonCollectionImpl implements PersonCollection {
    private Map<String, Person> personMap;
    private Map<Integer, Set<Person>> personByAge;
    private Map<String, Set<Person>> personByDomain;

    public PersonCollectionImpl() {
        this.personMap = new HashMap<>();
        this.personByAge = new HashMap<>();
        this.personByDomain = new HashMap<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (this.find(email) != null) {
            return false;
        }

        Person person = new Person(email, name, age, town);

        this.personMap.put(email, person);

        this.personByAge.putIfAbsent(age, new TreeSet<>(Comparator.comparingInt(Person::getAge).thenComparing(Person::compareTo)));
        this.personByAge.get(age).add(person);


        String emailDomain = person.getEmail().split("@")[1];

        this.personByDomain.putIfAbsent(emailDomain, new TreeSet<>());
        this.personByDomain.get(emailDomain).add(person);

        return true;
    }

    @Override
    public int getCount() {
        return this.personMap.size();
    }

    @Override
    public boolean delete(String email) {
        Person remove = this.personMap.remove(email);

        if (remove == null) {
            return false;
        }

        this.personByAge.get(remove.getAge()).remove(remove);

        String emailDomain = remove.getEmail().split("@")[1];
        this.personByDomain.get(emailDomain).remove(remove);

        return true;
    }

    @Override
    public Person find(String email) {
        return this.personMap.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        Set<Person> people = personByDomain.get(emailDomain);

        return people == null ? new ArrayList<>() : people;
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        List<Person> result = new ArrayList<>();

        for (Person person : personMap.values()) {

            if (person.getName().equals(name) && person.getTown().equals(town)) {
                result.add(person);
            }
        }

        result.sort(Person::compareTo);

        return result;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        List<Person> result = new ArrayList<>();

        this.personByAge.entrySet().stream()
                .filter(entry -> entry.getKey() >= startAge && entry.getKey() <= endAge)
                .forEach(entry -> {
                    result.addAll(entry.getValue());
                });


        return result;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        List<Person> result = new ArrayList<>();

        this.personByAge.entrySet().stream()
                .filter(entry -> entry.getKey() >= startAge && entry.getKey() <= endAge)
                .forEach(entry -> {
                    entry.getValue().forEach(person -> {
                        if (person.getTown().equals(town)) {
                            result.add(person);
                        }
                    });
                });

        return result;
    }
}
