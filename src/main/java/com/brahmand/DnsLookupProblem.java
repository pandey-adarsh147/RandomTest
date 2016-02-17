package com.brahmand;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by adarshpandey on 12/31/15.
 */
public class DnsLookupProblem {

    public static void main(String... args) {
        InetAddress[] addresses = null;
        String host = "rideeze.com";
        try {
            addresses = InetAddress.getAllByName(host);
            InetAddress[] result = new InetAddress[addresses.length];
            int i = 0;
            for( InetAddress addr : addresses ) {
                if(  addr instanceof Inet4Address) {
                    System.out.println("IPv4 : " + addr.getHostAddress());
                } else {
                    System.out.println("IPv6 : " + addr.getHostAddress());
                }
            }
        } catch( UnknownHostException e ) {
//            addresses = fallbackResolver(host);
//            if( addresses == null || addresses.length == 0 ) throw e;
        }
        /*if( addresses.length == 1 && host.endsWith(".spin.de") &&
                (addresses[0].isSiteLocalAddress() || addresses[0].isLinkLocalAddress())) {
            InetAddress[] tmpAddresses = fallbackResolver(host);
            if( tmpAddresses != null && tmpAddresses.length > 0 ) return tmpAddresses;
        }*/

    }
}
