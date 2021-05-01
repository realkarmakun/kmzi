package kmzi.commands.rsa;

import picocli.CommandLine;

@CommandLine.Command(name = "rsa", subcommands = {
        RSAEncrypt.class,
        RSADecrypt.class
})
public class RSACipher {
    // Nothing here
}
