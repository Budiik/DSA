import java.util.HashSet;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

public class AVL {

    Node root;
    AVL() {root = null;}
    int[] testarray;
    void insert(double key, String value) {
        root = insertKey(root, key, value);
    }

    Node insertKey(Node root, double key, String value) {

        if (root == null) {
            root = new Node(key, value);
            return root;
        }

        if (key < root.key)
            root.left = insertKey(root.left, key, value);
        else if (key > root.key)
            root.right = insertKey(root.right, key, value);


        update_height(root);
        root = update_balance(root);
        return root;
    }
    Node RR (Node root) {
        Node placeholder = root;
        root = root.left;
        placeholder.left = root.right;
        root.right = placeholder;
        update_height(placeholder);
        update_height(root);
        return root;
    }
    Node LR (Node root) {

        Node placeholder = root;
        root = root.right;
        placeholder.right = root.left;
        root.left = placeholder;
        update_height(placeholder);
        update_height(root);
        return root;
    }
    Node search(double key){
        return searchNode(root, key);
    }
    Node searchNode(Node root, double key){
        if (root == null){return root;}

        if (key == root.key){
            return root;
        } else if (key > root.key) {
            return searchNode(root.right, key);
        }else if (key < root.key) {
            return searchNode(root.left, key);
        }
        return root;
    }
    Node update_balance(Node root){

        int balance_factor = getheight(root.left) - getheight(root.right);

        if (balance_factor > 1) {

            if (getheight(root.left.left) < getheight(root.left.right)){

                root.left = LR(root.left);
                root = RR(root);
            }else{
                root = RR(root);
            }

        }else if (balance_factor < -1) {
            if (getheight(root.right.right) < getheight(root.right.left)){
                root.right = RR(root.right);
                root = LR(root);
            }else{
                root = LR(root);
            }
        }
        return root;
    }



    void update_height(Node root){
        root.height = Math.max(getheight(root.right),
                               getheight(root.left)) + 1;

    }
    int getheight(Node root){
        if (root == null) {
            return 0;
        }else{
            return root.height;
        }
    }

    void delete(int key){
        root = deleteNode(root, key);
    }
    Node deleteNode(Node root, double key){

        if (root == null){return root;}
        if (key > root.key) {
            root.right = deleteNode(root.right, key);
        }else if (key < root.key) {
            root.left = deleteNode(root.left, key);
        }else if (key == root.key){
            if (root.right == null) {
                root = root.left;
            }else if (root.left == null){
                root = root.right;
            }else{
                Node temp = minnode(root.right);
                root.key = temp.key;
                root.right = deleteNode(root.right, temp.key);

            }

        }
        if (root == null){return null;}
        update_height(root);
        root = update_balance(root);
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
    void printTree(Node node, int level) {
        if (node != null) {printTree(
            node.right, level + 1);
            System.out.println(" ".repeat(4 * level) + "-> " + node.key);
            printTree(node.left, level + 1);
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
        FileWriter clear = new FileWriter("tests/AVLInsert.txt", false);
        clear.close();
        System.out.println("Initiating AVL insert");
        FileWriter fileWriter = new FileWriter("tests/AVLInsert.txt", true);
        System.out.print("Creating an array ");

        animationThread.start();
        this.testarray = generateUniqueArray(Testing_size);
        animationThread.interrupt();

        long completeStartTimeAVL = System.nanoTime();
        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " inserted");
            long startTimeAVL = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                insert(testarray[k], "ight");
            }
            long endTimeAVL = System.nanoTime();
            fileWriter.write(Long.toString(endTimeAVL - startTimeAVL) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeAVL = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " +
                (float)((completeEndTimeAVL - completeStartTimeAVL)/1_000_000_000) +
                " seconds." + "\u001B[0m");
    }
    void testsearch(int Testing_size, int Test_jump) throws IOException {

        if (root == null)System.out.println("Searching in an empty tree!");
        System.out.println("Initiating AVL search");

        FileWriter clear = new FileWriter("tests/AVLSearch.txt", false);
        clear.close();

        FileWriter fileWriter = new FileWriter("tests/AVLSearch.txt", true);



        long completeStartTimeAVL = System.nanoTime();



        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " searched");
            long startTimeAVL = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                search(k);
            }
            long endTimeAVL = System.nanoTime();

            fileWriter.write(Long.toString(endTimeAVL - startTimeAVL) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeAVL = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeAVL - completeStartTimeAVL)/1_000_000_000) + " seconds." + "\u001B[0m");

    }

    void testdelete(int Testing_size, int Test_jump) throws IOException {

        if (root == null)System.out.println("Deleting in an empty tree!");
        System.out.println("Initiating AVL delete");
        FileWriter clear = new FileWriter("tests/AVLDelete.txt", false);
        clear.close();

        FileWriter fileWriter = new FileWriter("tests/AVLDelete.txt", true);
        System.out.print("Creating an array ");




        long completeStartTimeAVL = System.nanoTime();

        for (int b = 0; b<Testing_size; b+=Test_jump){

            System.out.print(b + " deleted");
            long startTimeAVL = System.nanoTime();
            for (int k = b; k<b + Test_jump; k++) {
                delete(testarray[k]);
            }
            long endTimeAVL = System.nanoTime();
            fileWriter.write(Long.toString(endTimeAVL - startTimeAVL) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        this.testarray = null;
        System.gc();
        long completeEndTimeAVL = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeAVL - completeStartTimeAVL)/1_000_000_000) + " seconds." + "\u001B[0m");    }
    void test(int Testing_size, int Test_jump) throws IOException {
        testinsert(Testing_size, Test_jump);
        testsearch(Testing_size, Test_jump);
        testdelete(Testing_size, Test_jump);
    }
}