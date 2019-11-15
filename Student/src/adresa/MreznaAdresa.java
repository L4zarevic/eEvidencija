package adresa;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MreznaAdresa {
    
    public static String getMac() {
        String macAdresa = null;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            
            byte[] mac = network.getHardwareAddress();
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAdresa = sb.toString();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(MreznaAdresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return macAdresa;
    }
    
    public static String getIpv4() {
        String ipv4Adresa = null;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipv4Adresa = ip.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MreznaAdresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ipv4Adresa;
    }
    
}
