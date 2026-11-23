import java.util.*;

class ProductOfNumbers {
    // prefix products: prod[i] = product of first (i-1) numbers (excluding dummy)
    private List<Integer> prod;

    public ProductOfNumbers() {
        prod = new ArrayList<>();
        prod.add(1);          // dummy to simplify calculations
    }

    public void add(int num) {
        if (num == 0) {
            // reset: only keep dummy 1
            prod.clear();
            prod.add(1);
        } else {
            // multiply last product by new number
            int last = prod.get(prod.size() - 1);
            prod.add(last * num);
        }
    }

    public int getProduct(int k) {
        int size = prod.size() - 1;          // number of actual elements
        if (k > size) return 0;             // not enough elements after a zero
        int right = prod.get(size);          // product of all elements till now
        int left  = prod.get(size - k);      // product upto element before last k
        return right / left;                 // integer division gives the product
    }
}