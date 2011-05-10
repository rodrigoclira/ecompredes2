package br.upe.ecomp.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

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
				int pos = 3;
				LinkedList<String> binary = new LinkedList<String>();
				String input = inputMessage.getText();
				inputMessage.setText("");
				int i=0;
			
    			int error=3;
    			int realPos=0;
    			
				CodeHamming codeHam = new CodeHamming(input.toCharArray());
				LinkedList<String> send = codeHam.calculateCodeHamming();
				
				//
				for (int j = 0; j < send.size(); j++) {
					binary.add(send.get(j));
				}

				int cont = binary.size() - ((int)Math.pow(2, i)-1)-1;
				//for(;cont<binary.size(); cont = binary.size() - (int)Math.pow(2, i)-1){
				for(;cont>=0; cont = binary.size() - ((int)Math.pow(2, i)-1)-1){
					i++;
					binary.set(cont,CodeHamming.PARIDADE);
				}
				
				i=binary.size()-1;
				while(error>0){
					
					if(!binary.get(i).equalsIgnoreCase(CodeHamming.PARIDADE)){
						error-=1;
					}
					realPos+=1;			
					i--; 
				}
				
				System.out.println(realPos);
				System.out.println("HJASHJK\n");
				for (String string : binary) {
					System.out.print(string);
				}
				error=realPos;
				// Agradeço a graça alcançada. 
				System.out.println();
				String bitInvertido = codeHam.invertBit(send.get(send.size()-(error)));
				send.set(send.size()-(error), bitInvertido);
				input = "";
				for (String str : send) {
					System.out.print(str);
				}
				for (i = 0; i < send.size(); i++) {
					input += send.get(i);
				}
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

