package trading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TradingApi {
    
    Comparator<Offer> CHEAP_FIRST = Comparator.comparing(Offer::getPrice).thenComparing(Offer::getTimestamp);
    Comparator<Offer> EXPENSIVE_FIRST = Comparator.comparing(Offer::getPrice).reversed().thenComparing(Offer::getTimestamp);
    
    PriorityQueue<Offer> buyers = new PriorityQueue<>(EXPENSIVE_FIRST);
    PriorityQueue<Offer> sellers = new PriorityQueue<>(CHEAP_FIRST);
    ArrayList<Transaction> transactions = new ArrayList<>();
    
    public synchronized void buy(String user, double price) {
        buyers.add(new Offer(user, price));
        match();
    }

    public synchronized void sell(String user, double price) {
        sellers.add(new Offer(user, price));
        match();
    }

    private void match() {
        if (!sellers.isEmpty() && !buyers.isEmpty() && buyers.peek().price >= sellers.peek().price) {

            Offer buyOffer = buyers.poll();
            Offer sellOffer = sellers.poll();

            Transaction transaction = new Transaction();
            transaction.buyer = buyOffer.user;
            transaction.seller = sellOffer.user;
            transaction.price = sellOffer.timestamp < buyOffer.timestamp ? buyOffer.price : sellOffer.price;
            
            transactions.add(transaction);
        }
    }

    public static class Offer {
        String user;
        double price;
        long timestamp = System.currentTimeMillis();

        public Offer(String user, double price) {
            this.user = user;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Transaction {
        String seller;
        String buyer;
        double price;

        @Override
        public String toString() {
            return String.format("%s sold to %s for %.2f", seller, buyer, price);
        }
    }
}
