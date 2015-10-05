package se.umu.cs._5dv147_proj.groupmanagement.module;

import remote.interfaces.ComModuleInterface;
import se.umu.cs._5dv147_proj.communication.api.CommunicationAPI;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-05.
 * @author c10mjn
 *
 * The groupmodule hold communication between NameServer and Middleware.
 * Initiates the GCOM middleswars proxy to RMI registry.
 *
 */
public class GroupModule {
    private ComModuleInterface leader;
    private CommunicationAPI com;
    private ArrayList<ComModuleInterface> proxyList;
    private NameServerCom ns;


    /**
     * Contructor for the GroupModule
     * @param nameServerAddress nameserver address
     * @param port nameServer rmi registry port
     * @param nickName Prefered nickname on server.
     * @throws RemoteException
     *
     * Constructs the group module.
     */
    public GroupModule(String nameServerAddress, int port, String nickName) throws RemoteException {
        this.proxyList = new ArrayList<>();
        this.com = new CommunicationAPI(nickName);
        this.proxyList.add((ComModuleInterface) UnicastRemoteObject.exportObject(this.com, 0));
        ns = new NameServerCom(nameServerAddress, port, this.proxyList.get(0));
    }

    /*********************Name server***********************/


    /**
     *
     * @param groupName group user whishes to join.
     * @throws RemoteException
     *
     */
    public void joinGroup(String groupName) throws RemoteException {
        this.leader = ns.joinGroup(groupName);
    }

    /**
     *
     * @param group group which lacks leader.
     * @throws RemoteException
     */
    public void electLeader(String group) throws RemoteException {
        this.ns.takeLeader(group);
    }

    /**
     *
     * @return Group list of <GroupName, LeaderName>
     * @throws RemoteException
     */
    public String[][] getGroups() throws RemoteException {
        ns.updateGroupList();

        return ns.getGroupList();
    }


    /************************ Group Module *******************/

    /**
     *
     * @return List of all known members
     */
    public ArrayList<ComModuleInterface> getProxyList() {
        return this.proxyList;
    }

    /**
     * @param newMember the new member.
     */
    public void addMember(ComModuleInterface newMember) {
        this.proxyList.add(newMember);
    }

    /**
     * Get created com module implementation.
     * @return middleware proxy.
     */
    public CommunicationAPI getCommunicationAPI() {
        return this.com;
    }


}
