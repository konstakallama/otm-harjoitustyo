/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p1.msap;

/**
 *
 * @author konstakallama
 */
public class Main {
    //static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int[] A = initA();    
        System.out.println(ag(A));
    }

    public static Xyz ag(int[] A) {
        int s = 0;
        int ps = 0;
        int a = 0;
        int b = 0;
        int pa = 0;
        int pb = 0;
        for (int i = 0; i < A.length; i++) {
            if (ps + A[i] > 0) {
                if (ps == 0) {
                    pa = i;
                }
                ps += A[i];
                pb = i;
            } else {
                ps = 0;
            }
            if (ps > s) {
                s = ps;
                a = pa;
                b = pb;
            }
        }
        return new Xyz(s, a, b);
    }

    private static int[] initA() {
        int[] A = new int[5];
        A[0] = 3;
        A[1] = 1;
        A[2] = -5;
        A[3] = 2;
        A[4] = 3;
        return A;
    }

    private static class Xyz {

        int s;
        int a;
        int b;

        public Xyz(int s, int a, int b) {
            this.s = s;
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "Xyz{" + "s=" + s + ", a=" + a + ", b=" + b + '}';
        }
    }

}
