import java.math.BigInteger;

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
}