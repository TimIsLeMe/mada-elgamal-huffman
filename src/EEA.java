import java.math.BigInteger;

public class EEA {

    public static BigInteger ggT(BigInteger num1, BigInteger num2) {
        return new Res(num1, num2).getGGT();
    }

    public static BigInteger inverseGGT(BigInteger num1, BigInteger num2) {
        return new Res(num1, num2).getInverseGGT();
    }

    private static class Res {
        public BigInteger x0, x1, y0, y1, q, r, initialA, initialB;
        public Res(BigInteger num1, BigInteger num2) {
            x0 = BigInteger.ONE;
            y0 = BigInteger.ZERO;
            x1 = BigInteger.ZERO;
            y1 = BigInteger.ONE;
            BigInteger a = num1.compareTo(num2) > 0 ? num1 : num2;
            BigInteger b = a == num1 ? num2 : num1;
            initialA = a;
            initialB = b;
            do  {
                q =  a.divide(b);
                r = a.mod(b);
                BigInteger x1Temp = x1;
                BigInteger y1Temp = y1;
                x1 = x0.subtract(q.multiply(x1));
                y1 = y0.subtract(q.multiply(y1));
                x0 = x1Temp;
                y0 = y1Temp;
                a = b;
                b = r;
            }
            while(!r.equals(BigInteger.ZERO) && b.compareTo(BigInteger.ZERO) >= 0);
            while (y0.compareTo(BigInteger.ZERO) < 0) {
                y0 = y0.add(initialA); //make sure y0 is > 0
            }
        }

        public BigInteger getGGT(){
            return x0.multiply(initialA).add(y0.multiply(initialB)); // normal ggT result
        }

        public BigInteger getInverseGGT(){
            return y0; // for getting the private key -> d
        }
    }
}
