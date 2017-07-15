package java.lang;

public class InternalError extends VirtualMachineError
{
	public InternalError()
	{
		super();
	}

	public InternalError(String message)
	{
		super(message);
	}
}
