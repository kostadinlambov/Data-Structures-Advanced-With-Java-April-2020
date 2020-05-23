package core;

import models.Planet;
import models.Star;

import java.util.*;

public class MilkyWay implements Galaxy {
    Map<Integer, Star> starsById;
    Map<Star, Set<Planet>> galaxyMap;
    Map<Star, Set<Planet>> galaxyMapSorted;
    Map<Star, Set<Planet>> galaxyMapOrderedOnlyByPlanetDistance;
    Map<Integer, Planet> planetsMap;
    Set<Star> starsOrderedByLuminosity;
    List<Star> starsInsertOrder;
    Set<Planet> planetsByMass;

    public MilkyWay() {
        this.starsById = new HashMap<>();
        this.galaxyMap = new LinkedHashMap<>();
        this.galaxyMapSorted = new HashMap<>();
        this.galaxyMapOrderedOnlyByPlanetDistance = new HashMap<>();
        this.planetsMap = new HashMap<>();
        this.starsOrderedByLuminosity = new TreeSet<>(Comparator.comparingInt(Star::getLuminosity).reversed());
        this.starsInsertOrder = new ArrayList<>();
        this.planetsByMass = new TreeSet<>(Comparator.comparingInt(Planet::getDistanceFromStar)
                .thenComparingInt(Planet::getMass));
    }

    @Override
    public void add(Star star) {
        boolean containsStar = this.contains(star);

        if (containsStar) {
            throw new IllegalArgumentException();
        }

        this.starsById.put(star.getId(), star);

        this.galaxyMap.putIfAbsent(star,
                new TreeSet<>(Comparator
                        .comparingInt(Planet::getDistanceFromStar)
                .thenComparingInt(Planet::getMass)));

        this.galaxyMapOrderedOnlyByPlanetDistance.putIfAbsent(star,
                new TreeSet<>(Comparator
                        .comparingInt(Planet::getDistanceFromStar)));

        this.starsOrderedByLuminosity.add(star);
        this.starsInsertOrder.add(star);
    }

    @Override
    public void add(Planet planet, Star star) {
        Set<Planet> planets = this.galaxyMap.get(star);


        if (planets == null) {
            throw new IllegalArgumentException();
        }

        if (planets.contains(planet)) {
            throw new IllegalArgumentException();
        }

        planets.add(planet);
        this.planetsMap.put(planet.getId(), planet);
        this.planetsByMass.add(planet);

        Set<Planet> planetsByPlanetDistance = this.galaxyMapOrderedOnlyByPlanetDistance.get(star);
        planetsByPlanetDistance.add(planet);
    }

    @Override
    public boolean contains(Planet planet) {
        return this.planetsMap.containsKey(planet.getId());
    }

    @Override
    public boolean contains(Star star) {
        return this.starsById.containsKey(star.getId());
    }

    @Override
    public Star getStar(int id) {
        Star star = this.starsById.get(id);

        if (star == null) {
            throw new IllegalArgumentException();
        }

        return star;
    }

    @Override
    public Planet getPlanet(int id) {
        Planet planet = this.planetsMap.get(id);

        if (planet == null) {
            throw new IllegalArgumentException();
        }

        return planet;
    }

    @Override
    public Star removeStar(int id) {
        Star removedStar = this.starsById.remove(id);

        if (removedStar == null) {
            throw new IllegalArgumentException();
        }

        Set<Planet> remove = this.galaxyMap.remove(removedStar);

        for (Planet planet : remove) {
            this.planetsMap.remove(planet.getId());
        }

        this.starsOrderedByLuminosity.remove(removedStar);
        this.starsInsertOrder.remove(removedStar);

        return removedStar;
    }

    @Override
    public Planet removePlanet(int id) {
        Planet planetToRemove = this.planetsMap.remove(id);

        if (planetToRemove == null) {
            throw new IllegalArgumentException();
        }

        this.galaxyMap.values().forEach(starsById ->
                starsById.remove(planetToRemove)
        );

        this.planetsByMass.remove(planetToRemove);

        return planetToRemove;
    }

    @Override
    public int countObjects() {
        return this.starsById.size() + this.planetsMap.size();
    }

    @Override
    public Iterable<Planet> getPlanetsByStar(Star star) {
        Set<Planet> planets = this.galaxyMap.get(star);

        if (planets == null) {
            throw new IllegalArgumentException();
        }

        return planets;
    }

    @Override
    public Iterable<Star> getStars() {
       return this.starsOrderedByLuminosity;
    }

    @Override
    public Iterable<Star> getStarsInOrderOfDiscovery() {
       return starsInsertOrder;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByMass() {
       return this.planetsByMass;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByDistanceFromStar(Star star) {
        Set<Planet> planets = this.galaxyMapOrderedOnlyByPlanetDistance.get(star);

        if(planets == null){
            throw new IllegalArgumentException();
        }

        return planets;
    }

    @Override
    public Map<Star, Set<Planet>> getStarsAndPlanetsByOrderOfStarDiscoveryAndPlanetDistanceFromStarThenByPlanetMass() {
        return this.galaxyMap;
    }
}
