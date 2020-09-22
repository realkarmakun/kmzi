package kmzi.commands;

import kmzi.commands.affine.AffineCipher;
import picocli.CommandLine;


@CommandLine.Command(name = "cmip", aliases = "cm", subcommands = {
        AffineCipher.class
})
public class Cmip {
    // Это пустой класс который отвечает за родительскую команду cmip. Сам по себе ничего не делает.
}