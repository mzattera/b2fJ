package js.tools;

import js.common.ToolException;

/**
 * Send exception.
 */
public class SendException extends ToolException
{
   /**
    * @param message
    */
   public SendException (String message)
   {
      super(message);
   }

   /**
    * @param cause
    */
   public SendException (Throwable cause)
   {
      super(cause);
   }

   /**
    * @param message
    * @param cause
    */
   public SendException (String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = 3257570585662796336L;
}