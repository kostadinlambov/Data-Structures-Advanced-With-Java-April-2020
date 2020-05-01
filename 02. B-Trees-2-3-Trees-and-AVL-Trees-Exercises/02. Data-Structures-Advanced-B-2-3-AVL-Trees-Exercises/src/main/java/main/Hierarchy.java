package main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {
    private HierarchyNode<T> root;
    private Map<T, HierarchyNode<T>> data;

    public Hierarchy(T element) {
        this.root = new HierarchyNode<>(element);
        this.data = new HashMap<>();
        this.data.put(element, root);
    }


    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public void add(T element, T child) {
        checkIfElementExists(element);

        if (this.data.containsKey(child)) {
            throw new IllegalArgumentException();
        }

        HierarchyNode<T> currentNode = this.data.get(element);

        HierarchyNode<T> childNode = new HierarchyNode<>(child);
        currentNode.getChildren().add(childNode);
        childNode.setParent(currentNode);

        this.data.put(child, childNode);
    }

    @Override
    public void remove(T element) {
        checkIfElementExists(element);

        HierarchyNode<T> nodeToRemove = this.data.get(element);

        if (nodeToRemove.getParent() == null) {
            throw new IllegalStateException();
        }

        HierarchyNode<T> parent = nodeToRemove.getParent();
        List<HierarchyNode<T>> childrenOfNodeToRemove = nodeToRemove.getChildren();

        parent.getChildren().addAll(childrenOfNodeToRemove);
        parent.getChildren().remove(nodeToRemove);

        childrenOfNodeToRemove.forEach(child -> child.setParent(parent));


        this.data.remove(nodeToRemove.getValue());
    }

    @Override
    public Iterable<T> getChildren(T element) {
        checkIfElementExists(element);

        List<HierarchyNode<T>> children = this.data.get(element).getChildren();

        return children.stream().map(HierarchyNode::getValue).collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        checkIfElementExists(element);

        HierarchyNode<T> parent = this.data.get(element).getParent();

        if (parent == null) {
            return null;
        }

        return parent.getValue();
    }

    @Override
    public boolean contains(T element) {
        return this.data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        List<T> result = new ArrayList<>();

        this.data.entrySet().forEach(currentNode -> {
            if (other.contains(currentNode.getKey())) {
                result.add(currentNode.getKey());
            }
        });

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(
                    Collections.singletonList(root)
            );


            @Override
            public boolean hasNext() {
                return deque.size() > 0;
            }

            @Override
            public T next() {
                HierarchyNode<T> poll = deque.poll();

                deque.addAll(poll.getChildren());

                return poll.getValue();
            }
        };
    }

    public void checkIfElementExists(T element) {
        if (!this.data.containsKey(element)) {
            throw new IllegalArgumentException();
        }
    }
}
