package br.upe.ecomp.client;


import java.util.LinkedList;




public class CodeHamming {
	
	public static final  String PARIDADE = "?";
	
	private LinkedList<String> _binary;
	private int _sizeOfParity; 
	private LinkedList<String> _parity; //
	
	public CodeHamming(char... binary){
		
		_binary = CodeHamming.BinaryToLinkedList(binary);
		_sizeOfParity = calculateSizeOfParity(_binary.size());
		bitsOfSindrome();

	}
	
	public CodeHamming(LinkedList<String> binary){
		
		int i=0;
		int cont = binary.size() - ((int)Math.pow(2, i)-1)-1;
		LinkedList<String> parity = new LinkedList<String>();
		LinkedList<String> tempBinary = new LinkedList<String>();
		
		//for(;cont<binary.size(); cont = binary.size() - (int)Math.pow(2, i)-1){
		for(;cont>=0; cont = binary.size() - ((int)Math.pow(2, i)-1)-1){
			i++;
			parity.add(binary.get(cont));
			binary.set(cont,PARIDADE);
		}
		
		for (int j = 0; j < binary.size(); j++) {
			tempBinary.add(binary.get(j));
		}
		
		i=0;
		while(i<binary.size()){
			if(binary.get(i)==PARIDADE){
			
				binary.remove(i);
				
			}else{
				i+=1;	  	
		    }
		}
		
		_binary = binary;
		_sizeOfParity = calculateSizeOfParity(binary.size());
		
		int pos = compararCodeHamming(parity);
		
		for(String str : tempBinary) {
			System.out.print(str);
		}
		System.out.println();
		System.out.println(pos);
		// Inverter Bit das POS
		
		if(pos==0){
			
			System.out.println("Não houve erro!");
		
		}else{
			
			// INVERTER BIT da POS
			tempBinary.set(tempBinary.size()-(pos), invertBit(tempBinary.get(tempBinary.size()-(pos))));
		}
		
		_binary = tempBinary;
		i=0;
		while(i<_binary.size()){
			if(_binary.get(i)==PARIDADE){
			
				_binary.remove(i);
				
			}else{
				i+=1;	  	
		    }
		}
		for(String str : _binary) {
			System.out.print(str);
		}
		System.out.println();
	}
	
	public String invertBit(String str) {
		return str.equalsIgnoreCase("0") ? "1" : "0";
	}
	
	private int compararCodeHamming(LinkedList<String> parityA){
		
		LinkedList<String> invertedPar = new LinkedList<String>();
		
		bitsOfSindrome();
		_parity = calculateParity();
		
		for (int i = parityA.size()-1; i >= 0; i--) {
			invertedPar.add(parityA.get(i));
		}
		

		LinkedList<String> sindrome = new LinkedList<String>();
		int xor;
	
		for(int i=0;i<_parity.size();i++){
			xor = Integer.parseInt(_parity.get(i)) ^ Integer.parseInt(invertedPar.get(i));  
			sindrome.add(0,String.valueOf(xor));		
		}
		
		for(String s : sindrome){
			System.out.print(s);
		}
		System.out.println();
		return binaryToInt(sindrome);
	}
	
	private int binaryToInt(LinkedList<String> linked){
		int sum=0;
		
		for(int i=0;i<linked.size();i++){
			
			sum += (int)Math.pow(2,i)*Integer.parseInt(linked.get(linked.size()-(i+1)));
			
		}
		return sum;
	}
	
	private LinkedList<String> calculateParity() {
		
		int _flag=0;
		int tempBin;
		int xor=0;
		String temp;
		LinkedList<String> parity = new LinkedList<String>();
		LinkedList<String> invertedBinary = new LinkedList<String>();
		
		for (int i = _binary.size()-1; i >= 0; i--) {
			invertedBinary.add(_binary.get(i));
		}
		
		for(int i=0;i<_binary.size();i++){
		
			if(_flag>=_sizeOfParity){	
				break;
			}
			
			if(invertedBinary.get(i) == PARIDADE){
				
				xor=0;
	
				for(int cont=0;cont<invertedBinary.size();cont++){
				
					if (cont != i && invertedBinary.get(cont)!= PARIDADE) {
						
						tempBin = Integer.parseInt(invertedBinary.get(cont));
						temp = Integer.toBinaryString(cont+1);
						
						for (int j = 0; temp.length() < _sizeOfParity;j++ ){
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
		
		for(String s : invertedBinary){
			System.out.print(s);
		}
		
		System.out.println();
		
		return invertedBinary;
	}
	
	private int calculateSizeOfParity(int M){
		int _i=0;
		
		// 2^K-1>=M+K
		
		for(;_i<M;_i++){
			
			if (Math.pow(2,_i)-1>=M+_i)
				break;
		}
		
		return _i;
	}
	
	
	public LinkedList<String> calculateCodeHamming(){
				   
		_parity = calculateParity();

		int parityCount = 0;
		for (int j =0; j <_binary.size() ; j++) {
			if (_binary.get(j) == PARIDADE) {
				_binary.set(j, String.valueOf(_parity.get(parityCount)));
				parityCount++;
			}
		}

		for(String s : _binary){
			System.out.print(s);
		}
		
		return _binary;
	}
	
	private void bitsOfSindrome(){
		
		int cont=0,i=0;
		for(String s : _binary){
			System.out.print(s);
		}
		
		System.out.println();

		while(cont<_sizeOfParity){
			//MUDEI
			_binary.add(_binary.size()-((int)Math.pow(2,i)-1),PARIDADE);
			i+=1;
			cont+=1;	
			
		}
		
		for(String s : _binary){
			System.out.print(s);
		}
		
		System.out.println();
	}
	
	public static LinkedList<String> BinaryToLinkedList(char... arrayChar){
		
		LinkedList<String> link = new LinkedList<String>();
		
		for(int i=0;i<arrayChar.length;i++){
			
			link.add(arrayChar[i]+"");
		}
		
		return link;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		LinkedList<String> teste = new LinkedList<String>();
		
		char [] b = {'0','0','1','1','1','0','0','1'};
					
		//Binary b = new Binary('1','1','0','1');
		//1101
		
		CodeHamming code = new CodeHamming(b);
		teste = code.calculateCodeHamming();
		
		//CodeHamming.compararCodeHamming();
		teste.clear();
		String a[] = {"0","0","1","1","0","1","1","0","1","1","1","1"};
		//System.out.println("s");
		
		for(String str: a)
			teste.add(str);
		
		CodeHamming code2 = new CodeHamming(teste);
	}
}
