package org.smx.captcha.impl;

import java.util.Stack;

public class MathExpresionParser {
	private Stack opstack;
	private String query;	
	private int current_index;
	private final int EOF=0;
	private final String TOKEN_NUM = "NUM"; // 1234	
	private final String TOKEN_IDENT = "IDENT"; // identifier
	
	private final int TYPE_OPERAND=1;
	private final int TYPE_OPERATOR=2; // + - * /
	
	
//	Signs
	private final String TOKEN_ADD = "ADD";
	private final String TOKEN_MINUS = "MIN";
	private final String TOKEN_MULTIPLY = "MUL";
	private final String TOKEN_DIVIDE = "DIV";
	
//	Precedence	
	
	private final int PREC_TOKEN_TOKEN_NUM = 0;
	
	private final int PREC_TOKEN_ADD = 1;
	private final int PREC_TOKEN_MINUS = 1;
	private final int PREC_TOKEN_MULTIPLY = 3; 
	private final int PREC_TOKEN_DIVIDE = 2;
	
	
	
	public MathExpresionParser(String expresion){
		query=expresion==null?"":expresion ;
		opstack=new Stack();
	}
	
	public void parse(){
		char ch;
		while ((ch = read_ch()) != EOF) {
			String token = "";
			switch (ch) {
			case ' ': {	//blanks		
				break;
			}
			case '+': {
				insert(TOKEN_ADD, "+");
				break;
			}
			
			case '-': {
				insert(TOKEN_MINUS, "-");
				break;
			}
			case '*': {
				insert(TOKEN_MULTIPLY , "*");
				break;
			}
			case '/': {
				insert(TOKEN_DIVIDE , "/");
				break;
			}
			default:
				if (Character.isDigit(ch)) {
					do{
						token += ch;
						ch = read_ch();
						//Put back the last char if its not the end of stear 
						if(!Character.isDigit(ch) && ch  != EOF){
							put_back();
						}
					} while (Character.isDigit(ch));
					
					insert(TOKEN_NUM, token);					
					continue;
				}
			
			
			if (Character.isLetter(ch)) {
				do {
					token += ch;
					ch = read_ch();
				} while (Character.isLetter(ch));
				insert(TOKEN_IDENT, token);
			}
			
			
			break;
			}
		}
	}
	public char read_ch() {
		if (current_index == query.length())
			return EOF;
		char ch = query.charAt(current_index);
		current_index++;
		return ch;
	}
	
	public void put_back() {
		current_index--;
	}
	
	private void insert(String token_type, String token) {
		Token topStack=new Token();
		console(token_type + " is " + token);
		Token  tk= new  Token();
		tk.value=token;
		
		if(token_type.equals(TOKEN_NUM)){
			tk.type=TYPE_OPERAND;
		}else{
			tk.type=TYPE_OPERATOR;
		}
		
		if(token_type.equals(TOKEN_ADD)){
			tk.precedence=PREC_TOKEN_ADD;
		}else if(token_type.equals(TOKEN_MINUS )){
			tk.precedence=PREC_TOKEN_MINUS;
		}else if(token_type.equals(TOKEN_MULTIPLY  )){
			tk.precedence=PREC_TOKEN_MULTIPLY;
		}else if(token_type.equals(TOKEN_DIVIDE)){
			tk.precedence=PREC_TOKEN_DIVIDE;
		}else if(token_type.equals(TOKEN_NUM)){
			tk.precedence=PREC_TOKEN_TOKEN_NUM;
		}
		
		if(tk.type == TYPE_OPERAND){
			opstack.push(tk);
		}
		if(tk.type == TYPE_OPERATOR && opstack.isEmpty()){
			opstack.push(tk);
		}if(tk.type == TYPE_OPERATOR && !opstack.isEmpty()){
			topStack=(Token)opstack.pop();
			console(tk.precedence +" ==" + topStack.precedence );
			
			if(tk.precedence > topStack.precedence){
				
			}
		}
	}
	
	public void eval(){
		console(opstack);
	
	/*	while(!opstack.isEmpty()){
			console(opstack.pop());
		}
	*/
	}
	
	public void console(Object o) {
		System.out.println(o);
	}
	
	public static void main(String[] args) {
		MathExpresionParser app= new MathExpresionParser("10+2*4/1");
		app.parse();
		app.eval();
	}
	
	public class Token{
		int precedence=0;
		int type=0; // 1= Operant 2=Operand
		String value="";
		public String toString(){
			return " [ t="+type+" p="+precedence+" v="+value+" ] ";
		}
	}
}

