public class Item {
    String nazov;
    double key;
    Item next;
    Item(String nazov, double key){
        this.nazov = nazov;
        this.key = key;
        this.next = null;
    }
    void delete() {
        this.nazov = null;
        this.key = 0;
        this.next = null;
    }
}
