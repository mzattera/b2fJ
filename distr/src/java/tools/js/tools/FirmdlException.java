package js.tools;

import js.common.ToolException;

/**
 * Firmdl exception.
 */
public class FirmdlException extends ToolException
{
   /**
    * @param message
    */
   public FirmdlException (String message)
   {
      super(message);
   }

   /**
    * @param cause
    */
   public FirmdlException (Throwable cause)
   {
      super(cause);
   }

   /**
    * @param message
    * @param cause
    */
   public FirmdlException (String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = 3544953268330443576L;
}