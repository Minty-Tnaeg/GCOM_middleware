package se.umu.cs._5dv147_proj.middleware.groupmanagement.module;

import se.umu.cs._5dv147_proj.middleware.communication.api.DebugProxy;
import se.umu.cs._5dv147_proj.middleware.communication.api.ReceiveProxy;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;


import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by c10mjn on 2015-10-05.
 * @author c10mjn
 *
 * The groupmodule hold communication between NameServer and Middleware.
 * Initiates the GCOM middleswars proxy to RMI registry.
 *
 */
public class GroupModule {
    private ProxyInterface leader;
    private AbstractProxy com;
    private HashMap<ProxyInterface, String> proxyList;
    private NameServerCom ns;
    private ProxyInterface myStub;
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
        if(Debug.getDebug().isEnabled()){
            this.com = new DebugProxy(nickName);
        }else{
            this.com = new ReceiveProxy(nickName);
        }
        this.myStub = (ProxyInterface) UnicastRemoteObject.exportObject(this.com, 0);
        this.proxyList.put(myStub, com.getNickName());
        ns = new NameServerCom(nameServerAddress, port, myStub);
    }

    /**
     *
     * @param groupName group user whishes to join.
     * @throws RemoteException
     *
     */
    public ProxyInterface joinGroup(String groupName) throws RemoteException {
        this.leader = ns.joinGroup(groupName);
        this.groupName = groupName;
        return this.leader;
    }

    /**
     *
     * @throws RemoteException
     */
    public void takeLeader() throws RemoteException {
        this.ns.takeLeader(this.groupName);
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
    public ArrayList<ProxyInterface> getProxyList() {
        ArrayList<ProxyInterface> proxys = new ArrayList<>();
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
    public boolean addMember(ProxyInterface newMember) {
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
    public AbstractProxy getCommunicationAPI() {
        return this.com;
    }


    /**
     *
     * @param member member to remove.
     */
    public void removeMember(ProxyInterface member) throws RemoteException {
        //Special case: Leader is leaving.
        if (compareProxy(member) && compareProxy(leader)) {
            Debug.getDebug().log("This is the leader receiveing it's own message.");
        } else if (member.equals(this.leader) && !(member.equals(this.com))) {
            Debug.getDebug().log("Leader is leaving " + member.getNickName());
            this.leader = this.ns.takeLeader(this.groupName);

        } else {
            //Regular case: Regular member is leaving.
            //Debug.getDebug().log("Regular member is leaving: " + member.getNickName());
        }

        this.proxyList.remove(member);



    }

    public ArrayList<ProxyInterface> electNewLeader(){
        ArrayList<ProxyInterface> highers = new ArrayList<>();
        for(Map.Entry proxy : proxyList.entrySet()){
            if(((String) proxy.getValue()).compareTo(proxyList.get(com)) < 0){
                highers.add((ProxyInterface) proxy.getKey());
            }
        }
        return highers;
    }

    public <T extends ProxyInterface> boolean compareProxy(T com) {
       return com.equals(this.myStub);
    }

    public <T extends ProxyInterface> boolean isLeader(T com) {
        return leader.equals(com);
    }

    public void removeGroup(String group) throws RemoteException {
        if(this.ns.removeGroup(group)) {
            Debug.getDebug().log("Group was removed");
        }
    }

}
