package br.upe.ecomp.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.upe.ecomp.lib.CodeHamming;
import br.upe.ecomp.lib.MySocket;

public class TelaClient extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField inputMessage;
	private JTextField inputError;
	private JButton sendButton;
	private JLabel lblInputMessage;
	private JLabel lblInputError;
	
	public TelaClient(String nomeDaTela){
		super(nomeDaTela);
		setSize(300, 90);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel panelBottom = new  JPanel();
		lblInputMessage = new JLabel("Mensagem:");
		panelBottom.add(lblInputMessage);
		inputMessage = new JTextField(15);
		panelBottom.add(inputMessage);
		lblInputError = new JLabel("Bit de erro:");
		panelBottom.add(lblInputError);
		inputError = new JTextField(8);
		panelBottom.add(inputError);
		sendButton = new JButton("Enviar");
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//here you put the event button
				eventClick(inputMessage.getText(), inputError.getText());
			}
		});
		
		panelBottom.add(sendButton);
		add(panelBottom);
		setVisible(true);
		
		
	}
	public static void main(String[] args) throws Exception {
		new TelaClient("Janela Client");
	}
	
	public void eventClick(String inputMessage, String inputError) {
		
		String input = inputMessage;
		LinkedList<String> send = null;
		LinkedList<String> parity = null;
	
		int error = Integer.parseInt(inputError) - 1;
		int sizeParity = 0;
		
		CodeHamming codeHam = new CodeHamming();
		// Transforma a string recebida numa lista com cada caractere.
		send = CodeHamming.BinaryToLinkedList(input.toCharArray());
		// Calcula a quantidade de bits de paridade.
		sizeParity = codeHam.calculateSizeOfParity(send.size());
		// Adiciona as interrogações na lista de dados.
		send = codeHam.addCharsParity(send);
		// Calcula os bits de paridade a serem adicionados.
		parity = codeHam.calculateParity(send, sizeParity);
		// Retira as interrogações.
		send = CodeHamming.removeParity(send);

		// Caso tenha erro, inverte o bit escolhido.
		if (error >= 0 && error < send.size()) {
			send.set(error, CodeHamming.invertBit(send.get(error)));
		}
		// Adiciona os bits de paridade na lista de dados.
		send = codeHam.addCharsParity(send, parity);
		
		// Transforma a lista a ser enviada numa String.
		input = CodeHamming.ConvertLinkedListToString(send);
		
		// Tenta enviar o pacote.
		MySocket sock = new MySocket();
		try {
			sock.SendUDPPacket(0, 12345, 54321, "127.0.0.1", "127.0.0.1", input.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

