package br.upe.ecomp.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jpcap.packet.UDPPacket;


public class Tela extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField inputMessage;
	private static JTextArea outputMessages;
	private JButton sendButton;
	
	public Tela(String nomeDaTela){
		super(nomeDaTela);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		outputMessages = new JTextArea();
		add(outputMessages);
		JPanel panelBottom = new  JPanel();
		inputMessage = new JTextField(15);
		panelBottom.add(inputMessage);
		sendButton = new JButton("Enviar");
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//here you put the event button
				String input = "Client diz: " + inputMessage.getText();
				inputMessage.setText("");
				MySocket sock = new MySocket();
				try {
					sock.SendUDPPacket(0, 12345, 54321, "127.0.0.1", "127.0.0.1", input.getBytes());
				} catch (Exception e) {

				}
			}
		});
		
		panelBottom.add(sendButton);
		add(panelBottom,BorderLayout.SOUTH);
		setVisible(true);
		
		
	}
	public static void main(String[] args) throws Exception {
		new Tela("Janela Client");
		
		run();
	}
	
	public static void run() throws Exception {
		MySocket sock = new MySocket();
		UDPPacket pack = null;
		
		while(true) {
			pack = sock.GetUDPPackage(0, 20, "127.0.0.1");
			outputMessages.setText(outputMessages.getText() + new String(pack.data) + '\n');
		}
	}
	
}

