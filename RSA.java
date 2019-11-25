import java.util.Scanner;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private BigInteger p, q;
    private BigInteger n;
    private BigInteger PhiN;
    private BigInteger e, d;

    public RSA() {
        initialize();
    }

    /**
     * Calculate the greatest common divisor from 2 number a & b
     * @param a A non negative number
     * @param b A non negative number
     * @return greatest common divisor of a & b
     */
    static private BigInteger bigGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return a;
        return bigGCD(b, a.mod(b));
    }

    /**
     *
     * @param a
     * @param m
     * @return
     */
    static private BigInteger bigModInverse(BigInteger a, BigInteger m) 
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
     * Power and modulo
     * @param x The base number
     * @param y The exponent
     * @param p The divisor
     * @return (x^y) mod p
     */
    static private BigInteger bigModPow(BigInteger x, BigInteger y, BigInteger p) 
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
     * Calculate ø(p*q)
     * @param p The first prime number
     * @param q The second prime number
     * @return number of positive integers less than p*q that are relatively prime to p*q
     */
    static public BigInteger bPhi(BigInteger p, BigInteger q)
    {
        BigInteger PhiN = p.subtract(BigInteger.ONE);
        return PhiN.multiply(q.subtract(BigInteger.ONE));
    }

    /**
     * Calculate private key from p,q and e
     * @param e Public key
     * @param p The first prime number
     * @param q The second prime number
     * @return Private key
     */
    static public BigInteger bPrivateKey(BigInteger e, BigInteger p, BigInteger q)
    {
        return bigModInverse(e, RSA.bPhi(p, q));
    }

    /**
     * Generate key pair from p & q
     * @param p The first prime number
     * @param q The second prime number
     */
    static public void genKeyPair(BigInteger p, BigInteger q)
    {
        n = p.multiply(q);
        PhiN = RSA.bPhi(p, q);
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while ((e.compareTo(PhiN) != 1)
                || (bigGCD(e,PhiN).compareTo(BigInteger.ONE) != 0));
        d = RSA.bPrivateKey(e, p, q);
    }

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

    static public BigInteger genPrime(AKS tb, int size)
    {
        BigInteger six = new BigInteger("6");
        BigInteger p = new BigInteger(size, new Random()).setBit(size-1);
        BigInteger m = p.divide(six);
        p = m.multiply(six).subtract(BigInteger.ONE);
        while (true)
        {
            if (tb.checkIsPrime(p)){
                break;
            }

            p = p.add(new BigInteger("2"));
            if (tb.checkIsPrime(p))
                break;

            p = p.add(new BigInteger("4"));
            if (p.bitLength() != size)
                p = new BigInteger(size, new Random()).setBit(size-1);
        }
        return p;
    }

    // TODO: reconstruct follow above function 
    public void initialize() {
        int SIZE = 64;
        AKS tb = new AKS();
        
        /* Step 1: Select two large prime numbers. Say p and q. */
        long startTime = System.currentTimeMillis();
        p = genPrime(tb, SIZE);
        q = genPrime(tb, SIZE);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Total prime generator time: " + elapsedTime + " ms");
        System.out.println(p);
        System.out.println(q);
        RSA.genKeyPair(p, q);
    }

    public BigInteger encrypt(BigInteger plaintext) {
        return bigModPow(plaintext, e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return bigModPow(ciphertext, d, n);
    }


    public static void main(String[] args) throws IOException {
        RSA app = new RSA();
        System.out.println("Enter any character : ");
        Scanner scanner = new Scanner(System.in);
        long plaintext = scanner.nextLong();
        BigInteger bplaintext, bciphertext;
        bplaintext = BigInteger.valueOf((long) plaintext);
        bciphertext = app.encrypt(bplaintext);
        System.out.println("Plaintext : " + bplaintext.toString());
        System.out.println("Ciphertext : " + bciphertext.toString());
        bplaintext = app.decrypt(bciphertext);
        System.out.println("After Decryption Plaintext : "
                + bplaintext.toString());
    }
}