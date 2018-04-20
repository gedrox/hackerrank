package hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.math.*;

public class ModulosPowers {

    static int maxK = 1000;

    static long[][] C = new long[maxK + 2][maxK + 2];
    static long[] B = new long[maxK + 1];
    static BigInteger[] BIGS = new BigInteger[maxK + 2];
    static {
        for (int i = 0; i < BIGS.length; i++) {
            BIGS[i] = BigInteger.valueOf(i);
        }
    }
    
    static int MOD = 1000000009;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);

    @Test
    public void test() {
        combinations();
//        if (true) return;
//        bernoulliNumbers();
        
//        ArrayList<Long> notZero = new ArrayList<>();
//        for (int i = 0; i < B.length; i++) {
//            if (i % 2 == 0) notZero.add(B[i]);
//        }
//        
//        System.out.println(Arrays.toString(B));
//        System.out.println(notZero);
        
//        long n = (long) 1e18;
        for (int n = 1; n <= 100; n++) {
        StringBuilder sb = new StringBuilder();
            for (int k = 1; k <= 200; k++) {
                int fast = highwayConstruction(n, k);
                sb.append(fast).append('\n');
//                System.out.println(fast);
                int slow = highwayConstructionSlow(n, k);
    //            System.out.println(slow);
                Assert.assertEquals(n + "," + k,slow, fast);
            }
//        System.out.print(sb.toString());
        }
        
    }
    
    static void combinations() {
        for (int i = 1; i <= maxK + 1; i++) {
            C[0][i] = 1;
            C[i][i] = 1;

            for (int j = 1; j <= i / 2; j++) {
                long newC = (C[j - 1][i] * divMod(i - j + 1, j)) % MOD;
                C[j][i] = C[i - j][i] = newC;
            }
        }
    }
    
    static void bernoulliNumbers() {
        for (int m = 0; m <= maxK; m++) {
            B[m] = 1;
            for (int k = 0; k < m; k++) {
                try {
                    B[m] -= C[k][m] * divMod(B[k], m - k + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                B[m] %= MOD;
            }
            if (B[m] < 0) B[m] += MOD;
        }
    }

    static long divMod(long x, long y) {
        return BIGS[(int) y]
                .modInverse(BIG_MOD)
                .multiply(BigInteger.valueOf(x))
                .mod(BIG_MOD)
                .longValue();
    }
    
    static int highwayConstruction(long n, int m) {
        
        if (n < 3) return 0;
        
        n -= 1;
        BigInteger bigN = BigInteger.valueOf(n);
        long sum = 0;
        for (int k = 0; k <= m; k++) {
            if (B[k] != 0) {
                sum += ((C[k][m + 1] * B[k]) % MOD) * (long) bigN.modPow(BIGS[m + 1 - k], BIG_MOD).mod(BIG_MOD).intValue();
                sum %= MOD;
            }
        }
        sum = divMod(sum, m + 1) - 1;
        if (sum < 0) sum += MOD;
        return (int) sum;
    }

    static int highwayConstructionSlow(long n, int k) {
        BigInteger bigK = BigInteger.valueOf(k);
        long sum = 0;
        for (long i = 2; i < n; i++) {
            int rem = BigInteger.valueOf(i).modPow(bigK, BIG_MOD).intValue();
            sum += rem;
            sum %= MOD;
        }
        return (int) sum;
    }

    public static void main(String[] args) {
        combinations();
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int a0 = 0; a0 < q; a0++) {
            long n = in.nextLong();
            int k = in.nextInt();
            int result = highwayConstruction(n, k);
            sb.append(result).append('\n');
        }
        System.out.print(sb.toString());
        in.close();
    }

    static long[] B2 = {1, 833333341, 633333339, 976190485, 633333339, 469696974, 160805862, 833333342, 574509802, 607769484, 269696443, 7252569, 160719282, 834758858, 88793724, 375425307, 458194177, 447972532, 840005835, 147911937, 906304171, 835100855, 790434390, 18465409, 219852826, 729430218, 549724558, 185958893, 966180022, 875441660, 163991022, 122478099, 356692668, 852972500, 843757650, 263617252, 612272568, 983136603, 460777770, 440931647, 1931634, 537844370, 329269021, 664160220, 301809573, 147947308, 209437816, 487053062, 903512931, 38883016, 399980359, 832893605, 137659912, 32046654, 96201969, 272382393, 984809722, 567516997, 277826241, 129450247, 859219181, 495505064, 8707610, 149991844, 197841389, 415837901, 452556555, 255969333, 587269527, 263414781, 832704626, 646359153, 886506266, 485734315, 770784160, 229289872, 784336735, 410524300, 585080214, 141553402, 425223057, 237545850, 63589729, 33858933, 325120530, 395472691, 961112492, 920184514, 418233316, 328311025, 88615861, 818164287, 583547044, 880260567, 227383121, 652754015, 195906609, 143074934, 826044587, 387905283, 604711815, 42021458, 18853119, 261939927, 570870708, 936427877, 22903034, 332187245, 174614373, 11038394, 363254813, 525874981, 658368378, 396296094, 783547564, 20855970, 562367802, 949243780, 211426253, 798316326, 2189158, 549188555, 65168462, 558688513, 614620286, 597722193, 560494311, 673146368, 51251074, 481381363, 701221794, 610905740, 538953990, 735130715, 122194196, 840631161, 315715642, 476095583, 89037894, 197626168, 379851231, 352939984, 1603881, 495686568, 452061366, 22791554, 467084765, 112929067, 26339868, 641824014, 151097054, 27572593, 128712039, 336837738, 136194315, 827051682, 733043040, 940867749, 601253373, 77767278, 414299962, 932037775, 738226723, 239077453, 393853968, 722937698, 91409035, 545345048, 114641356, 148877819, 262317365, 250818962, 921863573, 594138284, 216245249, 268198006, 644235673, 45020701, 15197600, 932257660, 233031406, 893606130, 102728268, 844873407, 402162002, 818789428, 29315973, 246877693, 786369190, 190539628, 650370482, 336782181, 307008561, 182715307, 32544774, 199166544, 936317371, 472944157, 996413699, 308540076, 724760508, 278710238, 728549643, 895187549, 597018742, 891332152, 213314275, 232858337, 769598130, 60714296, 513821745, 334412618, 355998556, 155515182, 272958997, 91080539, 289948741, 18208039, 228396089, 685375239, 932013571, 354972820, 142772925, 327077220, 147126894, 545255853, 326621095, 150434414, 41482011, 669751880, 517959223, 39900214, 101166409, 590690618, 455010719, 605684678, 317918217, 211140566, 435382484, 401548394, 598150259, 526705503, 214208303, 973980798, 259608707, 651883296, 640931689, 709670733, 845424175, 910642853, 117887071, 940539107, 447181503, 775851453, 798595153, 646557270, 598371650, 182121197, 216655153, 233674960, 348110444, 969342628, 941227791, 927291817, 548282982, 35494838, 193345727, 634083526, 848413421, 692263441, 141039063, 420990465, 99968958, 885427102, 974652496, 710104495, 758887858, 657847149, 905536919, 138925594, 961365471, 792067907, 830174595, 887505348, 856653405, 66520656, 464436811, 931952940, 183318987, 89044938, 470198525, 100406774, 2440362, 314128474, 443702464, 253825927, 458301637, 517281429, 216517109, 115148439, 352788208, 524532489, 731457312, 867734240, 702581780, 384432913, 998018408, 799667103, 766683031, 255467277, 769679929, 446799771, 506785884, 90367510, 810460783, 390906268, 739257138, 173415006, 188140175, 540061588, 567270286, 424512513, 502150529, 213408829, 413161595, 961560788, 510494972, 838316082, 879558681, 830705431, 591747767, 237101735, 140378135, 852001724, 331668928, 708252241, 68662461, 182555116, 893538359, 936149439, 951194459, 486306514, 271650811, 699214099, 765695912, 19806886, 205867235, 580303862, 31389524, 219423408, 665425392, 799852916, 744435881, 762868613, 310465177, 526023846, 658432441, 742149662, 110910902, 258750411, 409162808, 977047796, 43894115, 480488643, 268465155, 100559602, 867519227, 633379197, 98138438, 916305323, 103527234, 695952670, 51702511, 697247040, 269625520, 512484589, 959771857, 139799501, 182818844, 360880892, 676601375, 571409550, 423527592, 998937450, 911882358, 357372431, 569826883, 927762812, 789489021, 941796576, 176041796, 72554256, 301167323, 13654611, 452813689, 51503670, 762831889, 585781233, 121672429, 305331100, 967289676, 154103581, 257678198, 977205795, 980551098, 223350161, 894968118, 917907055, 808738276, 937225992, 468349251, 940092683, 30717348, 712135863, 612214371, 200486087, 936406892, 100110001, 427337831, 945651565, 236598252, 690549125, 394737961, 776706329, 299478746, 658529836, 307757330, 566747663, 710125911, 827130658, 9983955, 323916365, 607915625, 685905468, 972001040, 682676454, 310811738, 774922757, 644321926, 512199688, 643906074, 46604419, 857891194, 529476487, 909815091, 734569750, 169167645, 574820238, 181574243, 395929881, 329346463, 236503503, 481065962, 660068394, 39488967, 767760914, 846703781, 131607844, 31944430, 57414656, 103890805, 262746099, 414410559, 887714003, 318008553, 264525958, 313716146, 417633607, 298841184, 773043888, 111856336, 863238900, 906822925, 696923996, 114496374, 434214180, 200788426, 428710692, 644886298, 520452372, 337637872, 113445988, 647989717, 17926007, 750915521, 147574031, 620849157, 433572160, 270718319, 404134457, 242824034, 212675460, 812599908, 693194593, 370922888, 886952955, 700862511, 714683457, 266781052, 254450987, 104223891};
    static {
        B[1] = 500000005;
        for (int i = 0; i < B2.length; i++) {
            B[2 * i] = B2[i];
        }
    }
}
