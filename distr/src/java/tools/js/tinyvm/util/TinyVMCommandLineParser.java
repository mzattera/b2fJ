package js.tinyvm.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import js.common.ToolProgressMonitor;
import js.tinyvm.TinyVMException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * CommandLineParser
 */
public class TinyVMCommandLineParser {

   /**
    * Parse commandline.
    * 
    * @param args command line
    * @throws TinyVMException
    */
   public CommandLine parse (String[] args) throws TinyVMException
   {
      assert args != null: "Precondition: args != null";

      Options options = new Options();
      options.addOption("v", "verbose", false,
         "print class and signature information");
      options.addOption("h", "help", false, "help");
      Option classpathOption = new Option("cp", "classpath", true, "classpath");
      classpathOption.setArgName("classpath");
      options.addOption(classpathOption);
      Option outputOption = new Option("o", "output", true,
         "dump binary to file");
      outputOption.setArgName("binary");
      options.addOption(outputOption);
      options.addOption("a", "all", false, "do not filter classes");
      Option writeOrderOption = new Option("wo", "writeorder", true,
         "write order (BE or LE)");
      writeOrderOption.setArgName("write order");
      options.addOption(writeOrderOption);

      Option deviceOption = new Option("tty", "device", true,"device used (USB, COM1, etc)");
      deviceOption.setArgName("device");
      options.addOption(deviceOption);
      
      CommandLine result;
      try
      {
         try
         {
            result = new GnuParser().parse(options, args);
         }
         catch (ParseException e)
         {
            throw new TinyVMException(e.getMessage(), e);
         }

         if (result.hasOption("h"))
         {
            throw new TinyVMException("Help:");
         }

         if (!result.hasOption("cp"))
         {
            throw new TinyVMException("No classpath defined");
         }

         if (!result.hasOption("wo"))
         {
            throw new TinyVMException("No write order specified");
         }
         String writeOrder = result.getOptionValue("wo").toLowerCase();
         if (!"be".equals(writeOrder) && !"le".equals(writeOrder))
         {
            throw new TinyVMException("Wrong write order: " + writeOrder);
         }

         if (result.getArgs().length == 0)
         {
            throw new TinyVMException("No classes specified");
         }
      }
      catch (TinyVMException e)
      {
         StringWriter writer = new StringWriter();
         PrintWriter printWriter = new PrintWriter(writer);
         printWriter.println(e.getMessage());

         String usage = getClass().getName() + " [options] class1[,class2,...]";
         // TODO check format parameters
         new HelpFormatter().printHelp(printWriter, 80, usage.toString(), null,
            options, 0, 2, null);

         throw new TinyVMException(writer.toString());
      }

      assert result != null: "Postconditon: result != null";
      return result;
   }
}