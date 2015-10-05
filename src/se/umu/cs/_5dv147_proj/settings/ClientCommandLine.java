package se.umu.cs._5dv147_proj.settings;

import org.apache.commons.cli.*;

/**
 * Created by c10mjn on 19/09/15.
 */
public class ClientCommandLine {
    private CommandLineParser cmd;
    public boolean debug = false;
    public String nameserverAdress;
    public String nameserverPort;

    public ClientCommandLine(String[] args) throws ParseException {
        this.cmd = new DefaultParser();
        Options comOpt = createOptionsObject();
        CommandLine com = this.cmd.parse(comOpt, args);
        if (com.hasOption("d")) {
            Debug.getDebug().enableDebug();
        }
        if (com.hasOption("a")) {
            nameserverAdress = com.getOptionValue("a");
        }
        if (com.hasOption("p")) {
            nameserverPort = com.getOptionValue("p");
        }
    }

    private static Options createOptionsObject() {
        Options comOptions = new Options();
        comOptions.addOption("d", "debug", false, "Enable debug mode");
        comOptions.addOption("a", "adress", true, "Nameserver Adress");
        comOptions.addOption("p", "port", true, "Nameserver port");
        // addOptions(flag, shortDesc, hasArg, longDesc);
        return comOptions;
    }
}


