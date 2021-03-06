package sintatico;

import java.util.Stack;

import comum.Constants;
import comum.Token;

import semantico.SemanticError;
import semantico.Semantico;
import lexico.LexicalError;
import lexico.Lexico;

public class Sintatico implements Constants
{
	
	private final Stack<Integer>	stack	= new Stack<>();
	private Token					currentToken;
	private Token					previousToken;
	private Lexico					scanner;
	private Semantico				semanticAnalyser;
	
	private static final boolean isTerminal(final int x)
	{
		return x < FIRST_NON_TERMINAL;
	}
	
	private static final boolean isNonTerminal(final int x)
	{
		return (x >= FIRST_NON_TERMINAL) && (x < FIRST_SEMANTIC_ACTION);
	}
	
	private static final boolean isSemanticAction(final int x)
	{
		return x >= FIRST_SEMANTIC_ACTION;
	}
	
	private boolean step() throws LexicalError, SyntaticError, SemanticError
	{
		if (currentToken == null)
		{
			int pos = 0;
			int line = 1;
			int column = 1;
			if (previousToken != null) {
				pos = previousToken.getPosition() + previousToken.getLexeme().length();
				line = previousToken.getLine();
				column = previousToken.getColumn();
			}
			
			currentToken = new Token(DOLLAR, "$", line, column, pos);
		}
		
		int x = stack.pop().intValue();
		int a = currentToken.getId();
		
		if (x == EPSILON)
		{
			return false;
		}
		else if (isTerminal(x))
		{
			if (x == a)
			{
				if (stack.empty()) {
					return true;
				} else
				{
					previousToken = currentToken;
					currentToken = scanner.nextToken();
					return false;
				}
			}
			else
			{
				throw new SyntaticError(getErrorMessage(x, currentToken), currentToken.getLine(), currentToken.getPosition());
			}
		}
		else if (isNonTerminal(x))
		{
			if (pushProduction(x, a)) {
				return false;
			} else {
				throw new SyntaticError(getErrorMessage(x, currentToken), currentToken.getLine(),
						currentToken.getPosition());
			}
		}
		else // isSemanticAction(x)
		{
			semanticAnalyser.executeAction(x - FIRST_SEMANTIC_ACTION, previousToken);
			return false;
		}
	}
	
	private String getErrorMessage(final int errorCode, Token token) {
		String msg;
		switch (errorCode) {
			default:
				String expected = token.getLexeme();				
				if (expected.equals("$")) {
					expected = "final de arquivo";
				} else if (token.getId() == 2) { // identificador
					expected = "identificador (" + expected + ")";
				}				
				
				msg = "Era esperado " + EXPECTED_SYMBOLS[errorCode] + ", encontrado: " + expected;
				break;
		}
		return msg;
	}
	
	private boolean pushProduction(final int topStack, final int tokenInput)
	{
		int p = PARSER_TABLE[topStack - FIRST_NON_TERMINAL][tokenInput - 1];
		if (p >= 0)
		{
			int[] production = PRODUCTIONS[p];
			// empilha a produ��o em ordem reversa
			for (int i = production.length - 1; i >= 0; i--)
			{
				stack.push(new Integer(production[i]));
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void parse(final Lexico scanner, final Semantico semanticAnalyser) throws LexicalError, SyntaticError, SemanticError
	{
		this.scanner = scanner;
		this.semanticAnalyser = semanticAnalyser;
		
		stack.clear();
		stack.push(new Integer(DOLLAR));
		stack.push(new Integer(START_SYMBOL));
		
		currentToken = scanner.nextToken();
		
		while (!step()) {
			;
		}
	}
}
