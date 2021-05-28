package kmzi.commands.crc;

import picocli.CommandLine;

import java.math.BigInteger;
import java.util.concurrent.Callable;

import static kmzi.commands.crc.CRCHash.calculateCrc;
import static kmzi.utils.tools.altModInverse;
import static kmzi.utils.tools.gcd;

@CommandLine.Command(name = "sign")
public class CRCSignature implements Callable<Integer> {
    @CommandLine.Parameters(description = "Коэффиценты G(x)=a₈x⁸+a₇x⁷+a₆x⁶+a₅x⁵+a₄x⁴+a₃x³+a₂x²+a₁x+a₀")
    public int g_x;

    @CommandLine.Parameters(description = "Коэффиценты P(x)=a₈x⁸+a₇x⁷+a₆x⁶+a₅x⁵+a₄x⁴+a₃x³+a₂x²+a₁x+a₀")
    public int p_x;

    @CommandLine.Parameters(description = "p")
    public int p;

    @CommandLine.Parameters(description = "g")
    public int g;

    @CommandLine.Parameters(description = "x")
    public int x;

    @Override
    public Integer call() throws Exception {
        int h = calculateCrc(p_x, g_x);

        // Находим y, r, u, s
        int y = (int) (Math.pow(g, x)) % p;
        int r = (int) (Math.pow(g, k_satisfying(p))) % p;
        int u = (h+(-1+p-1)*x*r) % (p-1);
        int s = (int) (altModInverse(k_satisfying(p), p-1)* u) % (p-1);
        System.out.println("M="+Integer.parseInt(String.valueOf(p_x), 2)+", r="+r+", s="+s);
        System.out.println("Результат проверки подписи: "+signature_verification(y, r, p, s, p_x, g_x, g));
        return 0;
    }

    public static int k_satisfying(int p){ //находим k, удовлетворяющий условиям
        int k = 0;
        for (int i=2; i<p-1; i++) {
            if (gcd(i,p-1) == 1) {
                int k_1 = altModInverse(i, p-1);
                if (k_1 != -1) {
                    k=i;
                    i=p-1;
                }
            }
        }
        return k;
    }

    public static boolean signature_verification( int y, int r, int p, int s, int M, int gg, int g){
        boolean f = false;
        int h=Integer.parseInt(String.valueOf(calculateCrc(M, gg)), 2);
        BigInteger n_ = BigInteger.valueOf(p);
        BigInteger y_ = BigInteger.valueOf(y);
        BigInteger r_ = BigInteger.valueOf(r);
        BigInteger g_ = BigInteger.valueOf(g);
        BigInteger a=((y_.pow(r)).multiply(r_.pow(s))).mod(n_);
        BigInteger b=g_.pow(h).mod(n_);
        if(a.equals(b)) {
            f = true;
        }
        return f;
    }
}
