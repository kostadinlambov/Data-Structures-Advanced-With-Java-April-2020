import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PersonCollectionSlowImpl implements PersonCollection {
    private List<Person> personList;

    public PersonCollectionSlowImpl() {
        this.personList = new ArrayList<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        for (Person person : personList) {
            if (person.getEmail().equals(email)) {
                return false;
            }
        }

        Person person = new Person(email, name, age, town);

        return personList.add(person);
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public boolean delete(String email) {
        Person person = this.find(email);

        return this.personList.remove(person);
    }

    @Override
    public Person find(String email) {

        for (Person person : personList) {
            if (person.getEmail().equals(email)) {
                return person;
            }
        }

        return null;
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        List<Person> result = new ArrayList<>();

        for (Person person : personList) {
            boolean endsWith = person.getEmail().split("@")[1].equals(emailDomain);
            if (endsWith) {
                result.add(person);
            }
        }

        result.sort(Person::compareTo);

        return result;
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        List<Person> result = new ArrayList<>();

        for (Person person : personList) {

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

        for (Person person : personList) {

            if (person.getAge() >= startAge && person.getAge() <= endAge) {
                result.add(person);
            }
        }

        result.sort(Comparator.comparingInt(Person::getAge).thenComparing(Person::compareTo));

        return result;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        List<Person> result = new ArrayList<>();

        for (Person person : personList) {

            if (person.getAge() >= startAge && person.getAge() <= endAge && person.getTown().equals(town)) {
                result.add(person);
            }
        }

        result.sort(Comparator.comparingInt(Person::getAge).thenComparing(Person::compareTo));

        return result;
    }
}
