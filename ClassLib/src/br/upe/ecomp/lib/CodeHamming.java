package br.upe.ecomp.lib;

import java.util.LinkedList;

public class CodeHamming {
	
	public static final String PARIDADE = "?";
	
	public CodeHamming(){

	}
	
	/**
	 * Calcula o tamanho da paridade dado o tamanho da mensagem.
	 * @param M - Tamanho da mensagem.
	 * @return Tamanho da paridade.
	 */
	public int calculateSizeOfParity(int M){
		int _i=0;
		
		// 2^K-1>=M+K
		
		for(;_i<M;_i++){
			
			if (Math.pow(2,_i)-1>=M+_i)
				break;
		}
		
		return _i;
	}
	
	/**
	 * Pega a paridade de um binário passado.
	 * @param binary - Binário que contem a paridade.
	 * @return Bits de paridade
	 */
	public LinkedList<String> getParity(LinkedList<String> binary) {
		
		int i=0;
		int cont = binary.size() - ((int)Math.pow(2, i)-1)-1;
		LinkedList<String> parity = new LinkedList<String>();
		
		for(;cont>=0; cont = binary.size() - ((int)Math.pow(2, i)-1)-1){
			i++;
			parity.add(binary.get(cont));
		}
		
		return parity;
	}
	
	/**
	 * Adiciona as paridades nas posições corretas de acordo com o tamanho do binário.
	 * Até então as paridades são '?'.
	 * @param binary - Valor binário sem paridades.
	 * @return Valor binário com caracteres de paridade. 
	 */
	public LinkedList<String> addCharsParity(LinkedList<String> binary){
		
		int i = 0;
		int sizeParity = calculateSizeOfParity(binary.size());
		
		while(i<sizeParity){

			binary.add(binary.size()-((int)Math.pow(2,i)-1),PARIDADE);
			i+=1;
			
		}
		
		return binary;
	}
	
	public LinkedList<String> addCharsParity(LinkedList<String> binary, LinkedList<String> parity){
		
		int i = 0;
		int sizeParity = calculateSizeOfParity(binary.size());
		
		while(i<sizeParity){

			binary.add(binary.size()-((int)Math.pow(2,i)-1),parity.get(i));
			i+=1;
			
		}
		
		return binary;
	}
	
	/**
	 * Adiciona os caracteres da paridade onde existe ?(Caractere de paridade)
	 * @param binary - Binário com ? nas paridades.
	 * @return Binário com valores no lugar das ?.
	 */
	public LinkedList<String> addParity(LinkedList<String> binary, int sizeParity){
		
		LinkedList<String> result = new LinkedList<String>();
		LinkedList<String> parity = calculateParity(binary, sizeParity);

		for (int j = 0; j < binary.size(); j++) {
			result.add(binary.get(j));
		}
		
		int parityCount = 0;
		for (int j =0; j <result.size() ; j++) {
			if (result.get(j) == PARIDADE) {
				result.set(j, String.valueOf(parity.get(parityCount)));
				parityCount++;
			}
		}

		return result;
	}
	
	/**
	 * Calcula a posição que foi recebida com erro.
	 * @param parityA - Paridade calculada a partir do dado com erro.
	 * @param correctParity - Paridade correta.
	 * @return Posição com erro.
	 */
	public int compareCodeHamming(LinkedList<String> parityA, LinkedList<String> correctParity){
		
		//LinkedList<String> invertedPar = new LinkedList<String>();
		
//		for (int i = parityA.size()-1; i >= 0; i--) {
//			invertedPar.add(parityA.get(i));
//		}

		LinkedList<String> sindrome = new LinkedList<String>();
		int xor;
	
		for(int i=0;i<correctParity.size();i++){
			xor = Integer.parseInt(correctParity.get(i)) ^ Integer.parseInt(parityA.get(i));  
			sindrome.add(String.valueOf(xor));		
		}
		
		return binaryToInt(sindrome);
	}
	
	/**
	 * Calcula a paridade dada a mensagem.
	 * @param binary - Binário com ? no lugar das paridades.
	 * @return Os bits de paridade.
	 */
	public LinkedList<String> calculateParity(LinkedList<String> binary, int sizeParity) {
		
		int _flag = 0;
		int tempBin;
		int xor = 0;
		
		String temp;
		
		LinkedList<String> parity = new LinkedList<String>();
		LinkedList<String> invertedBinary = new LinkedList<String>();
		
		for (int i = binary.size()-1; i >= 0; i--) {
			invertedBinary.add(binary.get(i));
		}
		
		for(int i=0;i<invertedBinary.size();i++){
		
			if(_flag>=sizeParity){	
				break;
			}
			
			if(invertedBinary.get(i) == PARIDADE){
				
				xor=0;
	
				for(int cont=0;cont<invertedBinary.size();cont++){
				
					if (cont != i && invertedBinary.get(cont)!= PARIDADE) {
						
						tempBin = Integer.parseInt(invertedBinary.get(cont));
						temp = Integer.toBinaryString(cont+1);
						
						for (int j = 0; temp.length() < sizeParity;j++ ){
							temp="0"+temp;
						}
						
						if (temp.charAt(temp.length()-(_flag+1)) == '1' ) {
							xor = xor ^ tempBin;
						}
					}	
				}
				
				parity.add(String.valueOf(xor));
				_flag+=1;
				
			}
		}
		
		invertedBinary.clear();
		
		for (int i = parity.size()-1; i >= 0; i--) {
			invertedBinary.add(parity.get(i));
		}		
		
		return invertedBinary;
	}
	
