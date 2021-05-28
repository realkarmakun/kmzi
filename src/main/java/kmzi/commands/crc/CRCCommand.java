package kmzi.commands.crc;

import picocli.CommandLine;

@CommandLine.Command(name = "crc", subcommands = {
        CRCHash.class,
        CRCSignature.class
})
public class CRCCommand {
    // Nothing here
}