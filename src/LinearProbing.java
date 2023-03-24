import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LinearProbing {
    int size = 0;
    int arraysize = 64;
    Item[] table;
    LinearProbing(){
        table = new Item[arraysize];
    }
    void resize(boolean direction){
        double amp = direction ? 2 : 0.5;
        Item[] temptable = new Item[(int)(this.arraysize * amp)];

        for (int i = 0; i < this.arraysize; i++) {
            if (table[i] != null && table[i].nazov != null) {
                Item temp = table[i];
                hashInsert(temp.nazov,temp.key,temptable);
            }
        }
        this.arraysize = (int)(amp*this.arraysize);
        this.table = temptable;
    }
    long fnv1aHash(byte[] data) {
        final int prime = 0x01000193;
        int hash = 0x811c9dc5;

        for (byte b : data) {
            hash ^= b;
            hash *= prime;
        }

        return hash & 0xffffffffL;
    }
    void insert(String value, double key){
        this.size ++;
        if (this.size > arraysize*0.75){resize(true);}
        hashInsert(value, key, this.table);


    }
    void hashInsert(String value, double key, Item[] table) {


        Item newitem = new Item(value, key);
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) % table.length;


        if (table[(int)hashvalue] != null && table[(int)hashvalue].nazov != null) {
            Item temp = table[(int) hashvalue];


            while (temp != null && temp.nazov != null) {

                hashvalue++;
                if (hashvalue == table.length){hashvalue = 0;}
                temp = table[(int) hashvalue];

            }
        }
        table[(int)hashvalue] = newitem;

    }
    void delete(String value){
        this.size --;
        if (this.size < arraysize*0.25){

            resize(false);
        }
        hashDelete(value, this.table);


    }
    void hashDelete(String value, Item[] table) {
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) % arraysize;

        if (table[(int)hashvalue] != null) {
            Item temp = table[(int) hashvalue];
            while (true) {
                if(temp != null && temp.nazov != null){
                    if(temp.nazov.equals(value)) {
                        break;
                    }
                }

                hashvalue++;
                if (hashvalue == table.length){hashvalue = 0;}
                temp = table[(int)hashvalue];
            }
            table[(int)hashvalue].delete();
        }


    }
    double search(String value){
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) & (this.arraysize - 1);
        Item temp = table[(int) hashvalue];
        while (temp != null) {
            if (temp.nazov != null && temp.nazov.equals(value)) {
                return temp.key;
            }
            temp = temp.next;
        }
        return 0;
    }
    void print(){
        printa(table);
    }
    void printa(Item[] table){
        for(int i = 0; i < table.length; i++){
            Item temp = table[i];
            System.out.print(i);
            while (temp != null){
                System.out.print("->");
                System.out.print(temp.nazov);
                temp = temp.next;
            }
            System.out.println();

        }
        System.out.println(table.length);
    }


    void testinsert(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/LinearProbingInsert.txt", false);
        clear.close();
        System.out.println("Initiating linear probing insert");
        FileWriter fileWriter = new FileWriter("tests/LinearProbingInsert.txt", true);

        long completeStartTimeLinearProbing = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " inserted");
            long startTimeLinearProbing = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                    line = reader.readLine();
                    insert(line, k);
            }

            long endTimeLinearProbing = System.nanoTime();
            fileWriter.write(Long.toString(endTimeLinearProbing - startTimeLinearProbing) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeLinearProbing = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeLinearProbing - completeStartTimeLinearProbing)/1_000_000_000) + " seconds." + "\u001B[0m");

    }
    void testsearch(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/LinearProbingSearch.txt", false);
        clear.close();
        System.out.println("Initiating linear probing searching");
        FileWriter fileWriter = new FileWriter("tests/LinearProbingSearch.txt", true);

        long completeStartTimeLinearProbing = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " searched");
            long startTimeLinearProbing = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                line = reader.readLine();
                search(line);
            }

            long endTimeLinearProbing = System.nanoTime();
            fileWriter.write(Long.toString(endTimeLinearProbing - startTimeLinearProbing) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeLinearProbing = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeLinearProbing - completeStartTimeLinearProbing)/1_000_000_000) + " seconds." + "\u001B[0m");
    }
    void testdelete(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/LinearProbingDelete.txt", false);
        clear.close();
        System.out.println("Initiating linear probing delete");
        FileWriter fileWriter = new FileWriter("tests/LinearProbingDelete.txt", true);

        long completeStartTimeLinearProbing = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " deleted");
            long startTimeLinearProbing = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                line = reader.readLine();
                delete(line);
            }

            long endTimeLinearProbing = System.nanoTime();
            fileWriter.write(Long.toString(endTimeLinearProbing - startTimeLinearProbing) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeLinearProbing = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeLinearProbing - completeStartTimeLinearProbing)/1_000_000_000) + " seconds." + "\u001B[0m");
    }

    void test(int Testing_size, int Test_jump) throws IOException {
        testinsert(Testing_size, Test_jump);
        testsearch(Testing_size, Test_jump);
        testdelete(Testing_size, Test_jump);
    }

}
