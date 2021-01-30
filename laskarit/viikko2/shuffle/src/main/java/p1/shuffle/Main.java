/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p1.shuffle;

import java.util.HashMap;
import java.util.Random;


/**
 *
 * @author konstakallama
 */
public class Main {
    static Random r = new Random();
    public static void main(String[] args) {
        interateS(10000000, 5);
    }
    
    public static int[] shuffle(int[] A) {
        int[] B = new int[A.length];
        System.arraycopy(A, 0, B, 0, A.length);
        for (int i = 0; i < A.length; i++) {
            int rn = r.nextInt(A.length);
            swap(B, i, rn);
        }
        
        return B;
    }
    
    public static int[] shuffle2(int[] A) {
        int[] B = new int[A.length];
        System.arraycopy(A, 0, B, 0, A.length);
        for (int i = 0; i < A.length; i++) {
            int rn = i + r.nextInt(A.length - i);
            swap(B, i, rn);
        }
        
        return B;
    }
    
    public static void interateS(int times, int len) {
        HashMap<String, Integer> m = new HashMap<>();
        int[] A = generateA(len);
        for (int i = 0; i < times; i++) {
            int[] B = shuffle(A);
            String s = convertA(B);
            if (!m.containsKey(s)) {
                m.put(s, 1);
            } else {
                m.replace(s, m.get(s) + 1);
            }
        }
        printM(m, times);
    }
    
    public static void interateS2(int times, int len) {
        HashMap<String, Integer> m = new HashMap<>();
        int[] A = generateA(len);
        for (int i = 0; i < times; i++) {
            int[] B = shuffle2(A);
            String s = convertA(B);
            if (!m.containsKey(s)) {
                m.put(s, 1);
            } else {
                m.replace(s, m.get(s) + 1);
            }
        }
        printM(m, times);
    }
    
    public static String convertA(int[] A) {
        String s = "";
        for (int i : A) {
            s += A[i];
        }
        return s;
    }
    
    public static int[] acopy(int[] A) {
        int[] B = new int[A.length];
        System.arraycopy(A, 0, B, 0, A.length);
        return B;
    }

    private static void swap(int[] B, int i, int rn) {
        int temp = B[i];
        B[i] = B[rn];
        B[rn] = temp;
    }

    private static int[] generateA(int len) {
        int[] A = new int[len];
        for (int i = 0; i < len; i++) {
            A[i] = i;
        }
        return A;
    }

    private static void printM(HashMap<String, Integer> m, int total) {
        Double t = Double.parseDouble(total + "");
        for (String s : m.keySet()) {
            Double d = (m.get(s)/t) * 100.0;
            String p = String.format("%.2f", d);
            System.out.println(s + ": " + p + "%");
            //+ m.get(s) + ", "
        }
    }
    
}
