package js.tinyvm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import js.common.CLIToolProgressMonitor;
import js.common.ToolProgressMonitor;
import js.tinyvm.util.TinyVMCommandLineParser;
import js.tools.LejosdlException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Tiny VM.
 */
public class TinyVM extends TinyVMTool
{
	private TinyVMCommandLineParser fParser;
	
   /**
    * Main entry point for command line usage.
    * 
    * @param args command line
    */
   public static void main (String[] args)
   {
      try
      {
         TinyVM tinyVM = new TinyVM(new CLIToolProgressMonitor());
         tinyVM.start(args);
      }
      catch (TinyVMException e)
      {
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }

   /**
    * Constructor.
    */
   public TinyVM (ToolProgressMonitor monitor)
   {
      super(monitor);
      fParser = new TinyVMCommandLineParser();
   }

   /**
    * Execute tiny vm.
    * 
    * @param args command line
    * @throws TinyVMException
    */
   public void start (String[] args) throws TinyVMException
   {
      assert args != null: "Precondition: args != null";

      CommandLine commandLine = fParser.parse(args);

      // options
      boolean verbose = commandLine.hasOption("v");
      String classpath = commandLine.getOptionValue("cp");
      String output = commandLine.getOptionValue("o");
      boolean all = commandLine.hasOption("a");
      boolean bigEndian = "be".equalsIgnoreCase(commandLine
         .getOptionValue("wo"));

      // files
      String[] classes = commandLine.getArgs();

      ((CLIToolProgressMonitor) getProgressMonitor()).setVerbose(verbose);

      OutputStream stream = null;
      try
      {
         stream = output == null
            ? (OutputStream) System.out
            : (OutputStream) new FileOutputStream(output);
         link(classpath, classes, all, stream, bigEndian);
      }
      catch (FileNotFoundException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
      finally
      {
         if (stream instanceof FileOutputStream)
         {
            try
            {
               stream.close();
            }
            catch (IOException e)
            {
               throw new TinyVMException(e);
            }
         }
      }
   }

}