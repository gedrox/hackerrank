package treecoordinates;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Solution {
    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        int p[] = {-1, 0, 0, 0, 1, 1};
        int ch[][] = {{1, 2, 3}, {4, 5}, {}, {}, {}, {}};
        int n = p.length;

        int[] up = new int[n];
        int[] down = new int[n];
        int[] max = new int[n];

        LinkedList<Integer> q = new LinkedList<>();
        int[] depthLast = new int[n];
        int dli = 0;
        q.add(0);
        int depth = 0;
        while (!q.isEmpty()) {
            int node = q.pollFirst();
            if (node < 0) {
                depth = -node;
                continue;
            }
            depthLast[dli++] = node;
            q.add(-depth-1);
            for (int child : ch[node]) {
                q.add(child);
            }
            up[node] = depth;
        }

        System.err.println(Arrays.toString(up));

        for (int i = n - 1; i >= 0; i--) {
            int node = depthLast[i];
            if (p[node] != -1) {
                down[p[node]] = Math.max(down[p[node]], down[node] + 1);
            }
        }

        System.err.println(Arrays.toString(down));

        for (int node : depthLast) {
            if (p[node] != -1) {
                max[node] = max[p[node]] + 1;
                for (int child : ch[p[node]]) {
                    if (child != node) {
                        if (down[child] + 2 > max[node]) {
                            max[node] = down[child] + 2;
                        }
                    }
                }
            }
        }

        for (int node = 0; node < n; node++) {
            if (up[node] > max[node]) max[node] = up[node];
            if (down[node] > max[node]) max[node] = down[node];
        }

        System.err.println(Arrays.toString(max));

        Toolkit.getDefaultToolkit().beep();

        tone(1000,100);
//        Thread.sleep(1000);
        tone(100,1000);
//        Thread.sleep(1000);
        tone(5000,100);
//        Thread.sleep(1000);
        tone(400,500);
//        Thread.sleep(1000);
        tone(400,500, 0.2);

    }

    public static void tone(int hz, int msecs, double vol)
            throws LineUnavailableException
    {
        byte[] buf = new byte[1];
        AudioFormat af =
                new AudioFormat(
                        SAMPLE_RATE, // sampleRate
                        8,           // sampleSizeInBits
                        1,           // channels
                        true,        // signed
                        false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs)
            throws LineUnavailableException
    {
        tone(hz, msecs, 1.0);
    }


}
