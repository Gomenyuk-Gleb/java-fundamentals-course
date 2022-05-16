package com.bobocode;

import java.util.Objects;
import java.util.Stack;

public class DemoApp {

        public static void main(String[] args) {
            var head = createLinkedList(4, 3, 9, 1);
            printReversedRecursively(head);
            printReversedUsingStack(head);
        }

        /**
         * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
         *
         * @param elements an array of elements that should be added to the list
         * @param <T>      elements type
         * @return head of the list
         */
        public static <T> Node<T> createLinkedList(T... elements) {
            Objects.requireNonNull(elements);
            if(elements.length <= 0) {
                throw new RuntimeException("array is empty");
            }
            Node<T> head = new Node<>(elements[0]);
            Node<T> current = head;
            for (int i = 1; i < elements.length; i++) {
                current.next = new Node<>(elements[i]);
                current = current.next;
            }

            return  head;
        }

        /**
         * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
         * just print its elements.
         * <p>
         * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
         * 1 -> 9 -> 3 -> 4
         *
         * @param head the first node of the list
         * @param <T>  elements type
         */
        public static <T> void printReversedRecursively(Node<T> head) {
            printReversedRecursivelyNext(head.next);
            System.out.print(head.elem);
            System.out.println();

        }

        public static <T> void printReversedRecursivelyNext(Node<T> head) {
            if (head.next != null) {
                printReversedRecursivelyNext(head.next);
                System.out.print(head.elem + "->");
                return;
            }
            System.out.print(head.elem + "->");
        }

        /**
         * Prints a list in a reserved order using a {@link java.util.Stack} instance. Please note that it should not change
         * the list, just print its elements.
         * <p>
         * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
         * 1 -> 9 -> 3 -> 4
         *
         * @param head the first node of the list
         * @param <T>  elements type
         */
        public static <T> void printReversedUsingStack(Node<T> head) {
            Stack<T> tStack = new Stack<>();
            while (head != null) {
                tStack.add(head.elem);
                head = head.next;
            }

            int size = tStack.size();

            for (int i = 0; i < size - 1; i++) {
                System.out.print(tStack.pop() + "->");
            }
            System.out.print(tStack.pop());

        }
}
