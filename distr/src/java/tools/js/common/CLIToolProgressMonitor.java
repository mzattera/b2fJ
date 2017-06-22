package js.common;

/**
 * Simple implementation of ToolProgressMonitor with output to System.out.
 */
public class CLIToolProgressMonitor implements ToolProgressMonitor
{
   private boolean _verbose = false;

   /*
    * (non-Javadoc)
    * 
    * @see js.tools.ToolProgressMonitor#operation(java.lang.String)
    */
   public void operation (String message)
   {
      assert message != null: "Precondition: message != null";
      System.out.println(message);
   }

   /*
    * (non-Javadoc)
    * 
    * @see js.tools.ToolProgressMonitor#log(java.lang.String)
    */
   public void log (String message) {
	   if(!_verbose)
		   return;
      assert message != null: "Precondition: message != null";
      System.out.println(message);
   }

   /*
    * (non-Javadoc)
    * 
    * @see js.tools.ToolProgressMonitor#progress(int)
    */
   public void progress (int progress)
   {
      assert progress >= 0 && progress <= 1000: "Precondition: progress >= 0 && progress <= 1000";
      System.out.print("\r  " + (progress/10) + "%\r");
      if (progress >= 1000)
      {
         System.out.println();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see js.common.ToolProgressMonitor#isCanceled()
    */
   public boolean isCanceled ()
   {
      return Thread.currentThread().isInterrupted();
   }

   /**
    * Be verbose?
    */
   public void setVerbose (boolean verbose)
   {
      _verbose = verbose;
   }
}