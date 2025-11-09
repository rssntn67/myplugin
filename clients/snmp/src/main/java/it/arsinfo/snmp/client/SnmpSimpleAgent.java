package it.arsinfo.snmp.client;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpSimpleAgent {

    private final Snmp snmp;
    private Address targetAddress;
    private CommunityTarget target;

    public SnmpSimpleAgent(String ipAddress, String community) throws IOException {
        // Initialize SNMP session
        TransportMapping<?> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();

        // Create target
        targetAddress = GenericAddress.parse("udp:" + ipAddress + "/161");
        target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
    }

    /**
     * Perform SNMP SET operation
     * @param oid The OID to set
     * @param value The value to set
     * @return true if successful, false otherwise
     */
    public boolean set(String oid, Variable value) {
        try {
            // Create PDU
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid), value));
            pdu.setType(PDU.SET);

            // Send SET request
            ResponseEvent responseEvent = snmp.set(pdu, target);

            if (responseEvent != null) {
                PDU response = responseEvent.getResponse();
                if (response != null) {
                    int errorStatus = response.getErrorStatus();
                    if (errorStatus == PDU.noError) {
                        System.out.println("SET operation successful");
                        return true;
                    } else {
                        System.out.println("Error in SET operation: " +
                                response.getErrorStatusText());
                    }
                } else {
                    System.out.println("No response received");
                }
            }
        } catch (IOException e) {
            System.err.println("Error during SNMP SET: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Example method to set different types of values
     */
    public void demonstrateSetOperations() {
        System.out.println("Demonstrating SNMP SET operations...");

        // Set an Integer value
        boolean success1 = set("1.3.6.1.2.1.1.7.0", new Integer32(3));
        System.out.println("Set sysServices to 3: " + (success1 ? "Success" : "Failed"));

        // Set a String value
        boolean success2 = set("1.3.6.1.2.1.1.4.0", new OctetString("admin@example.com"));
        System.out.println("Set sysContact: " + (success2 ? "Success" : "Failed"));

        // Set a Counter32 value
        boolean success3 = set("1.3.6.1.2.1.2.2.1.10.1", new Counter32(1000));
        System.out.println("Set ifInOctets: " + (success3 ? "Success" : "Failed"));

        // Set an IP Address
        boolean success4 = set("1.3.6.1.2.1.4.20.1.1.192.168.1.100",
                new IpAddress("192.168.1.100"));
        System.out.println("Set IP address: " + (success4 ? "Success" : "Failed"));

        // Set a TimeTicks value
        boolean success5 = set("1.3.6.1.2.1.1.3.0", new TimeTicks(360000));
        System.out.println("Set sysUpTime: " + (success5 ? "Success" : "Failed"));
    }

    /**
     * Close the SNMP session
     */
    public void close() {
        try {
            if (snmp != null) {
                snmp.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing SNMP session: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SnmpSimpleAgent snmpSet = null;

        try {
            // Replace with your SNMP agent details
            String ipAddress = "192.168.1.1";  // Target device IP
            String community = "private";      // Write community string

            snmpSet = new SnmpSimpleAgent(ipAddress, community);
            snmpSet.demonstrateSetOperations();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (snmpSet != null) {
                snmpSet.close();
            }
        }
    }
}