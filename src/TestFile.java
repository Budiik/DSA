import java.io.IOException;

public class TestFile {
    static Splay splayTree = new Splay();
    static AVL avlTree = new AVL();
    static ChainingHash chainingHashTable = new ChainingHash();
    static LinearProbing linearProbingTable = new LinearProbing();

    public static void main(String args[]) throws IOException {


        /*
        splayTree.test(10_000_000, 10_000);



        avlTree.test(10_000_000, 10_000);

        linearProbingTable.test(10_000_000, 10_000);

        chainingHashTable.test(10_000_000, 10_000);
        */

        System.gc();
    }

}
