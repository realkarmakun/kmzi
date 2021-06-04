package kmzi.commands.decryption;

import kmzi.commands.permutation.PRDecrypt;
import kmzi.commands.permutation.PREncrypt;
import picocli.CommandLine;

import java.math.BigInteger;
import java.util.concurrent.Callable;

import static kmzi.utils.tools.gcd;

@CommandLine.Command(name = "decrypt", aliases = "dc")
public class DecryptionCommand implements Callable<Integer> {
    @CommandLine.Parameters(description = "N")
    public int N;

    @CommandLine.Option(names = {"-e"}, required = true)
    public int e;

    @CommandLine.Option(names = {"-c"}, required = true)
    public int C;

    //Проводит факторизацию N.
    public static int factorization(int N) {
        int p=0;
        for (int x=2; x<N; x++){
            for (int y=2; y<N; y++){
                if((x*x)%N==(y*y)%N) {
                    if(x!=y){
                        int m=x-y;
                        if(m<0) m=-m;
                        int d=gcd(m, N);
                        if((d!=1)&&(d!=N)) p=d;
                        x=N;
                        y=N;
                    }}
            }
        }

        return p;
    }
    //Определяет закрытый ключ.
    public static int d (int e, int p, int q){
        int d=0;
        for(int i=0; i<(p-1)*(q-1); i++){
            if ((i*e)%((p-1)*(q-1))==1) d=i;
        }
        return d;
    }


    @Override
    public Integer call() throws Exception {
        int p=factorization(N);
        int q=N/p;
        int d=d(e, p, q);
        BigInteger n = new BigInteger(String.valueOf(p*q));
        BigInteger c =new BigInteger(String.valueOf(C));
        //Расшифровывает сообщение.
        BigInteger M=c.pow(d).mod(n);
        System.out.println("Факторизация N: p="+p+" q="+q+"\nЗакрытый ключ: d="+d+"\nРасшифрованное сообщение: M="+M);
        return 0;
    }
}
