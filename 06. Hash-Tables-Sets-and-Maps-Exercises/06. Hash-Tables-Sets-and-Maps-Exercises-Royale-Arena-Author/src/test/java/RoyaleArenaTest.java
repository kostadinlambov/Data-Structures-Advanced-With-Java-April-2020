import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoyaleArenaTest {

    private RoyaleArena royaleArena;

    @Before
    public void setUp() throws Exception {
        this.royaleArena = new RoyaleArena();

        Battlecard firstCard = new Battlecard(1, CardType.BUILDING, "firstCard", 25.25, 38.26);
        Battlecard secondCard = new Battlecard(2, CardType.BUILDING, "secondCard", 26.25, 52.26);
        Battlecard thirdCard = new Battlecard(3, CardType.MELEE, "thirdCard", 32.25, 68.26);
        Battlecard fourthCard = new Battlecard(4, CardType.BUILDING, "fourthCard", 25.25, 15.26);
        Battlecard fifthCard = new Battlecard(5, CardType.BUILDING, "fifthCard", 12.25, 69.26);
        Battlecard sixthCard = new Battlecard(6, CardType.BUILDING, "sixthCard", 90.25, 90.26);

        royaleArena.add(firstCard);
        royaleArena.add(secondCard);
        royaleArena.add(thirdCard);
        royaleArena.add(fourthCard);
        royaleArena.add(fifthCard);
        royaleArena.add(sixthCard);

    }

    @Test
    public void add() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void count() {
    }

    @Test
    public void changeCardType() {
    }

    @Test
    public void getById() {
    }

    @Test
    public void removeById() {
        Battlecard battlecardById = this.royaleArena.getById(2);

        this.royaleArena.removeById(2);

        boolean contains = this.royaleArena.contains(battlecardById);

        assertFalse(contains);
        assertEquals(5, royaleArena.count());
    }

    @Test
    public void getByCardType() {

        Iterable<Battlecard> byCardType = this.royaleArena.getByCardType(CardType.BUILDING);


        System.out.println();

    }

    @Test
    public void getByTypeAndDamageRangeOrderedByDamageThenById() {

        Iterable<Battlecard> byCardType = this.royaleArena.getByTypeAndDamageRangeOrderedByDamageThenById(CardType.BUILDING, 20, 40 );

        System.out.println();
    }

    @Test
    public void getByCardTypeAndMaximumDamage() {
    }

    @Test
    public void getByNameOrderedBySwagDescending() {
    }

    @Test
    public void getByNameAndSwagRange() {
    }

    @Test
    public void getAllByNameAndSwag() {
    }

    @Test
    public void findFirstLeastSwag() {
    }

    @Test
    public void getAllInSwagRange() {
    }

    @Test
    public void iterator() {
    }
}