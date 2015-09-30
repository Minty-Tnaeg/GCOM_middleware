package se.umu.cs._5dv147_proj;

import org.apache.commons.cli.ParseException;

import remote.objects.ComModuleImp;
import se.umu.cs._5dv147_proj.network.NameServerCom;
import se.umu.cs._5dv147_proj.settings.*;

import java.rmi.RemoteException;

/**
 * Created by c10mjn on 20/09/15.
 */
public class Main {

    public static void main (String[] args)  {
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "middle.policy");
            System.setProperty("java.rmi.server.codebase", "file:/home/c10/c10mjn/edu/5dv147/assignments/project/GCOM_middleware/out/production/GCOM_middleware/se/umu/cs/_5dv147_proj/modules/");
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ClientCommandLine cli = new ClientCommandLine(args);
            ComModuleImp com = new ComModuleImp();

            NameServerCom ns = new NameServerCom("localhost", 33400, com);
            ns.createGroup("HERP");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
