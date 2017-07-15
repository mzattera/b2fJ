package java.util;

/**
 * This class has been developed to parse strings with delimiters
 * 
 * @author Juan Antonio Brenha Moral
 */
public class StringTokenizer implements Enumeration<Object> {
	/**
	 * Delimiter string.
	 */
	//TODO support UTF-16, multiple delimiters, etc.
	//TODO StringTokeziner from JDK does not return empty tokens, this one does
	private String _delimiter;
//	private int total;
	private int currentPosition;
	private int nextPosition;
	private String s;

	/**
	 * Assigns the delimiter as ',' by default.
	 * 
	 * @param s String to be StringTokenizer
	 */
	public StringTokenizer(String s){
		//TODO wrong default delimiter
		this(s, ",");
	}

	/**
	 * The constructor
	 * 
	 * @param s
	 * @param delimiter
	 */
	public StringTokenizer(String s, String delimiter) {
		String character = s.substring(s.length()-1);
		if(character != delimiter){
			s += delimiter;
		}
		
		this._delimiter = delimiter;
		this.s = s;
//		this.total = s.length();
		this.currentPosition = 0;
		this.nextPosition = s.indexOf(_delimiter, currentPosition);
	}

	/**
	 *     Calculates the number of times that this tokenizer's nextToken method can be called before it generates an exception. The current position is not advanced.
	 * @return the number of tokens remaining in the string using the current delimiter set.
	 */
	public int countTokens() {
		int count = 0;
		int fromIndex = currentPosition;
		
		while(s.indexOf(_delimiter, fromIndex) != -1) {
			count++;
			fromIndex = s.indexOf(_delimiter, fromIndex) + 1;
		}
		
		return count;
	}
	
	/**
	 * Method used to know if exists new tokens
	 * 
	 * @return true iff there are more tokens
	 */
	public boolean hasMoreTokens(){
		return ((nextPosition != -1) && (currentPosition <= nextPosition));
	}
	
	/**
	 * Method implemented by interface requirement
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}
	
	/**
	 * 
	 * @return Next token
	 * @throws NoSuchElementException If there is no token left
	 */
	public String nextToken() throws NoSuchElementException{// 
		if (!hasMoreElements()){
			throw new NoSuchElementException();
		}

		String next = s.substring(currentPosition, nextPosition);

		currentPosition = nextPosition + 1;
		nextPosition = s.indexOf(_delimiter, currentPosition);

		return next;
	}

	/**
	 * Method implemented by interface requirement
	 */
	public Object nextElement(){
		Object ne = null;
		try{
			ne = nextToken();
		}catch(NoSuchElementException e){

		}
		return ne;
	}
}