package se.umu.cs._5dv147_proj.groupmanagement.module;

import remote.interfaces.ComModuleInterface;
import se.umu.cs._5dv147_proj.communication.api.ReceiveProxy;
import se.umu.cs._5dv147_proj.settings.Debug;


import java.rmi.NoSuchObjectException;
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
    private ComModuleInterface myStub;
    private String groupName;


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
        this.myStub = (ComModuleInterface) UnicastRemoteObject.exportObject(this.com, 0);
        this.proxyList.put(myStub, com.getNickName());
        ns = new NameServerCom(nameServerAddress, port, myStub);
    }

    /**
     *
     * @param groupName group user whishes to join.
     * @throws RemoteException
     *
     */
    public ComModuleInterface joinGroup(String groupName) throws RemoteException {
        this.leader = ns.joinGroup(groupName);
        this.groupName = groupName;
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
    public void removeMember(ComModuleInterface member) throws RemoteException {
        //Special case: Leader is leaving.
        if (compareProxy(member) && compareProxy(leader)) {
            Debug.getDebug().log("This is the leader receiveing it's own message.");
            try {
                if (UnicastRemoteObject.unexportObject(this.myStub, true)) {
                    Debug.getDebug().log("Leader stub closed");
                }
            } catch (NoSuchObjectException noe) {
                Debug.getDebug().log(noe);
            }

        } else if (member.equals(this.leader) && !(member.equals(this.com))) {
            Debug.getDebug().log("Leader is leaving " + member.getNickName());
            this.leader = this.ns.takeLeader(this.groupName);

        } else {
            Debug.getDebug().log("Regular member is leaving: " + member.getNickName());
        }

        this.proxyList.remove(member);
        //Regular case: Regular member is leaving.


    }

    public <T extends ComModuleInterface> boolean compareProxy(T com) throws RemoteException {
       return com.equals(this.myStub);
    }


}
