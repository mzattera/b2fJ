package js.common;

/**
 * Dummy progress listener.
 */
public class NullToolProgressMonitor implements ToolProgressMonitor
{
   /*
    * (non-Javadoc)
    * 
    * @see js.common.ToolProgressMonitor#operation(java.lang.String)
    */
   public void operation (String message)
   {}

   /*
    * (non-Javadoc)
    * 
    * @see js.common.ToolProgressMonitor#log(java.lang.String)
    */
   public void log (String message)
   {}

   /*
    * (non-Javadoc)
    * 
    * @see js.common.ToolProgressMonitor#progress(int)
    */
   public void progress (int progress)
   {}

   /*
    * (non-Javadoc)
    * 
    * @see js.common.ToolProgressMonitor#isCanceled()
    */
   public boolean isCanceled ()
   {
      return false;
   }
}