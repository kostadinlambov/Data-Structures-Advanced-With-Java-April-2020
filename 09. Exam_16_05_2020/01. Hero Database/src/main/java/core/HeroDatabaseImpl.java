package core;

import models.Hero;
import models.HeroType;

import java.util.*;
import java.util.stream.Collectors;

public class HeroDatabaseImpl implements HeroDatabase {
    Map<String, Hero> heroByName;
    Set<Hero> heroByNameSorted;
    Map<HeroType, Set<Hero>> heroByTypeMap;
    Map<Integer, Set<Hero>> heroByLevelMap;
    Set<Hero> heroByPointsSorted;

    public HeroDatabaseImpl() {
        this.heroByName = new HashMap<>();
        this.heroByTypeMap = new HashMap<>();
        this.heroByLevelMap = new HashMap<>();
        this.heroByNameSorted = new TreeSet<>(Comparator.comparingInt(Hero::getLevel).reversed().thenComparing(Hero::getName));
        this.heroByPointsSorted = new TreeSet<>(Comparator.comparingInt(Hero::getPoints).reversed().thenComparingInt(Hero::getLevel));
    }

    @Override
    public void addHero(Hero hero) {
        boolean contains = this.contains(hero);

        if (contains) {
            throw new IllegalArgumentException();
        }

        String heroName = hero.getName();
        this.heroByName.put(heroName, hero);

        // Put heroes to the heroByType map
        HeroType heroType = hero.getHeroType();
        this.heroByTypeMap.putIfAbsent(heroType, new HashSet<>());
        this.heroByTypeMap.get(heroType).add(hero);

        // Put heroes to the heroByLevel map
        int level = hero.getLevel();
        this.heroByLevelMap.putIfAbsent(level, new TreeSet<>(Comparator.comparing(Hero::getName)));
        this.heroByLevelMap.get(level).add(hero);

        // Put heroes to the heroByNameSorted map
        this.heroByNameSorted.add(hero);
        this.heroByPointsSorted.add(hero);
    }

    @Override
    public boolean contains(Hero hero) {
        return this.heroByName.containsKey(hero.getName());
    }

    @Override
    public int size() {
        return this.heroByName.size();
    }

    @Override
    public Hero getHero(String name) {
        Hero hero = this.heroByName.get(name);

        if (hero == null) {
            throw new IllegalArgumentException();
        }

        return hero;
    }

    @Override
    public Hero remove(String name) {
        Hero heroToRemove = this.heroByName.remove(name);

        if (heroToRemove == null) {
            throw new IllegalArgumentException();
        }

        this.heroByTypeMap.get(heroToRemove.getHeroType()).remove(heroToRemove);
        this.heroByLevelMap.get(heroToRemove.getLevel()).remove(heroToRemove);
        this.heroByNameSorted.remove(heroToRemove);
        this.heroByPointsSorted.remove(heroToRemove);

        return heroToRemove;
    }

    @Override
    public Iterable<Hero> removeAllByType(HeroType type) {
        Set<Hero> heroes = this.heroByTypeMap.get(type);
        Set<Hero> result = new HashSet<>(heroes);

        this.heroByTypeMap.remove(type);

        for (Hero hero : result) {
            this.heroByName.remove(hero.getName());
            this.heroByLevelMap.get(hero.getLevel()).removeIf(hero1 -> hero1.getName().equals(hero.getName()));
            this.heroByNameSorted.remove(hero);
            this.heroByPointsSorted.remove(hero);
        }

        return result;
    }

    @Override
    public void levelUp(String name) {
        Hero hero = this.heroByName.get(name);

        if (hero == null) {
            throw new IllegalArgumentException();
        }

        int level = hero.getLevel();

        hero.setLevel(level + 1);
    }

    @Override
    public void rename(String oldName, String newName) {
        boolean containsOldName = this.heroByName.containsKey(oldName);

        if (!containsOldName) {
            throw new IllegalArgumentException();
        }

        boolean containsNewName = this.heroByName.containsKey(newName);

        if (containsNewName) {
            throw new IllegalArgumentException();
        }

        Hero heroWithOldName = this.heroByName.remove(oldName);
        heroWithOldName.setName(newName);
        this.heroByName.put(newName, heroWithOldName);

    }

    @Override
    public Iterable<Hero> getAllByType(HeroType type) {
        return this.heroByTypeMap.get(type);
    }

    @Override
    public Iterable<Hero> getAllByLevel(int level) {
        Set<Hero> heroes = this.heroByLevelMap.get(level);

        if(heroes == null){
            return new HashSet<>();
        }

        return heroes;
    }

    @Override
    public Iterable<Hero> getInPointsRange(int lowerBound, int upperBound) {
        return  this.heroByPointsSorted
                .stream()
                .filter(hero -> hero.getPoints() >= lowerBound && hero.getPoints() < upperBound)
                .sorted(Comparator.comparingInt(Hero::getPoints).reversed().thenComparing(Hero::getLevel))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Hero> getAllOrderedByLevelDescendingThenByName() {
        return heroByNameSorted;
    }
}
