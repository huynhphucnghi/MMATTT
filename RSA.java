import java.util.*;
import java.io.IOException;
import java.math.BigInteger;

public class RSA {
    private BigInteger p, q;
    private BigInteger n;
    private BigInteger PhiN;
    private BigInteger e, d;
    private int SIZE;

    public RSA() {
        initialize();
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
        return Utils.bigModInverse(e, RSA.bPhi(p, q));
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


    /**
     * Generate key pair from p & q
     * @param p The first prime number
     * @param q The second prime number
     */
    public void genKeyPair(BigInteger p, BigInteger q)
    {
        n = p.multiply(q);
        PhiN = RSA.bPhi(p, q);
        do {
            e = new BigInteger(2 * SIZE, new Random());
        } while ((e.compareTo(PhiN) != 1)
                || (Utils.bigGCD(e,PhiN).compareTo(BigInteger.ONE) != 0));
        d = RSA.bPrivateKey(e, p, q);
    }

    // TODO: reconstruct follow above function
    public void initialize() {
        SIZE = 64;
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
        this.genKeyPair(p, q);
    }

    /**
     * Encrypt plaintext using public key.
     * If plaintext is larger than or equal to n then split it into blocks of multiple bytes that are less than n
     * Each block will be encrypted separately and then concatenated to return the final result
     */
    public BigInteger encrypt(BigInteger plaintext) {
        if (plaintext.compareTo(n) == -1) {
            return Utils.bigModPow(plaintext, e, n);
        }
        int blockByteLen = (n.bitLength() - 1) / 8;
        int blockNum = (plaintext.bitLength() - 1) / (blockByteLen * 8) + 1;
        List<BigInteger> resList = new ArrayList<>();
        byte[] bytesPlaintext = plaintext.toByteArray();
        // Split and encrypt each block
        for (int i = 0; i < blockNum; i++) {
            int startByte = bytesPlaintext.length - (i + 1) * blockByteLen;
            int endByte = startByte + blockByteLen;
            startByte = Math.max(startByte, 0);
            BigInteger block = BigInteger.valueOf(bytesPlaintext[startByte]);
            for (int j = startByte + 1; j < endByte; j++) {
                block = block.shiftLeft(8).add(BigInteger.valueOf(bytesPlaintext[j]));
            }
            resList.add(0, Utils.bigModPow(block, e, n));
        }
        // Concatenate all
        int eBlockByteLen = blockByteLen + 1; // encrypted blocks may have a bigger size than unencrypted blocks, thus we allocate one more byte for them
        return Utils.concatBigInteger(resList, eBlockByteLen);
    }

    /**
     * Decrypt cipherText using private key.
     * If cipherText is larger than or equal to n then split it into blocks of multiple bytes that are less than n
     * Each block will be decrypted separately and then concatenated to return the final result
     */
    public BigInteger decrypt(BigInteger cipherText) {
        if (cipherText.compareTo(n) == -1) {
            return Utils.bigModPow(cipherText, d, n);
        }
        int eBlockByteLen = (n.bitLength() - 1) / 8 + 1;
        int eBlockNum = (cipherText.bitLength() - 1) / (eBlockByteLen * 8) + 1;
        List<BigInteger> resList = new ArrayList<>();
        byte[] bytesCipherText = cipherText.toByteArray();
        // Split and decrypt each encrypted block
        for (int i = 0; i < eBlockNum; i++) {
            int startByte = bytesCipherText.length - (i + 1) * eBlockByteLen;
            int endByte = startByte + eBlockByteLen;
            startByte = Math.max(startByte, 0);
            BigInteger eBlock = BigInteger.valueOf(bytesCipherText[startByte] & 0xFF);
            for (int j = startByte + 1; j < endByte; j++) {
                eBlock = eBlock.shiftLeft(8).add(BigInteger.valueOf(bytesCipherText[j] & 0xFF));
            }
            resList.add(0, Utils.bigModPow(eBlock, d, n));
        }
        // Concatenate all
        int blockByteLen = eBlockByteLen - 1; // decrypted block has 1 byte less than encrypted block, see the encrypt function
        return Utils.concatBigInteger(resList, blockByteLen);
    }


    public static void main(String[] args) throws IOException {
        RSA app = new RSA();
        System.out.println("Enter any character : ");
        Scanner scanner = new Scanner(System.in);
        String plaintext = scanner.nextLine();

        BigInteger bplaintext, bciphertext;
        bplaintext = Utils.str2BigInteger(plaintext);
        System.out.println("Plaintext : " + bplaintext.toString());

        // Encrypt
        bciphertext = app.encrypt(bplaintext);
        System.out.println("Ciphertext : " + bciphertext.toString());

        // Decrypt
        bplaintext = app.decrypt(bciphertext);
        System.out.println("After Decryption Plaintext : " + Utils.bigInteger2Str(bplaintext));
    }
}