package js.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class ListReader
{
   public static Vector loadStrings (File aFile)
   {
      Vector pVec = new Vector();
      if (!aFile.exists())
         return pVec;
      try
      {
         BufferedReader pReader = new BufferedReader(new FileReader(aFile));
         String pLine;
         while ((pLine = pReader.readLine()) != null)
         {
            pLine = pLine.trim();
            if (pLine.startsWith("#") || pLine.equals(""))
               continue;
            pVec.addElement(pLine);
         }
         pReader.close();
      }
      catch (IOException aE)
      {
         aE.printStackTrace();
      }
      return pVec;
   }
}