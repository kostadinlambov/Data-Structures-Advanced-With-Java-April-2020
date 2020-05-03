import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    private Node<T> root;

    public static class Node<T extends Comparable<T>> {
        private T element;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T element) {
            this.element = element;
            this.level = 1;
        }
    }

    public AATree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;

    }

    public void insert(T element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }

        if (this.root == null) {
            this.root = new Node<>(element);
            return;
        }

        this.root = insert(this.root, element);

    }

    private Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }

        // Find the right position and insert the element
        int cmp = node.element.compareTo(element);

        if (cmp > 0) {
            node.left = insert(node.left, element);
        } else if (cmp < 0) {
            node.right = insert(node.right, element);
        }

        node = skew(node);
        node = split(node);

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right != null && node.right.right != null && (node.level == node.right.right.level)) {
            Node<T> tmp = node.right;
            node.right = tmp.left;
            tmp.left = node;

            tmp.level = node.level + 1;

            return tmp;
        }

        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left.level == node.level) {
            Node<T> temp = node.left;
            node.left = temp.right;
            temp.right = node;

            return temp;
        }

        return node;
    }

    public int countNodes() {
        if (this.root == null) {
            return 0;
        }

        return countNodes(this.root);
    }

    private int countNodes(Node<T> node) {
        int count = 1;

        if (node.left != null) {
            count += countNodes(node.left);
        }

        if (node.right != null) {
            count += countNodes(node.right);
        }

        return count;
    }

    public boolean search(T element) {
        Node<T> search = search(this.root, element);

        return search != null;
    }

    private Node<T> search(Node<T> node, T element) {
        if (node == null) {
            return null;
        }

        int cmp = node.element.compareTo(element);

        if (cmp > 0) {
           return search(node.left, element);
        } else if (cmp < 0) {
            return  search(node.right, element);
        }else {
            return node;
        }
    }

    public void inOrder(Consumer<T> consumer) {
        inOrder(this.root, consumer);
    }

    private void inOrder(Node<T> node, Consumer<T> consumer) {
        if(node == null){
            return;
        }

        inOrder(node.left, consumer);
        consumer.accept(node.element);
        inOrder(node.right, consumer);
    }


    public void preOrder(Consumer<T> consumer) {
        preOrder(this.root, consumer);
    }

    private void preOrder(Node<T> node, Consumer<T> consumer) {
        if(node == null){
            return;
        }

        consumer.accept(node.element);
        preOrder(node.left, consumer);
        preOrder(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        postOrder(this.root, consumer);
    }

    private void postOrder(Node<T> node, Consumer<T> consumer) {
        if(node == null){
            return;
        }

        postOrder(node.left, consumer);
        postOrder(node.right, consumer);
        consumer.accept(node.element);
    }
}