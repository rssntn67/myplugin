package it.arsinfo.snmp.client;

import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthGeneric;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivacyGeneric;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpAgent {
    private Address nmsIP;
    private String user;
    private String securityName;
    private String privacyPassword;
    private String authorizationPassword;
    private AuthGeneric authProtocol;
    private PrivacyGeneric privacyProtocol;
    private String protocol;

    private long timeOut = 1000L;
    private int noOfRetries = 2;

    private Snmp snmp;
    private UserTarget target;

    SnmpAgent(String ip, String protocol, int snmpPort, String username,
                String securityName, String privacyPassword, String authPassowrd,
                AuthGeneric authProtocol, PrivacyGeneric privacyProtocol) {

        nmsIP = GenericAddress.parse(protocol + ":" + ip + "/" + snmpPort);
        System.out.println("NMS IP set : " + nmsIP.toString());

        this.protocol = protocol;
        this.user = username;
        this.securityName = securityName;
        this.privacyPassword = privacyPassword;
        this.authorizationPassword = authPassowrd;
        this.authProtocol = authProtocol;
        this.privacyProtocol = privacyProtocol;
    }

    public static void main(String[] args) {

        SnmpAgent agent = new SnmpAgent("nms/server-ip", "udp", 162,
                "abhinav", "abhinav", "myprivpass", "myauthpass",
                new AuthSHA(), new PrivAES128());
        try {
            agent.startAgent();
            ResponseEvent response = agent
                    .snmpGetOperation(SnmpConstants.sysName);
            System.out.println(response.getResponse());
            // Similarly you can perform set operation.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAgent() throws IOException {
        if (snmp == null) {
            TransportMapping<? extends TransportIpAddress> transport = null;
            if (protocol.equalsIgnoreCase("udp")) {
                System.out.println("UDP Protocol selected.");
                transport = new DefaultUdpTransportMapping();
            } else {
                System.out.println("TCP Protocol selected.");
                transport = new DefaultTcpTransportMapping();
            }
            snmp = new Snmp(transport);
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
                    MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            transport.listen();
            snmp.getUSM().addUser(
                    new OctetString(user),
                    new UsmUser(new OctetString(securityName), authProtocol
                            .getID(), new OctetString(authorizationPassword),
                            privacyProtocol.getID(), new OctetString(
                            privacyPassword)));

            target = createUserTarget();
        }

    }

    public ResponseEvent snmpSetOperation(VariableBinding[] vars)
            throws IOException {
        PDU setPdu = new ScopedPDU();
        for (VariableBinding variableBinding : vars) {
            setPdu.add(variableBinding);
        }
        return snmp.send(setPdu, target);
    }

    public ResponseEvent snmpGetOperation(OID oid) throws IOException {

        PDU getPdu = new ScopedPDU();
        getPdu.add(new VariableBinding(oid));
        return snmp.get(getPdu, target);
    }

    private UserTarget createUserTarget() {
        UserTarget target = new UserTarget();
        target.setAddress(nmsIP);
        target.setRetries(noOfRetries);
        target.setTimeout(timeOut);
        target.setVersion(3);
        target.setSecurityLevel(3);
        target.setSecurityName(new OctetString(securityName));
        return target;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public int getNoOfRetries() {
        return noOfRetries;
    }

    public void setNoOfRetries(int noOfRetries) {
        this.noOfRetries = noOfRetries;
    }
}
