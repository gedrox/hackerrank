package trading;

import org.junit.Test;

import static org.junit.Assert.*;

public class TradingApiTest {

    private TradingApi api = new TradingApi();

    @Test
    public void oneBuy_noTrannsaction() {
        api.buy("a", 100);
        assertEquals(0, api.transactions.size());
        assertEquals(1, api.buyers.size());
        assertEquals(0, api.sellers.size());
    }

    @Test
    public void oneSell_noTrannsaction() {
        api.sell("a", 100);
        assertEquals(0, api.transactions.size());
        assertEquals(1, api.sellers.size());
        assertEquals(0, api.buyers.size());
    }
    
    @Test
    public void onBuySell_match() {
        api.buy("a", 100);
        api.sell("b", 100);
        assertEquals(1, api.transactions.size());
        assertEquals(0, api.sellers.size());
        assertEquals(0, api.buyers.size());
    }

    @Test
    public void onSellBuy_match() {
        api.sell("a", 100);
        api.buy("b", 100);
        assertEquals(1, api.transactions.size());
        assertEquals(0, api.sellers.size());
        assertEquals(0, api.buyers.size());
    }

    @Test
    public void onSellBuy_noMatch() {
        api.sell("a", 101);
        api.buy("b", 99);
        assertEquals(0, api.transactions.size());
        assertEquals(1, api.sellers.size());
        assertEquals(1, api.buyers.size());
    }

    @Test
    public void testCase() {
        api.buy("A", 10);
        api.buy("B", 11);
        api.sell("C", 15);
        api.sell("D", 9);
        api.buy("E", 10);
        api.sell("F", 10);
        api.buy("G", 100);
        assertEquals(3, api.transactions.size());
        assertEquals(0, api.sellers.size());
        assertEquals(1, api.buyers.size());

        assertEquals(100, api.transactions.get(2).price, 0);
        
        System.out.println(api.transactions);
    }

}