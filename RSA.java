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

    static private BigInteger bigGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return a;
        return bigGCD(b, a.mod(b));
    }

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

    static public BigInteger bPhi(BigInteger p, BigInteger q)
    {
        return BigInteger.ZERO;
    }

    static public BigInteger bPubKey(BigInteger e, BigInteger p, BigInteger q)
    {
        return BigInteger.ZERO;
    }

    static public void bKeyPair(BigInteger e, BigInteger d, BigInteger p, BigInteger q)
    {
        return;
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

    // TODO: reconstruct follow above function 
    public void initialize() {
        int SIZE = 512;
        /* Step 1: Select two large prime numbers. Say p and q. */
        p = new BigInteger(SIZE, 15, new Random());
        q = new BigInteger(SIZE, 15, new Random());
        System.out.println(p);
        System.out.println(q);
        /* Step 2: Calculate n = p.q */
        n = p.multiply(q);
        /* Step 3: Calculate ø(n) = (p - 1).(q - 1) */
        PhiN = p.subtract(BigInteger.ONE);
        PhiN = PhiN.multiply(q.subtract(BigInteger.ONE));
        /* Step 4: Find e such that gcd(e, ø(n)) = 1 ; 1 < e < ø(n) */
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while ((e.compareTo(PhiN) != 1)
                || (bigGCD(e,PhiN).compareTo(BigInteger.ONE) != 0));
        /* Step 5: Calculate d such that e.d = 1 (mod ø(n)) */
        d = bigModInverse(e, PhiN);
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