import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * Convert String to BigInteger
     */
    static public BigInteger str2BigInteger(String text)
    {
        byte [] byteTmp = text.getBytes();
        BigInteger res = BigInteger.ZERO;
        try {
            res = new BigInteger(byteTmp);
        } catch(NumberFormatException ex) {
            System.err.println("Plain text must not be empty");
        }
        return res;
    }

    /**
     * Convert BigInteger to String
     */
    static public String bigInteger2Str(BigInteger n)
    {
        byte [] plainBytes = n.toByteArray();
        String res  = new String(plainBytes);
        return res;
    }

    /**
     * Power and modulo
     * @param x The base number
     * @param y The exponent
     * @param p The divisor
     * @return (x^y) mod p
     */
    static public BigInteger bigModPow(BigInteger x, BigInteger y, BigInteger p)
    {
        BigInteger res = BigInteger.ONE;
        x = x.mod(p);
        while (y.compareTo(BigInteger.ZERO) == 1){
            if(y.and(BigInteger.ONE).equals(BigInteger.ONE))
                res = res.multiply(x).mod(p);
            y = y.shiftRight(1);
            x = x.multiply(x).mod(p);
        }
        return res;
    }

    /**
     *
     * @param a
     * @param m
     * @return
     */
    static public BigInteger bigPow(BigInteger a, int m) 
    { 
        BigInteger res = BigInteger.ONE;
        while(m > 0){
            // If m is odd: res *= a
            if ((m & 1) == 1)
                res = res.multiply(a);
            // m /= 2
            m = m >> 1;
            a = a.multiply(a);
        }
        return res;
    }

    /**
     * Calculate the greatest common divisor from 2 number a & b
     * @param a A non negative number
     * @param b A non negative number
     * @return greatest common divisor of a & b
     */
    static public BigInteger bigGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return a;
        return bigGCD(b, a.mod(b));
    }

    /**
     *
     * @param a
     * @param m
     * @return
     */
    static public BigInteger bigModInverse(BigInteger a, BigInteger m) 
    { 
        // extend Euclid
        BigInteger m0 = m;
        BigInteger y = BigInteger.ZERO;
        BigInteger x = BigInteger.ONE;
        if (m.equals(BigInteger.ONE)) return BigInteger.ZERO;
        while (a.compareTo(BigInteger.ONE) == 1) {
            BigInteger q = a.divide(m);
            BigInteger t = m;
            m = a.mod(m);
            a = t;
            t = y;
            y = x.subtract(q.multiply(y));
            x = t;
        }
        if (x.compareTo(BigInteger.ZERO) == -1)
            x = x.add(m0);
        return x;
    }

    /**
     * Concatenate block by block from a list of BigInteger into a single BigInteger. Each block has a specified length
     * @param l The list of BigInteger, should have at least one element
     * @param len The number of bytes of a single block
     * @return The concatenated BigInteger
     */
    static public  BigInteger concatBigInteger(List<BigInteger> l, int len) {
        List<BigInteger> l_copy = new ArrayList<>(l);
        BigInteger res = l_copy.remove(0);
        while (!l_copy.isEmpty()) {
            res = res.shiftLeft(len * 8);
            res = res.add(l_copy.remove(0));
        }
        return res;
    }
}