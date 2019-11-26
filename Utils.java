import java.math.BigInteger;

public class Utils {
    static public BigInteger str2BigInteger(String plainText)
    {
        byte [] byteTmp = plainText.getBytes();
        BigInteger bPlainText = BigInteger.ZERO;
        try {
            bPlainText = new BigInteger(byteTmp);

        } catch(NumberFormatException ex) {
            System.err.println("Plain text must not be empty");
        }
        return bPlainText;
    }

    static public String bigInteger2Str(BigInteger cipherText)
    {
        byte [] plainBytes = cipherText.toByteArray();
        String plainText  = new String(plainBytes);
        return plainText;
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