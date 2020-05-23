package core;

import models.Hero;
import models.HeroType;

import java.util.*;
import java.util.stream.Collectors;

public class HeroDatabaseImpl implements HeroDatabase {
    private Map<String, Hero> heroesByNames;
    private Map<Integer, Set<Hero>> heroesByLevels;
    private Map<HeroType, Set<Hero>> heroByTypes;
    private Set<Hero> heroes;

    public HeroDatabaseImpl() {
        this.heroesByNames = new HashMap<>();
        this.heroByTypes = new HashMap<>();
        this.heroesByLevels = new HashMap<>();
        this.heroes = new TreeSet<>(Comparator.comparingInt(Hero::getLevel).reversed().thenComparing(Hero::getName));
        for (HeroType value : HeroType.values()) {
            this.heroByTypes.put(value, new TreeSet<>(Comparator.comparing(Hero::getName)));
        }
    }

    @Override
    public void addHero(Hero hero) {
        if (this.heroesByNames.containsKey(hero.getName())) {
            throw new IllegalArgumentException();
        }
        this.heroes.add(hero);
        this.heroByTypes.get(hero.getHeroType()).add(hero);
        this.heroesByNames.put(hero.getName(), hero);
        this.heroesByLevels.putIfAbsent(hero.getLevel(), new TreeSet<>(Comparator.comparing(Hero::getName)));
        this.heroesByLevels.get(hero.getLevel()).add(hero);
    }

    @Override
    public boolean contains(Hero hero) {
        return this.heroesByNames.containsKey(hero.getName());
    }

    @Override
    public int size() {
        return this.heroesByNames.size();
    }

    @Override
    public Hero getHero(String name) {
        Hero hero = this.heroesByNames.get(name);
        if (hero == null) {
            throw new IllegalArgumentException();
        }

        return hero;
    }

    @Override
    public Hero remove(String name) {
        Hero hero = this.heroesByNames.remove(name);
        if (hero == null) {
            throw new IllegalArgumentException();
        }

        this.heroByTypes.get(hero.getHeroType()).removeIf(h -> h.getName().equals(name));
        this.heroes.remove(hero);
        this.heroesByLevels.get(hero.getLevel()).removeIf(h -> h.getName().equals(name));
        return hero;
    }

    @Override
    public Iterable<Hero> removeAllByType(HeroType type) {
        List<Hero> heroes = this.heroesByNames.values().stream()
                .filter(h -> h.getHeroType() == type)
                .collect(Collectors.toList());

        for (Hero hero : heroes) {
            this.heroesByNames.remove(hero.getName());
            this.heroesByLevels.get(hero.getLevel()).removeIf(h -> h.getName().equals(hero.getName()));
        }

        this.heroByTypes.get(type).clear();

        this.heroes.removeIf(h -> h.getHeroType() == type);

        return heroes;
    }

    @Override
    public void levelUp(String name) {
        Hero hero = getHero(name);
        hero.setLevel(hero.getLevel() + 1);
    }

    @Override
    public void rename(String oldName, String newName) {
        if (this.heroesByNames.containsKey(newName)) {
            throw new IllegalArgumentException();
        }
        Hero hero = getHero(oldName);
        this.remove(hero.getName());
        hero.setName(newName);
        this.heroesByNames.put(hero.getName(), hero);
    }

    @Override
    public Iterable<Hero> getAllByType(HeroType type) {
        return this.heroByTypes.get(type);
    }

    @Override
    public Iterable<Hero> getAllByLevel(int level) {
        Set<Hero> heroes = this.heroesByLevels.get(level);
        if (heroes != null) {
            return heroes;
        }
        return new HashSet<>();
    }

    @Override
    public Iterable<Hero> getInPointsRange(int lowerBound, int upperBound) {
        return this.heroesByNames.values().stream()
                .filter(h -> h.getPoints() >= lowerBound && h.getPoints() < upperBound)
                .sorted(Comparator.comparingInt(Hero::getPoints).reversed().thenComparing(Hero::getLevel))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Hero> getAllOrderedByLevelDescendingThenByName() {
        return this.heroes;
    }
}
