package lesson3;

import org.junit.Test;
import org.junit.jupiter.api.Tag;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BinTreeTests {

    @Test
    public void removalTests() {
        BinaryTree<Integer> testTree = new BinaryTree<>();
        assertFalse(testTree.remove(2));
        testTree.add(2);
        assertTrue(testTree.remove(2));
        for (int i = 10; i < 20; i++) testTree.add(i);
        for (int i = 0; i < 10; i++) testTree.add(i);
        testTree.remove(7);
        for (int i = 0; i < 20; i++) if (i != 7) assertTrue(testTree.contains(i));
        assertEquals(19, testTree.size());
    }

    @Test
    public void checkRangeTests() {
        BinaryTree<String> tree = new BinaryTree<>();
        assertTrue(tree.checkRange("abc"));
        tree = new BinaryTree<>("aa", null);
        assertTrue(tree.checkRange("aaa"));
        assertFalse(tree.checkRange("a"));
        tree = new BinaryTree<>(null, "aa");
        assertTrue(tree.checkRange("a"));
        assertFalse(tree.checkRange("aaa"));
        tree = new BinaryTree<>("a", "z");
        assertTrue(tree.checkRange("b"));
        assertFalse(tree.checkRange("A"));
    }

    @Test
    public void sizeTests() {
        BinaryTree<Integer> tree = new BinaryTree<>();
        for (int i = 0; i < 10; i++) tree.add(i);
        assertEquals(10, tree.size());
        tree.fromElement = 5;
        assertEquals(5, tree.size());
        tree.toElement = 7;
        assertEquals(2, tree.size());
    }

    private void singleBTIteratorTest() {
        Set comparingSet = new TreeSet();
        BinaryTree<Integer> tree = new BinaryTree<>();
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {
            int value = rand.nextInt();
            comparingSet.add(value);
            tree.add(value);
            System.out.print(value + " ");
        }
        System.out.println();

        int counter = 0;
        Iterator<Integer> treeIter = tree.iterator();
        while (treeIter.hasNext()) {
            assertTrue(comparingSet.contains(treeIter.next()));
            counter++;
        }
        assertEquals(counter, comparingSet.size());
    }

    @Test
    public void BTIteratorTests() { for (int i = 0; i < 100; i++) singleBTIteratorTest(); }

    private void singleSubSetTest() {
        Set<Integer> comparingSet = new TreeSet<>();
        BinaryTree<Integer> tree = new BinaryTree<>();
        Random rand = new Random();
        System.out.print("\nSet is: ");
        for (int i = 0; i < 100; i++) {
            int value = rand.nextInt();
            comparingSet.add(value);
            tree.add(value);
            System.out.print(value + " ");
        }

        int fromElement = rand.nextInt();
        int toElement = rand.nextInt();
        while (fromElement >= toElement) {
            toElement = rand.nextInt();
            fromElement = rand.nextInt();
        }
        System.out.print("\nFromElement: " + fromElement + ", toElement: " + toElement);
        SortedSet<Integer> shortTree = tree.subSet(fromElement, toElement);
        ArrayList<Integer> remList = new ArrayList<>();
        for (Object i : comparingSet) {
            int iInt = (Integer) i;
            if (!(iInt >= fromElement && iInt < toElement)) remList.add(iInt);
        }
        for (int i : remList) comparingSet.remove(i);
        System.out.print("\nSubSet size: " + shortTree.size() + "\nRequired size: " + comparingSet.size() +"\n");
        assertEquals(shortTree.size(), comparingSet.size());
    }

    private void singleSubSetIteratorTest() {
        int bound = 100;
        BinaryTree<Integer> tree = new BinaryTree<>();
        Set<Integer> comparingSet = new TreeSet<>();
        Random r = new Random();
        int toElement = r.nextInt(bound);
        int fromElement = r.nextInt(bound);
        while (fromElement >= toElement) {
            fromElement = r.nextInt(bound);
            toElement = r.nextInt(bound);
        }
        System.out.print("\n" + "fromEl is: " + fromElement + ", toEl is: " + toElement);
        for (int i = 0; i < 15; i++) {
            int addVal = r.nextInt(bound);
            if (addVal < toElement && addVal >= fromElement) comparingSet.add(addVal);
            tree.add(addVal);
        }
        System.out.println();
        System.out.print("Set is: ");
        for (int i : tree) System.out.print(i + " ");
        System.out.print("\nExpected set is: ");
        for (int i : comparingSet) System.out.print(i + " ");
        System.out.print("\nSubset is: ");
        SortedSet<Integer> subSet = tree.subSet(fromElement, toElement);
        int counter = 0;
        for (int i : subSet) {
            System.out.print(i + " ");
            counter++;
            assertTrue(comparingSet.contains(i), i + " is in subSet, but should not be there");
        }
        System.out.println();
        assertEquals(counter, comparingSet.size());
    }

    @Test
    @Tag("Impossible")
    public void subSetTests() {
        for (int i = 0; i < 100; i++) singleSubSetTest();
        for (int i = 0; i < 100; i++) singleSubSetIteratorTest();
    }

}
