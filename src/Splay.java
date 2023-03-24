import java.io.*;
import java.util.Random;
import java.util.HashSet;
import java.io.FileWriter;

public class Splay {
    Node root;
    Splay(){
        root = null;
    }

    int[] testarray;

    Node RR(Node root) {
        if (root == null || root.left == null) {return root;}
        Node placeholder = root.left;
        root.left = placeholder.right;
        placeholder.right = root;
        return placeholder;
    }
    Node LR (Node root) {
        if (root == null || root.right == null) {return root;}
        Node placeholder = root.right;
        root.right = placeholder.left;
        placeholder.left = root;
        return placeholder;
    }


    Node search(double key){
        root = searchNode(root, key);
        if (root.left != null && root.left.key == key) {
            root = RR(root);
        }else if (root.right != null && root.right.key == key) {
            root = LR(root);
        }
        return root;
    }

    Node searchNode(Node root, double key) {
        if (root == null){return null;}
        if(root.right != null && root.key < key) {
            root.right = searchNode(root.right, key);
            if(root.right.right != null && root.right.right.key == key){
                root = LR(root);
                root = LR(root);
                return root;
            } else if(root.right.left != null && root.right.left.key == key){
                root.right = RR(root.right);
                root = LR(root);
                return root;
            }
        } else if (root.left != null && root.key > key) {
            root.left = searchNode(root.left, key);
            if(root.left.right != null && root.left.right.key == key){
                root.left = LR(root.left);
                root = RR(root);
                return root;
            } else if(root.left.left != null && root.left.left.key == key){
                root = RR(root);
                root = RR(root);
                return root;
            }
        }
        return root;
    }
    void insert(double key, String value){
        root = insertNode(root, key, value);
        if(root.left != null && root.left.key == key){
            root = RR(root);
        }else if(root.right != null && root.right.key == key) {
            root = LR(root);
        }
    }

    Node insertNode(Node root, double key, String value){
        if (root == null){return new Node(key, value);}
        if(root.key < key) {//right
            root.right = insertNode(root.right, key, value);
            if(root.right.right != null && root.right.right.key == key){
                root = LR(root);
                root = LR(root);
                return root;
            } else if(root.right.left != null && root.right.left.key == key){
                root.right = RR(root.right);
                root = LR(root);
                return root;
            }
        } else if (root.key > key) {
            root.left = insertNode(root.left, key, value);
            if(root.left.right != null && root.left.right.key == key){
                root.left = LR(root.left);
                root = RR(root);
                return root;
            } else if(root.left.left != null && root.left.left.key == key){
                root = RR(root);
                root = RR(root);
                return root;
            }
        }
        return root;
    }


    void delete(double key) {
        root = deleteNode(root, key);
    }

    Node deleteNode(Node root, double key) {
        if (root == null) {
            return null;
        }

        if (key < root.key) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.key) {
            root.right = deleteNode(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            Node minRight = minnode(root.right);
            root.key = minRight.key;
            root.value = minRight.value;
            root.right = deleteNode(root.right, minRight.key);
        }

        return root;
    }

    Node minnode(Node root) {
        if (root == null) {
            return null;
        }
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }


    void ight(int value) {
        printTree(root, value);
    }
    void printTree(Node root, int level) {
        if (root != null) {
            printTree(root.right, level + 1);
            System.out.println(" ".repeat(4 * level) + "-> " + root.key);
            printTree(root.left, level + 1);
        }
    }
    static int[] generateUniqueArray(int length) {
        int[] result = new int[length];
        Random random = new Random();
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < length; i++) {
            int randomNumber = random.nextInt(length) + 1;
            while (set.contains(randomNumber)) {
                randomNumber = random.nextInt(length) + 1;
            }
            set.add(randomNumber);
            result[i] = randomNumber;
        }
        return result;
    }

    void testinsert(int Testing_size, int Test_jump) throws IOException {

        Thread animationThread = new Thread(() -> {
            String[] animation = {"|", "/", "-", "\\"};
            int i = 0;
            while (true) {
                System.out.print(animation[i % 4]);
                i++;
                try {
                    Thread.sleep(300);
                    System.out.print("\b");
                } catch (InterruptedException e) {
                    break;
                }
            }
        });


        FileWriter clear = new FileWriter("tests/SplayInsert.txt", false);
        clear.close();
        System.out.println("Initiating splay insert");
        FileWriter fileWriter = new FileWriter("tests/SplayInsert.txt", true);
        System.out.print("Creating an array ");


        animationThread.start();
        this.testarray = generateUniqueArray(Testing_size);
        animationThread.interrupt();
        long completeStartTimeSplay = System.nanoTime();
        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " inserted");
            long startTimeSplay = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                insert(testarray[k], "ight");
            }
            long endTimeSplay = System.nanoTime();
            fileWriter.write(Long.toString(endTimeSplay - startTimeSplay) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeSplay = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeSplay - completeStartTimeSplay)/1_000_000_000) + " seconds." + "\u001B[0m");
    }
    void testsearch(int Testing_size, int Test_jump) throws IOException {

        if (root == null)System.out.println("Searching in an empty tree!");
        System.out.println("Initiating splay search");

        FileWriter clear = new FileWriter("tests/SplaySearch.txt", false);
        clear.close();

        FileWriter fileWriter = new FileWriter("tests/SplaySearch.txt", true);


        long completeStartTimeSplay = System.nanoTime();



        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " searched");
            long startTimeSplay = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                search(testarray[k]);
            }
            long endTimeSplay = System.nanoTime();

            fileWriter.write(Long.toString(endTimeSplay - startTimeSplay) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeSplay = System.nanoTime();
        System.out.println("\u001B[32m" + "Searching " + Testing_size + " completed in: " + (float)((completeEndTimeSplay - completeStartTimeSplay)/1_000_000_000) + " seconds." + "\u001B[0m");
    }

    void testdelete(int Testing_size, int Test_jump) throws IOException {


        if (root == null)System.out.println("Deleting in an empty tree!");
        System.out.println("Initiating splay delete");
        FileWriter clear = new FileWriter("tests/SplayDelete.txt", false);
        clear.close();

        FileWriter fileWriter = new FileWriter("tests/SplayDelete.txt", true);




        long completeStartTimeSplay = System.nanoTime();

        for (int b = 0; b<Testing_size; b+=Test_jump){

            System.out.print(b + " deleted");
            long startTimeSplay = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                delete(testarray[k]);
            }
            long endTimeSplay = System.nanoTime();
            fileWriter.write(Long.toString(endTimeSplay - startTimeSplay) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        this.testarray = null;
        System.gc();
        long completeEndTimeSplay = System.nanoTime();
        System.out.println("\u001B[32m" + "Deleting " + Testing_size + " completed in: " + (float)((completeEndTimeSplay - completeStartTimeSplay)/1_000_000_000) + " seconds." + "\u001B[0m");

    }

    void test(int Testing_size, int Test_jump) throws IOException {
        testinsert(Testing_size, Test_jump);
        testsearch(Testing_size, Test_jump);
        testdelete(Testing_size, Test_jump);
    }


}
