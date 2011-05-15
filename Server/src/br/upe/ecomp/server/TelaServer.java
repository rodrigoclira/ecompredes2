package br.upe.ecomp.server;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jpcap.packet.UDPPacket;
import br.upe.ecomp.lib.CodeHamming;
import br.upe.ecomp.lib.MySocket;


public class TelaServer extends JFrame{

	private static final long serialVersionUID = 1L;
	private static JTextArea outputMessages;
	
	public TelaServer(String nomeDaTela){
		super(nomeDaTela);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		outputMessages = new JTextArea();
		add(outputMessages);
		JPanel panelBottom = new  JPanel();
		
		add(panelBottom,BorderLayout.NORTH);
		setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
		new TelaServer("Janela Server");
		
		run();
	}
	
	public static void run(){
		MySocket sock = new MySocket();
		UDPPacket pack = null;
		LinkedList<String> captor = new LinkedList<String>();
		CodeHamming codeHam = new CodeHamming();
		String output = "";
		
		while(true) {
			output = "";
			// Aguarda receber um pacote não nulo.
			try {
				pack = sock.GetUDPPackage(0, 20, "127.0.0.1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			captor = CodeHamming.BinaryToLinkedList((new String(pack.data)).toCharArray());
			output += "Pacote recebido: ";
			output += CodeHamming.ConvertLinkedListToString(captor);
			
			// Pega a paridade correta, a que veio junto com a mensagem.
			LinkedList<String> correctParity = codeHam.getParity(captor);
			output += "\n";
			output += "Paridade: ";
			output += CodeHamming.ConvertLinkedListToString(correctParity);
			
			// Remove a paridade da mensagem recebida.
			captor = CodeHamming.removeParity(captor);
			output += "\n";
			output += "Mensagem incorreta: ";
			output += CodeHamming.ConvertLinkedListToString(captor);
			
			// Calcula o tamanho da paridade para o valor recebido.
			int sizeParity = codeHam.calculateSizeOfParity(captor.size());
			captor = codeHam.addCharsParity(captor);
			
			// Calcula a paridade errada, ela vai ser calculada com a mensagem com um bit invertido.
			LinkedList<String> erroneousParity = codeHam.calculateParity(captor, sizeParity);
			output += "\n";
			output += "Paridade incorreta: ";
			output += CodeHamming.ConvertLinkedListToString(erroneousParity);
			
			// Compara as duas paridades para descobrir onde ocorreu o erro.
			int position = codeHam.compareCodeHamming(erroneousParity, correctParity);
			if (position > 0) {
				// Caso tenha ocorrido erro, inverte o bit na posição calculada.
				captor.set(captor.size() - (position), CodeHamming.invertBit(captor.get(captor.size() - (position))));
			}
			captor = CodeHamming.removeParity(captor);
			output += "\n\n";
			output += "Mensagem correta: ";
			output += CodeHamming.ConvertLinkedListToString(captor);
			outputMessages.setText(output);
		}
	}
	
}

