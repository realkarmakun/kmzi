package kmzi.commands;

import kmzi.commands.affine.AffineCipher;
import kmzi.commands.crc.CRCHash;
import kmzi.commands.permutation.PermutationCipher;
import kmzi.commands.pseudorandomgen.PseudorandomGen;
import kmzi.commands.rsa.RSACipher;
import picocli.CommandLine;


@CommandLine.Command(name = "cmip", aliases = "cm", subcommands = {
        AffineCipher.class,
        PermutationCipher.class,
        PseudorandomGen.class,
        RSACipher.class,
        CRCHash.class
})
public class Cmip {
    // Это пустой класс который отвечает за родительскую команду cmip. Сам по себе ничего не делает.
}