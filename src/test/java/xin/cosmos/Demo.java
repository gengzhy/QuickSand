package xin.cosmos;

import lombok.AllArgsConstructor;

public class Demo {
    public static void main(String[] args) {




    }
}

@AllArgsConstructor
class Node<E> {
    E data;
    Node<E> root;
    Node<E> pre;
    Node<E> next;
}
