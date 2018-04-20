package milliday;

// TODO: imports so this actually compiles

import org.junit.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Mainnn {

    public static char[] reverseWords(char[] input) {

        // remember last word start, go till the next WS, reverse, repeat
        int wordStart = 0;
        // we are not finishing the last word.
        for (int i = 0; i < input.length; i++) {
            // finish the word
            if (isWhitespace(input[i])) {
                reverseWord(input, wordStart, i);
                wordStart = i + 1;
            }
        }

        // reverse last word
        reverseWord(input, wordStart, input.length);

        return input;
    }

    /**
     * TODO: can we have different WS chars or just space?
     * Might check with reg expression \w, \W.
     * For sake of simplicity, let's assume we can have only spaces.
     */
    static boolean isWhitespace(char c) {
        return c == ' ';
    }

    static void reverseWord(char[] input, int start, int end) {
        // is there anything to reverse? might even remove this check as revCount would be 0 anyway
        if (end - start > 1) {
            // meet in the middle
            int revCount = (end - start) / 2;
            for (int j = 0; j < revCount; j++) {
                // swap
                char tmp = input[start + j];
                input[start + j] = input[end - 1 - j];
                input[end - 1 - j] = tmp;
            }
        }
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][]{
                {"I love Taxify", "I evol yfixaT"},
                {"", ""},
                {"a", "a"},
                {"ab", "ba"},
                {"abc", "cba"},
                {"        abc       ", "        cba       "},
        };
    }

    @Test(dataProvider = "data")
    public void genericTest(String in, String out) {
        char[] input = in.toCharArray();
        reverseWords(input);
        Assert.assertEquals(out, new String(input));
    }

}