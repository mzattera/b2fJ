package java.lang;

/**
 * All exceptions and errors extend this class.
 */
public class Throwable
{
	private String _message;
  public Throwable() {
	  _message = "";
  }

  public Throwable(String message) {
	  _message = message;
  }

  public String getMessage()
  {
    return _message;
  }
}
