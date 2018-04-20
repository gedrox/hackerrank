package hackerrank;

import org.junit.Test;

import java.util.*;

public class Hotels {

    static int[] sort_hotels(String keywords, int[] hotel_ids, String[] reviews) {
        long t0 = System.currentTimeMillis();
        Map<Integer, Integer> mentions = count2(hotel_ids, reviews, keywords);
        System.err.println(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();
        Map<Integer, Integer> mentions_check = count(hotel_ids, reviews, keywords);
        System.err.println(System.currentTimeMillis() - t0);
        
        assert mentions_check.equals(mentions);

        Integer[] ids = mentions.keySet().toArray(new Integer[mentions.size()]);
        Comparator<Integer> cmp = Comparator.comparingInt(hId -> -mentions.get(hId));
        Arrays.sort(ids, cmp.thenComparingInt(hId -> hId));

        ArrayList<Integer> list = new ArrayList(mentions.keySet());
        list.sort(Comparator.<Integer>comparingInt(mentions::get).reversed().thenComparingInt(hId -> hId));

        int[] primitiveArr = Arrays.stream(ids).mapToInt(i -> i).toArray();
        
        return primitiveArr;
    }

    private static Map<Integer, Integer> count(int[] hotel_ids, String[] reviews, String keywords) {
        String[] keywordArr = keywords.toLowerCase().split(" ");
        HashSet<String> keywordSet = new HashSet<>();
        keywordSet.addAll(Arrays.asList(keywordArr));
        
        int[] cnt = new int[hotel_ids.length];

        for (int i = 0; i < reviews.length; i++) {
            StringTokenizer tokenizer = new StringTokenizer(reviews[i].toLowerCase(), " \t\n\r\f.,");
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                if (keywordSet.contains(word)) {
                    cnt[i]++;
                }
            }
        }

        HashMap<Integer, Integer> mentions = new HashMap<>();
        for (int i = 0; i < reviews.length; i++) {
            int hotelId = hotel_ids[i];
            mentions.putIfAbsent(hotelId, 0);
            mentions.put(hotelId, mentions.get(hotelId) + cnt[i]);
        }
        return mentions;
    }
    
    static class Node {
        static final Node END = new Node();
        Node[] next = new Node[43];
        
        public Node() {}

        private Node get(char c) {
            return this.next[c - 48];
        }

        private Node proceed(char c) {
            if (this.next[c - 48] == null) {
                return (this.next[c - 48] = new Node());
            } else {
                return this.next[c - 48];
            }
        }

        private void end() {
            next['@' - 48] = END;
        }
    }
    
    static char normalize(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c;
        }
        if (c >= 'a' && c <= 'z') {
            return (char) ((c - 'a') + 'A');
        }
        if (c >= '0' && c <= '9') {
            return c;
        }
        return '@';
    }

    private static Map<Integer, Integer> count2(int[] hotel_ids, String[] reviews, String keywords) {
        
        final Node root = new Node();
        Node cur = root;
        for (int i = 0; i <= keywords.length(); i++) {
            char c = i == keywords.length() ? '@' : keywords.charAt(i);
            c = normalize(c);
            
            if (c == '@') {
                // end of a keyword
                if (root != cur) {
                    cur.end();
                    cur = root;
                }
                continue;
            }

            cur = cur.proceed(c);
        }
        
        int[] cnt = new int[hotel_ids.length];

        for (int r_i = 0; r_i < reviews.length; r_i++) {
            String r = reviews[r_i];
            cur = root;
            for (int i = 0; i <= r.length(); i++) {
                char c = i == r.length() ? '@' : r.charAt(i);
                c = normalize(c);
                
                if (cur != null) {
                    cur = cur.get(c);
                }
                
                if (c == '@') {
                    if (cur != null && cur == Node.END) {
                        cnt[r_i]++;
                    }
                    
                    // reset
                    cur = root;
                }
                
            }
        }

        HashMap<Integer, Integer> mentions = new HashMap<>();
        for (int i = 0; i < reviews.length; i++) {
            int hotelId = hotel_ids[i];
            mentions.putIfAbsent(hotelId, 0);
            mentions.put(hotelId, mentions.get(hotelId) + cnt[i]);
        }
        
        return mentions;
    }

    public static void main(String[] args) {
        int[] res = sort_hotels("one two three", new int[]{2, 1, 2, 2, 1}, new String[]{"one", "one TWOTHREE", "one", "one THREE", "one, two, three"});
        System.out.println(Arrays.toString(res));
    }
    
    @Test
    public void test() {
        
        Random r = new Random(0);
        int limit = 10000;
        
        int k = 100;
        String keywords = generateRandomString(r, limit, k);
        
        int n = 1000;
        int w = 10000;
        
        int[] hotel_id = new int[n];
        String[] reviews = new String[n];

        for (int i = 0; i < n; i++) {
            hotel_id[i] = r.nextInt(limit);
            reviews[i] = generateRandomString(r, limit, w);
        }

        long t0 = System.currentTimeMillis();
        int[] res = sort_hotels(keywords, hotel_id, reviews);
        System.out.println(Arrays.toString(res));
        System.out.println(System.currentTimeMillis() - t0);
    }

    private String generateRandomString(Random r, int limit, int k) {
        StringBuilder kB = new StringBuilder();
        for (int i = 0; i < k; i++) {
            kB.append("text").append(r.nextInt(limit)).append(" ");
        }
        return kB.toString();
    }
}
