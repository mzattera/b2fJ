package js.common;

/**
 * Abstract tool.
 */
public class AbstractTool
{
   private ToolProgressMonitor _progress;

   /**
    * Constructor.
    * 
    * @param listener tool progress listener
    */
   public AbstractTool (ToolProgressMonitor listener)
   {
      _progress = listener;
   }

   //
   // protected interface
   //

   /**
    * Progress listener.
    */
   protected ToolProgressMonitor getProgressMonitor ()
   {
      assert _progress != null: "Postconditon: result != null";
      return _progress;
   }
}