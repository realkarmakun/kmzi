package kmzi.commands;

import kmzi.commands.affine.AffineCipher;
import kmzi.commands.permutation.PermutationCipher;
import kmzi.commands.pseudorandomgen.PseudorandomGen;
import picocli.CommandLine;


@CommandLine.Command(name = "cmip", aliases = "cm", subcommands = {
        AffineCipher.class,
        PermutationCipher.class,
        PseudorandomGen.class
})
public class Cmip {
    // Это пустой класс который отвечает за родительскую команду cmip. Сам по себе ничего не делает.
}