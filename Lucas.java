// Use Lucas-Lehmer to find next Prime
import java.math.BigInteger;
import java.util.Random;
  
class Lucas{ 
    static boolean bigIsPrime(BigInteger p) { 
        BigInteger checkNumber = pow(BigInteger.valueOf(2), p).subtract(BigInteger.ONE);
        BigInteger nextval = BigInteger.valueOf(4).mod(checkNumber);
        BigInteger i = BigInteger.ONE;
        while (i.compareTo(p.subtract(BigInteger.ONE)) == -1){
            nextval = nextval.multiply(nextval).subtract(BigInteger.valueOf(2)).mod(checkNumber);
            i = i.add(BigInteger.ONE);
        }
        return (nextval.equals(BigInteger.ZERO)); 
    } 

    static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
          if (exponent.testBit(0)) result = result.multiply(base);
          base = base.multiply(base);
          exponent = exponent.shiftRight(1);
        }
        return result;
    }
      
    public static void main(String[] args) { 
        BigInteger q = new BigInteger(10, new Random());
        while(!bigIsPrime(q)){
            System.out.println(q + "^2-1 is not Prime.");
            q = q.add(BigInteger.ONE);
        }
        BigInteger qpow = pow(BigInteger.valueOf(2), q).subtract(BigInteger.ONE);
        System.out.println(q + "^2-1=" + qpow + " is Prime"); 
    } 
} 