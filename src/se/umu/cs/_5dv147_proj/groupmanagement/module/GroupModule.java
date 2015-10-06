package se.umu.cs._5dv147_proj.groupmanagement.module;

import remote.interfaces.ComModuleInterface;
import se.umu.cs._5dv147_proj.communication.api.ReceiveProxy;
import se.umu.cs._5dv147_proj.settings.Debug;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

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
    private ReceiveProxy com;
    private HashMap<ComModuleInterface, String> proxyList;
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
        this.proxyList = new HashMap<>();
        this.com = new ReceiveProxy(nickName);
        ComModuleInterface stub = (ComModuleInterface) UnicastRemoteObject.exportObject(this.com, 0);
        this.proxyList.put(stub, com.getNickName());
        ns = new NameServerCom(nameServerAddress, port, stub);
    }

    /**
     *
     * @param groupName group user whishes to join.
     * @throws RemoteException
     *
     */
    public ComModuleInterface joinGroup(String groupName) throws RemoteException {
        this.leader = ns.joinGroup(groupName);
        return this.leader;
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



    /**
     *
     * @return List of all known members
     */
    public ArrayList<ComModuleInterface> getProxyList() {
        ArrayList<ComModuleInterface> proxys = new ArrayList<>();
        proxys.addAll(this.proxyList.keySet());
        return proxys;
    }

    public ArrayList<String> getNameList(){
        ArrayList<String> names = new ArrayList<>();
        names.addAll(this.proxyList.values());
        return names;
    }

    /**
     * @param newMember the new member.
     */
    public boolean addMember(ComModuleInterface newMember) {
        String putValue;
        try {
            putValue = this.proxyList.put(newMember, newMember.getNickName());
        } catch (RemoteException e) {
            putValue = this.proxyList.put(newMember, "Unknown user");
        }
        return (putValue == null);
    }

    /**
     * Get created com module implementation.
     * @return middleware proxy.
     */
    public ReceiveProxy getCommunicationAPI() {
        return this.com;
    }


    /**
     *
     * @param member member to remove.
     */
    public void removeMember(ComModuleInterface member) {
        this.proxyList.remove(member);
    }


}
