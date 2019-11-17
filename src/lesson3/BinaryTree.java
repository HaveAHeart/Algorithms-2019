package lesson3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    public BinaryTree() {}

    public BinaryTree(T fromEl, T toEl) {
        fromElement = fromEl;
        toElement = toEl;
    }

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;



    T fromElement;
    T toElement;

    @Override
    public boolean add(T t) {
        if (!checkRange(t)) throw new IllegalArgumentException();
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    private Node<T> findParent(T value) {
        if (root.right == null) return findParent(root, root.left, value);
        else if (root.left == null) return findParent(root, root.right, value);
        Node<T> rightSearch = findParent(root, root.right, value);

        if (rightSearch != null) return rightSearch;
        else return findParent(root, root.left, value);
    }

    private Node<T> findParent(Node<T> prevNode, Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison < 0) {
            if (start.left == null) return null;
            else return findParent(start, start.left, value);
        }
        else if (comparison > 0) {
            if (start.right == null) return null;
            else return findParent(start, start.right, value);
        }
        else return prevNode;
    }

    private Node<T> findLesserClosest(Node<T> start) {
        Node<T> currNode = start.left;
        if (currNode != null) while (currNode.right != null) currNode = currNode.right;
        return currNode;
    }

    boolean checkRange(T value) {
        if (fromElement != null && toElement != null)
            return (value.compareTo(fromElement) >= 0) && (value.compareTo(toElement) < 0);
        else if (fromElement != null) return value.compareTo(fromElement) >= 0;
        else if (toElement != null) return value.compareTo(toElement) < 0;
        else return true;
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */

    //all the additional tests can be found in BinTreeTests class(Algorithms-2019\test\lesson3)

    /* complexity(all the O's are for the worst cases): O(height) for findOrNull(starting from root),
    * O(height) for findLesserClosest(starting from root),
    * O(height) for findParent(parent is not found),
    * Total: O(height)
    *
    * Memory(worst cases): O(height) for recursion needs in findOrNull(),
    * O(height) for recursion needs in findLesserClosest(),
    * O(height) for recursion needs in findParent(),
    * Total: O(height)
    *
    * The worst-worst case is if all the children are ONLY left children or ONLY right children (height = N)
    *
    * and, yes, this code below looks disgusting because I made if-else construction for root/non-root check,
    * almost doubling the amount of code.
    * It would be great to get some advice about how it could be done more beautifully :c
    */
    @Override
    public boolean remove(Object o) {
        T remVal = (T) o;
        if (!checkRange(remVal)) return false;
        if (root == null) return false;

        Node<T> remNode = findOrNull(root, remVal);
        if (remNode == null) return false;
        if (remNode.value.compareTo(root.value) == 0) {
            //root case
            //System.out.println("currValue is root" + remVal);
            if (remNode.left == null && remNode.right == null) root = null;
            else if (remNode.left == null) root = root.right;
            else if (remNode.right == null) root = root.left;
            else {
                Node<T> replaceNode = findLesserClosest(remNode);
                Node<T> lesserParentNode = findParent(replaceNode.value);
                if (lesserParentNode == null) {
                    root = replaceNode;
                    return true;
                }
                if (replaceNode.value.compareTo(lesserParentNode.value) < 0) lesserParentNode.left = replaceNode.left;
                else lesserParentNode.right = replaceNode.left;
                replaceNode.left = root.left;
                replaceNode.right = root.right;
                root = replaceNode;
            }
        }
        else {
            Node<T> parentNode = findParent(remVal);
            Node<T> replaceNode;
            if (remNode.left == null && remNode.right == null) {
                //System.out.println("both remNode children are null " + remVal);
                if (remVal.compareTo(parentNode.value) < 0) parentNode.left = null;
                else parentNode.right = null;
            }
            else if (remNode.left == null) {
                //System.out.println("remNode children are null/notnull" + remVal);
                if (remVal.compareTo(parentNode.value) < 0) parentNode.left = remNode.right;
                else parentNode.right = remNode.right;
            }
            else if (remNode.right == null) {
                //System.out.println("remNode children are notnull/null" + remVal);
                if (remVal.compareTo(parentNode.value) < 0) parentNode.left = remNode.left;
                else parentNode.right = remNode.left;
            }
            else {
                replaceNode = findLesserClosest(remNode);

                System.out.println(remNode.left + ";" + remNode.right);
                Node<T> lesserParentNode = findParent(replaceNode.value);
                if (replaceNode.value.compareTo(lesserParentNode.value) < 0) lesserParentNode.left = replaceNode.left;
                else lesserParentNode.right = replaceNode.left;
                replaceNode.left = remNode.left;
                replaceNode.right = remNode.right;
                if (remVal.compareTo(parentNode.value) < 0) parentNode.left = replaceNode;
                else parentNode.right = replaceNode;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        if (!checkRange(t)) return false;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {

        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    private Node<T> findOrNull(Node<T> start, T value) {

        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return null;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return null;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {
        ArrayDeque<Node<T>> buffer;
        Node<T> currNode;
        T maxVal;
        boolean isInitial;
        boolean lastUsed;

        private BinaryTreeIterator() {
            buffer = new ArrayDeque<>();
            Node<T> iterNode = root;
            while (iterNode != null) {
                buffer.add(iterNode);
                iterNode = iterNode.left;
            }
            maxVal = last();
            currNode = buffer.peekLast();
            isInitial = true;
            lastUsed = false;
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */

        /*
         * Complexity: O(N) in the worst case, only for receiving proper initial value if there are both toEl and fromEl
         * Memory: O(1) due to same root usage, but there is still an buffer ArrayDeque, which size can reach O(Height)
         */
        @Override
        public boolean hasNext() {
            //if there are no restrictions from head/tail/sub set
            if (fromElement == null && toElement == null) return !buffer.isEmpty();
            //head set case - just getting sure that the next element is lower than toElement
            else if (fromElement == null) {
                if (!buffer.isEmpty()) {
                    if (currNode.right != null) {
                        Node<T> iterNode = currNode.right;
                        while (iterNode.left != null) iterNode = iterNode.left;
                        return iterNode.value.compareTo(toElement) < 0;
                    }
                    else return buffer.peekLast().value.compareTo(toElement) < 0;
                }
                else return false;
            }
            //tail set case - all the lower values skip logic is in Next(), but we still need to be sure that there will
            //be some values to iterate through
            else if (toElement == null) {
                return (fromElement.compareTo(maxVal) <= 0 && !buffer.isEmpty());
            }
            //if both the toElement and fromElement exists
            else {
                if (!buffer.isEmpty()) {
                    //if this is the first hasNext, then we need to be sure that there is at least 1 element to iterate
                    if (isInitial) {
                        BinaryTree<T> tempTree = new BinaryTree<>();
                        tempTree.root = root;
                        for (T val : tempTree)
                            if (val.compareTo(fromElement) >= 0 && val.compareTo(toElement) < 0) return true;
                        return false;
                    }
                    //toCond is a boolean for the toElement requirements, fromCond - for the fromElement
                    boolean toCond;
                    if (currNode.right != null) {
                        Node<T> iterNode = currNode.right;
                        while (iterNode.left != null) iterNode = iterNode.left;
                        toCond = iterNode.value.compareTo(toElement) < 0;
                    }
                    else toCond = buffer.peekLast().value.compareTo(toElement) < 0;
                    boolean fromCond = fromElement.compareTo(maxVal) <= 0;
                    //both of conditions should be true to iterate further
                    return  toCond && fromCond;
                }
                else return false;
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */

        /*
        * Complexity: O(height) for iterating to the very left element of the tree
        * Memory: O(1), but there is still an buffer ArrayDeque, which size can reach O(Height)
         */
        @Override
        public T next() {
            //skipping all the elements lower than fromElement and starting to iterate further from the proper element
            //this should be done only once, that's why isInitial exists
            if (isInitial && fromElement != null) {
                isInitial = false;
                T res;
                do {
                    currNode = buffer.pollLast();
                    res = currNode.value;
                    Node<T> iterNode = currNode.right;
                    if (iterNode != null) {
                        while (iterNode != null) {
                            buffer.add(iterNode);
                            iterNode = iterNode.left;

                        }
                    }
                    //System.out.println("currNode " + currNode.value);
                } while (res.compareTo(fromElement) < 0);
                return res;
            }
            //iterating normally - toElement logic is in hasNext() method
            else {
                currNode = buffer.pollLast();
                T res = currNode.value;
                Node<T> iterNode = currNode.right;
                if (iterNode != null) {
                    while (iterNode != null) {
                        buffer.add(iterNode);
                        iterNode = iterNode.left;
                    }
                }

                return res;
            }
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            //TODO()
            throw new NotImplementedException();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    private int getSize(Node<T> node) {
        if (node == null) return 0;
        if (!checkRange(node.value)) return getSize(node.left) + 0 + getSize(node.right); //currNode is not included
        else return getSize(node.left) + 1 + getSize(node.right); //currNode is included
    }

    @Override
    public int size() {
        /*complexity becomes O(N) instead of O(1), but now it changes for head/tail sets
        * memory: O(N) in the worst case(if all the children will be only left children or only right children)
        */
        return getSize(root);
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */

    /*
    * Complexity: O(1)
    * Memory: O(1), the only NEW data will be: fromElement, toElement and link to the root
    *
    * Complexity/Memory are the same for subSet, headSet and TailSet
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        BinaryTree result = new BinaryTree<>(fromElement, toElement);
        result.root = this.root;
        return result;
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        BinaryTree result = new BinaryTree<>(null, toElement);
        result.root = this.root;
        return result;

    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        BinaryTree result = new BinaryTree<>(fromElement, null);
        result.root = this.root;
        return result;
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