	/* -- STATIC METHODS -- */
	public static LinkedList<String> BinaryToLinkedList(char... arrayChar){
		
		LinkedList<String> link = new LinkedList<String>();
		
		for(int i=0;i<arrayChar.length;i++){
			
			link.add(arrayChar[i]+"");
		}
		
		return link;
	}

	/**
	 * Remove os bits de paridade.
	 * @param binary - Binário com os bits de paridade.
	 * @return Binário sem os bits de paridade.
	 */
	public static LinkedList<String> removeParity(LinkedList<String> binary) {
		
		int i = 0;
		int cont = binary.size() - ((int)Math.pow(2, i)-1)-1;
		LinkedList<String> result = new LinkedList<String>();
		
		for (int j = 0; j < binary.size(); j++) {
			result.add(binary.get(j));
		}
		
		for(;cont>=0; cont = result.size() - ((int)Math.pow(2, i)-1)-1){
			i++;
			result.set(cont,PARIDADE);
		}
		
		i=0;
		while(i<result.size()){
			if(result.get(i)==PARIDADE){
				result.remove(i);
			}else{
				i+=1;	  	
		    }
		}
		
		return result;
	}
	
	/**
	 * Invert o bit passado como parâmetro.
	 * @param str - Apenas um bit.
	 * @return Bit invertido.
	 */
	public static String invertBit(String str) {
		return str.equalsIgnoreCase("0") ? "1" : "0";
	}
	
	/**
	 * Converte linkedList para String.
	 * @param linkedList - LinkedList de String a ser convertida.
	 * @return String convertida.
	 */
	public static String ConvertLinkedListToString(LinkedList<String> linkedList) {
		
		String result = "";
		
		for (int j = 0; j < linkedList.size(); j++) {
			result += linkedList.get(j);
		}
		
		return result;
	}
	
	/**
	 * Converte um valor binário para inteiro.
	 * @param linked - Valor em binário.
	 * @return Valor em inteiro.
	 */
	public static int binaryToInt(LinkedList<String> linked){
		int sum=0;
		
		for(int i=0;i<linked.size();i++){
			
			sum += (int)Math.pow(2,i)*Integer.parseInt(linked.get(linked.size()-(i+1)));
			
		}
		return sum;
	}
	
	public static void main(String[] args) {
		
		CodeHamming codeHam = new CodeHamming();
		
		int error = 1;
		error--;
		LinkedList<String> teste = new LinkedList<String>();
		//String[] str = {"0", "0", "1", "1", "1", "0", "0"}; // Client
		String[] str = {"0","0","1","1","1","1","0","0","1","0","1","0"}; // Server erro no bit 1
		for (int i = 0; i < str.length; i++) {
			teste.add(str[i]);
		}

//		int sizeParity = codeHam.calculateSizeOfParity(teste.size());
//		
//		teste = codeHam.addCharsParity(teste);
//		LinkedList<String> parity = codeHam.calculateParity(teste, sizeParity);
//		teste = CodeHamming.removeParity(teste);
//		teste.set(error, CodeHamming.invertBit(teste.get(error)));
//		teste = codeHam.addCharsParity(teste, parity);
		
		LinkedList<String> correctParity = codeHam.getParity(teste);
		for (String s : correctParity) {
			System.out.print(s);
		}
		System.out.println();
		teste = CodeHamming.removeParity(teste);
		for (String s : teste) {
			System.out.print(s);
		}
		System.out.println();
		int sizeParity = codeHam.calculateSizeOfParity(teste.size());
		teste = codeHam.addCharsParity(teste);
		LinkedList<String> erroneousParity = codeHam.calculateParity(teste, sizeParity);
		for (String s : erroneousParity) {
			System.out.print(s);
		}
		System.out.println();
		int position = codeHam.compareCodeHamming(erroneousParity, correctParity);
		System.out.println(position);
		teste.set(teste.size() - (position), CodeHamming.invertBit(teste.get(teste.size() - (position))));
		teste = CodeHamming.removeParity(teste);
		for (String s : teste) {
			System.out.print(s);
		}
		System.out.println();
	}
}
