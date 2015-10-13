package se.umu.cs._5dv147_proj.middleware.settings;

import org.apache.commons.cli.*;
import se.umu.cs._5dv147_proj.middleware.message.container.ContainerType;

/**
 * Created by c10mjn on 19/09/15.
 */
public class ClientCommandLine {
    private CommandLineParser cmd;
    public String nameserverAdress;
    public String nameserverPort;
    public String nickName;
    public ContainerType containerType;

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
        if(com.hasOption("u")){
            nickName = com.getOptionValue("u");
        }else{
            nickName = "Unknown user";
        }
        if(com.hasOption("c")){
            String c = com.getOptionValue("c");
            if(c.equals("unordered")){
                containerType = ContainerType.Unordered;
            }else if(c.equals("causal")){
                containerType = ContainerType.Causal;
            }else{
                containerType = ContainerType.Causal;
            }
        }
    }

    private static Options createOptionsObject() {
        Options comOptions = new Options();
        comOptions.addOption("d", "debug", false, "Enable debug mode");
        comOptions.addOption("a", "adress", true, "Nameserver Adress");
        comOptions.addOption("p", "port", true, "Nameserver port");
        comOptions.addOption("u", "username", true, "User nickname");
        comOptions.addOption("c", "container", true, "Container type");
        // addOptions(flag, shortDesc, hasArg, longDesc);
        return comOptions;
    }
}


