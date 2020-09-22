package kmzi.commands.affine;

import picocli.CommandLine;

@CommandLine.Command(name = "affine", aliases = "af", subcommands = {
        AFEncrypt.class,
        AFDecrypt.class
})
public class AffineCipher {
    // nothing here
}
