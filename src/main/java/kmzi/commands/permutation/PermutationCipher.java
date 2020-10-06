package kmzi.commands.permutation;

import picocli.CommandLine;

@CommandLine.Command(name = "permutation", aliases = "pr", subcommands = {
        PREncrypt.class,
        PRDecrypt.class
})
public class PermutationCipher {
    // nothing here
}