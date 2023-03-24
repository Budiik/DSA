import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChainingHash {
    int size = 0;
    int arraysize = 64;
    int[] randomarray;
    Item[] table;
    ChainingHash(){
        table = new Item[arraysize];
    }
    void resize(boolean direction){
        double amp = direction ? 2 : 0.5;
        Item[] temptable = new Item[(int)(this.arraysize * amp)];


        for (int i = 0; i < this.arraysize; i++) {
            if (table[i] != null) {
                Item temp = table[i];
                while(temp.next != null) {
                    hashInsert(temp.nazov,temp.key,temptable);
                    temp = temp.next;

                }
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
        hashInsert(value, key, this.table);
        if (this.size > arraysize*0.75){resize(true);}

    }
    void hashInsert(String value, double key, Item[] table) {


        Item newitem = new Item(value, key);
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) % table.length;


        if (table[(int)hashvalue] != null) {
            Item temp = table[(int)hashvalue];
            while(temp.next != null) {
                temp = temp.next;
            }
            temp.next = newitem;
        }else {
            table[(int)hashvalue] = newitem;
        }

    }
    void delete(String value){
        this.size --;

        hashDelete(value, this.table);
        if (this.size < arraysize*0.25){resize(false);}

    }
    void hashDelete(String value, Item[] table) {
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) % arraysize;
        if (!table[(int)hashvalue].nazov.equals(value)) {

            Item temp = table[(int)hashvalue];
            while(!temp.next.nazov.equals(value)) {
                temp = temp.next;
            }
            temp.next = temp.next.next;
        }else{
            table[(int)hashvalue] = table[(int)hashvalue].next;;
        }

    }
    double search(String value){
        byte[] data = value.getBytes();
        long hashvalue = fnv1aHash(data) % this.arraysize;
        Item temp = this.table[(int)hashvalue];
        while (!temp.nazov.equals(value)) {
            temp = temp.next;
        }
        return temp.key;

    }
    void print(){
        for(int i = 0; i < this.arraysize; i++){
            Item temp = this.table[i];
            System.out.print(i);
            while (temp != null){
                System.out.print("->");
                System.out.print(temp.nazov);
                temp = temp.next;
            }
            System.out.println();

        }
        System.out.println(this.arraysize);
    }
    void testinsert(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/ChainingInsert.txt", false);
        clear.close();
        System.out.println("Initiating chaining insert");
        FileWriter fileWriter = new FileWriter("tests/ChainingInsert.txt", true);

        long completeStartTimeChaining = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " inserted");
            long startTimeChaining = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                line = reader.readLine();
                insert(line, k);
            }

            long endTimeChaining = System.nanoTime();
            fileWriter.write(Long.toString(endTimeChaining - startTimeChaining) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeChaining = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeChaining - completeStartTimeChaining)/1_000_000_000) + " seconds." + "\u001B[0m");
    }
    void testsearch(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/ChainingSearch.txt", false);
        clear.close();
        System.out.println("Initiating chaining searching");
        FileWriter fileWriter = new FileWriter("tests/ChainingSearch.txt", true);

        long completeStartTimeChaining = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " searched");
            long startTimeChaining = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                line = reader.readLine();
                search(line);
            }

            long endTimeChaining = System.nanoTime();
            fileWriter.write(Long.toString(endTimeChaining - startTimeChaining) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeChaining = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeChaining - completeStartTimeChaining)/1_000_000_000) + " seconds." + "\u001B[0m");

    }

    void testdelete(int Testing_size, int Test_jump) throws IOException {


        FileWriter clear = new FileWriter("tests/ChainingDelete.txt", false);
        clear.close();
        System.out.println("Initiating chaining delete");
        FileWriter fileWriter = new FileWriter("tests/ChainingDelete.txt", true);

        long completeStartTimeChaining = System.nanoTime();

        BufferedReader reader = new BufferedReader(new FileReader("text10,000,000.txt"));
        String line;

        for (int b = 0; b<Testing_size; b+=Test_jump){
            System.out.print(b + " deleted");
            long startTimeChaining = System.nanoTime();

            for (int k = b; k<b + Test_jump; k++) {
                line = reader.readLine();
                delete(line);
            }

            long endTimeChaining = System.nanoTime();
            fileWriter.write(Long.toString(endTimeChaining - startTimeChaining) + "\n");
            System.out.print("\r\033[K");
        }
        fileWriter.close();
        long completeEndTimeChaining = System.nanoTime();
        System.out.println("\u001B[32m" + "Inserting " + Testing_size + " completed in: " + (float)((completeEndTimeChaining - completeStartTimeChaining)/1_000_000_000) + " seconds." + "\u001B[0m");
    }

    void test(int Testing_size, int Test_jump) throws IOException {
        testinsert(Testing_size, Test_jump);
        testsearch(Testing_size, Test_jump);
        testdelete(Testing_size, Test_jump);
    }
}
