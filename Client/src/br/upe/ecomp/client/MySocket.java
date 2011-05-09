/**
 * Funções de envio e recebimento de pacotes utilizando JPCap.
 */

package br.upe.ecomp.client;

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
	
	public UDPPacket GetUDPPackage(int indexDevice, int timeout_ms, String ip) throws Exception {
		
		// Obtem interfaces de rede.
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		
		JpcapCaptor captor = JpcapCaptor.openDevice(devices[indexDevice], 65535, false, timeout_ms);

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
	
	public void SendUDPPacket(int indexDevice, int sourcePort, int destinePort, String ipSource,
			String ipDestine, byte[] data) throws Exception {
		
		// Obtem interfaces de rede.
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		
		JpcapSender sender = JpcapSender.openDevice(devices[indexDevice]);
		
		// Cria pacote a ser enviado.
		UDPPacket p = new UDPPacket(sourcePort, destinePort);
		p.setIPv4Parameter(0,false,false,false,0,false,false,false,0,1010101,100,IPPacket.IPPROTO_UDP,
				  InetAddress.getByName(ipSource),InetAddress.getByName(ipDestine));
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
