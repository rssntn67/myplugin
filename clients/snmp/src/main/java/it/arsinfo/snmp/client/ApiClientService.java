package it.arsinfo.snmp.client;


import java.io.IOException;

public class ApiClientService {

    private final AdvancedSnmpSet snmpsetter;


    public ApiClientService(AdvancedSnmpSet advancedSnmpSet) {
        this.snmpsetter = advancedSnmpSet;
    }

    public boolean set(String oid, String value) {
            return snmpsetter.setString(oid, value);
    }
}
