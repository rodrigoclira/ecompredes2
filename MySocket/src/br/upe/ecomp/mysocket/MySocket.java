/**
 * Funções de envio e recebimento de pacotes utilizando JPCap.
 */

package br.upe.ecomp.mysocket;

import java.net.InetAddress;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.UDPPacket;

/**
 * @author Sergio Ribeiro
 *
 */
public class MySocket {

	// Default constructor.
	public MySocket() {
		
	}
	
	public UDPPacket GetUDPPackage(NetworkInterface device,String ip) throws Exception {
		
		JpcapCaptor captor = JpcapCaptor.openDevice(device, 65535, false, 20);

		// Seta filtro para capturar pacotes.
		captor.setFilter("net " + ip, true);
		
		UDPPacket p = null;
		
		// Espera receber um pacote.
		while (true) {
			p = (UDPPacket)captor.getPacket();
			if (p != null) {
				break;
			}
		}
		
		// Retorna o pacote recebido.
		return p;
	}
	
	public void SendUDPPacket(NetworkInterface device, String ipSource, String ipDestination,byte[] data) throws Exception {
								
		JpcapSender sender = JpcapSender.openDevice(device);
		
		// Cria pacote a ser enviado.
		UDPPacket p = new UDPPacket(12345, 54321);
		p.setIPv4Parameter(0,false,false,false,0,false,false,false,0,1010101,100,IPPacket.IPPROTO_TCP,
				  InetAddress.getByName(ipSource),InetAddress.getByName(ipDestination));
		p.data = data;
		
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_IP;
		ether.src_mac=new byte[]{(byte)0,(byte)1,(byte)2,(byte)3,(byte)4,(byte)5};
		ether.dst_mac=new byte[]{(byte)0,(byte)6,(byte)7,(byte)8,(byte)9,(byte)10};
		p.datalink = ether;
		
		// Envia o pacote.
		sender.sendPacket(p);
	}

}
