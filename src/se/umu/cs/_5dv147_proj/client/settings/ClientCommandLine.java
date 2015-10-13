package se.umu.cs._5dv147_proj.client.settings;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by c10mjn on 19/09/15.
 */
public class ClientCommandLine {
    private CommandLineParser cmd;


    public ClientCommandLine(String[] args) throws ParseException {
        this.cmd = new DefaultParser();
        Options comOpt = createOptionsObject();
        this.cmd.parse(comOpt, args);

    }


    private static Options createOptionsObject() {
        Options comOptions = new Options();
        comOptions.addOption("d", "debug", false, "Enable debug mode");
        // addOptions(flag, shortDesc, hasArg, longDesc);
        return comOptions;
    }
}

