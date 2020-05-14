import java.util.*;
import java.util.stream.Collectors;

public class RoyaleArena implements IArena {
    private Map<Integer, Battlecard> cardsById;
    private Map<CardType, Set<Battlecard>> cardsByType;
    private Map<String, Set<Battlecard>> cardsByName;

    public RoyaleArena() {
        this.cardsById = new LinkedHashMap<>();
        this.cardsByType = new LinkedHashMap<>();
        this.cardsByName = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        int cardId = card.getId();

        this.cardsById.putIfAbsent(cardId, card);

        // Add card to cardsByType collection
        CardType type = card.getType();
        this.cardsByType.putIfAbsent(type, new TreeSet<>(Battlecard::compareTo));
        this.cardsByType.get(type).add(card);

        // Add card to cardByName collection
        String name = card.getName();
        this.cardsByName.putIfAbsent(name, new TreeSet<>(Comparator
                .comparingDouble(Battlecard::getSwag)
                .reversed()
                .thenComparingInt(Battlecard::getId)));
        this.cardsByName.get(name).add(card);

    }

    @Override
    public boolean contains(Battlecard card) {
        return this.cardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.cardsById.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        Battlecard cardById = this.cardsById.get(id);

        if (cardById == null) {
            throw new IllegalArgumentException("BattleCard with this id does not exist.");
        }

        cardById.setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        Battlecard battlecard = this.cardsById.get(id);
        if (battlecard == null ) {
            throw new UnsupportedOperationException("BattleCard with this id does not exist.");
        }

        return battlecard;
    }

    @Override
    public void removeById(int id) {
        Battlecard removedCard = this.cardsById.remove(id);

        if (removedCard == null) {
            throw new UnsupportedOperationException("Couldn't remove this Card. Card with this id doesn't exist.");
        }

        // Remove the card from the cardsByType collection
        this.cardsByType.get(removedCard.getType()).remove(removedCard);

        // Remove the card from the cardsByName collection
        this.cardsByName.get(removedCard.getName()).remove(removedCard);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        Set<Battlecard> battleCards = this.cardsByType.get(type);

        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return battleCards;
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> battleCards = this.cardsByType.get(type);

        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        // TODO
        return battleCards
                .stream()
                .filter(card -> card.getDamage() > lo && card.getDamage() < hi)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battleCards = this.cardsByType.get(type);

        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        List<Battlecard> battleCardListByDamage = battleCards
                .stream()
                .filter(card -> card.getDamage() <= damage)
                .collect(Collectors.toList());

        if (battleCardListByDamage.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return battleCardListByDamage;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        Set<Battlecard> battleCards = this.cardsByName.get(name);

        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return battleCards;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        Set<Battlecard> battleCards = this.cardsByName.get(name);

        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        List<Battlecard> battleCardListByNameAndSwag = battleCards
                .stream()
                .filter(card -> card.getSwag() >= lo && card.getSwag() < hi)
                .collect(Collectors.toList());

        if (battleCardListByNameAndSwag.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return battleCardListByNameAndSwag;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        return this.cardsByName.values()
                .stream()
                .map(battlecards -> battlecards.stream().findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if(n > this.cardsById.size()){
            throw new UnsupportedOperationException();
        }

        return this.cardsById.values()
                .stream()
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).thenComparingInt(Battlecard::getId))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
       return this.cardsById.values()
                .stream()
                .filter(card -> card.getSwag() >= lo &&  card.getSwag() <= hi)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
      return new BattleCardIterator();

//      return  this.cardsById.values().iterator();
    }

    private class BattleCardIterator implements Iterator<Battlecard>{
        Deque<Battlecard> deque;

        public BattleCardIterator(){
            List<Battlecard> cardsValuesSet = new ArrayList<>(cardsById.values());
            this.deque = new ArrayDeque(cardsValuesSet);
        }

        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }

        @Override
        public Battlecard next() {
            return deque.poll();
        }
    }
}
