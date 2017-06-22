package js.tinyvm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.IOUtilities;

/**
 * Record table.
 */
public class RecordTable extends WritableDataWithOffset
{
   /**
    * Descriptive name of this record table.
    */
   private String _name;

   /**
    * Allow duplicates in table?
    */
   private boolean _duplicates;

   /**
    * Align output?
    */
   private boolean _align;

   /**
    * List of all writable element.
    */
   private ArrayList _list;

   /**
    * Length of output.
    */
   private int _length;

   /**
    * Constructor.
    * 
    * @param name description of this record table
    * @param allowDuplicates allow duplicates?
    * @param align align output?
    */
   public RecordTable (String name, boolean allowDuplicates, boolean align)
   {
      assert name != null: "Precondition: name != null";

      _name = name;
      _duplicates = allowDuplicates;
      _align = align;
      _list = new ArrayList();
      _length = -1;
   }

   //
   // public interface
   //

   /**
    * Add a writable element.
    * 
    * @param element
    */
   public void add (WritableData element)
   {
      assert element != null: "Precondition: element != null";

      if (_duplicates || !_list.contains(element))
      {
         _list.add(element);
      }
   }

   /**
    * Get writable element by its index.
    * 
    * @param index index
    */
   public WritableData get (int index)
   {
      assert index >= 0 && index < size(): "Precondition: index >= 0 && index < size()";

      WritableData result = (WritableData) _list.get(index);

      assert result != null: "Postconditon: result != null";
      return result;
   }

   /**
    * Get index of writable element.
    * 
    * @param element
    * @return index or -1 if not found
    */
   public int indexOf (WritableData element)
   {
      assert element != null: "Precondition: element != null";

      return _list.indexOf(element);
   }

   /**
    * Iterator.
    */
   public Iterator iterator ()
   {
      Iterator result = _list.iterator();

      assert result != null: "Postconditon: result != null";
      return result;
   }

   /**
    * Number of entries.
    */
   public int size ()
   {
      return _list.size();
   }

   //
   // Writable interface
   //

   /**
    * Dump all elements.
    */
   public void dump (IByteWriter writer) throws TinyVMException
   {
      assert writer != null: "Precondition: writer != null";

      try
      {
         boolean pDoVerify = TinyVMConstants.VERIFY_LEVEL > 0;
         for (Iterator iter = _list.iterator(); iter.hasNext();)
         {
            WritableData pData = (WritableData) iter.next();

            int pLength = pData.getLength();
            int pPrevSize = writer.offset();

            pData.dump(writer);

            if (pDoVerify)
            {
               if (writer.offset() != pPrevSize + pLength)
               {
                  if (pData instanceof RecordTable)
                  {
                     // _logger.log(Level.SEVERE, "Aligned sequence: "
                     //    + ((RecordTable) pData)._align);
                  }
                  throw new TinyVMException("Bug RT-1: Written="
                     + (writer.offset() - pPrevSize) + " Length=" + pLength
                     + " Class=" + pData.getClass().getName());
               }
            }
         }
         if (_align)
         {
            IOUtilities.writePadding(writer, 2);
         }
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   /**
    * Length.
    */
   public int getLength () throws TinyVMException
   {
      if (_length == -1)
      {
         _length = 0;
         for (Iterator iter = _list.iterator(); iter.hasNext();)
         {
            _length += ((WritableData) iter.next()).getLength();
         }

         // _logger.log(Level.INFO, "length of " + _name + ": " + _length);

         if (_align)
         {
            _length = IOUtilities.adjustedSize(_length, 2);
         }
      }

      return _length;
   }

   /**
    * Init offset of record table and all of its elements.
    */
   public void initOffset (int startOffset) throws TinyVMException
   {
      // _logger.log(Level.INFO, "init offset of " + _name + ": " +
      // startOffset);

      super.initOffset(startOffset);

      for (Iterator iter = _list.iterator(); iter.hasNext();)
      {
         WritableData element = (WritableData) iter.next();
         if (element instanceof WritableDataWithOffset)
         {
            ((WritableDataWithOffset) element).initOffset(startOffset);
         }
         startOffset += element.getLength();
      }
   }

   // private static final Logger _logger = Logger.getLogger("TinyVM");
}

