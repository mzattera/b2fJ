package js.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * A comparable ArrayList.
 */
class Packing implements Comparable
{
   Size array[];
   int thisSize;

   // Construct a fully functioning object
   Packing ()
   {
      array = new Size[CodePacker.list.size()];
      thisSize = 0;
   }

   // Add an element if there is room.
   boolean add (int i)
   {
      Size size = (Size) CodePacker.list.get(i);
      if (size.size + thisSize <= CodePacker.maxSize)
      {
         array[i] = size;
         thisSize += size.size;
         return true;
      }
      return false;
   }

   void remove (int i)
   {
      if (array[i] != null)
      {
         thisSize -= array[i].size;
         array[i] = null;
      }
   }

   int getSize ()
   {
      return thisSize;
   }

   /**
    * Pick a cross-over point within the index range. length/2 points before it
    * come from one array, the other come from the other array until the
    * capacity gets busted.
    */
   Packing breed (Packing other)
   {
      int crossOver = (int) (Math.random() * array.length);
      Packing offspring = new Packing();

      for (int i = 0; i < array.length / 2; i++)
      {
         if (array[(i + crossOver) % array.length] != null)
            offspring.add((i + crossOver) % array.length);

         if (array[(i + array.length / 2 + crossOver) % array.length] != null)
            offspring.add((i + array.length / 2 + crossOver) % array.length);
      }

      offspring.mutate();

      return offspring;
   }

   void mutate ()
   {
      for (int i = 0; i < array.length; i++)
      {
         if (Math.random() > 0.5)
         {
            if (array[i] == null)
               add(i);
            else
               remove(i);
         }
      }
   }

   public int compareTo (Object other)
   {
      return thisSize - ((Packing) other).thisSize;
   }
}

class Size
{
   int size;
   String file;

   Size (int size, String file)
   {
      this.size = size;
      this.file = file;
   }
}

public class CodePacker
{
   static ArrayList list;
   static int maxSize;

   /**
    * Shuffle the passed list.
    */
   static void shuffle ()
   {
      for (int i = 0; i < list.size() - 1; i++)
      {
         int selIndex = (int) (Math.random() * (list.size() - i) + i);
         Object selected = list.get(selIndex);
         list.set(selIndex, list.get(i));
         list.set(i, selected);
      }
   }

   /**
    * Randomly select a set of n elements from the passed list until the total
    * size of those elements would exceed the specified maximum size.
    * <P>
    * This is like shuffling the passed list and then selecting the first n
    * elements, except we stop shuffling once the exit criteria has been met.
    */
   static Packing select (int indices[])
   {
      Packing selection = new Packing();

      for (int i = 0; i < indices.length - 1; i++)
      {
         int selIndex = (int) (Math.random() * (indices.length - i) + i);
         int at = indices[selIndex];
         indices[selIndex] = indices[i];
         indices[i] = at;
         selection.add(i);
      }

      return selection;
   }

   static Packing pack ()
   {
      // Create an array of indices into the passed list:
      int indices[] = new int[list.size()];
      for (int i = 0; i < indices.length; i++)
      {
         indices[i] = i;
      }

      // Generate an initial random set of packings of size indices.length
      TreeSet populations = new TreeSet();
      for (int i = 0; i < indices.length; i++)
      {
         Packing packing = select(indices);

         // If we've already got a perfect packing, just return it.
         if (packing.thisSize == maxSize)
         {
            return packing;
         }

         populations.add(packing);
      }

      // Breed for a while.
      for (int i = 0; i < indices.length * 2; i++)
      {
         // Select two elements at random to breed
         Packing array[] = (Packing[]) populations
            .toArray(new Packing[populations.size()]);
         Packing offspring = array[i % array.length].breed(array[(int) (Math
            .random() * array.length)]);
         if (offspring.thisSize == maxSize)
            return offspring;

         // Add the new offspring and remove the least fit.
         if (populations.add(offspring))
            populations.remove(populations.first());
      }

      return (Packing) populations.last();
   }

   public static void main (String[] args) throws Exception
   {
      int extraStart = 0;
      String ldsFile = args[0];
      String mapFile = args[1];
      String outputFile = args[2];
      StringBuffer prologue = new StringBuffer();
      StringBuffer epilogue = new StringBuffer();
      StringBuffer current = prologue;
      list = new ArrayList();

      // Locate 'extra1'
      BufferedReader br = new BufferedReader(new FileReader(ldsFile));
      String s;
      while ((s = br.readLine()) != null)
      {
         current.append(s);
         current.append("\n");
         StringTokenizer st = new StringTokenizer(s);
         if (st.hasMoreTokens())
         {
            String token = st.nextToken();
            if (token.equals("extra1"))
            {
               st.nextToken(); // :
               st.nextToken(); // o
               st.nextToken(); // =
               token = st.nextToken();
               extraStart = Integer.decode(token).intValue();
               st.nextToken(); // ,
               st.nextToken(); // l
               st.nextToken(); // =
               token = st.nextToken();
               maxSize = Integer.decode(token).intValue();
            }
         }
         if (s.indexOf("__extra_start") != -1)
            current = epilogue;
      }
      br.close();

      // Read in the size of all of the .o's.
      br = new BufferedReader(new FileReader(mapFile));
      while ((s = br.readLine()) != null)
      {
         StringTokenizer st = new StringTokenizer(s);
         if (st.hasMoreTokens())
         {
            String token = st.nextToken();
            if (token.equals(".text"))
            {
               // Discard the next token
               st.nextToken();

               // Next token is the size in hex;
               int size = Integer.decode(st.nextToken()).intValue();

               // Next token is the name of the object file
               String file = st.nextToken();

               if (!file.equals("main.o") && file.indexOf("init.o") == -1)
               {
                  // Is this a file in a library?
                  int lparenI = file.indexOf("(");
                  // GNU LD doesn't make life easy...
                  if (lparenI != -1)
                  {
                     int rparenI = file.indexOf(")");
                     file = "*" + file.substring(lparenI + 1, rparenI);
                  }

                  list.add(new Size(size, file));
               }
            }
         }
      }

      Packing packing = pack();

      System.out.println("Segment size=" + maxSize + ", packing size="
         + packing.thisSize);
      PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
      pw.print(prologue);
      for (int i = 0; i < packing.array.length; i++)
      {
         if (packing.array[i] != null)
         {
            pw.println(packing.array[i].file + "(.text)");
         }
      }
      pw.print(epilogue);
      pw.close();
   }
}