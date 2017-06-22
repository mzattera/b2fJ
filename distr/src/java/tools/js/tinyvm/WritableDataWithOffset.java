package js.tinyvm;

public abstract class WritableDataWithOffset implements WritableData
{
   /**
    * Offset.
    */
   private int _offset = -1;

   /**
    * Init offset.
    * 
    * @param startOffset start offset
    * @throws TinyVMException if offset is not correct
    */
   public void initOffset (int startOffset) throws TinyVMException
   {
      assert startOffset != -1: "Precondition: aStart != -1";

      if (startOffset < 0 || startOffset > 0xFFFF)
      {
         throw new TinyVMException("Offset out of range (" + startOffset + ")");
      }

      _offset = startOffset;
   }

   /**
    * Offset.
    * 
    * @throws TinyVMException if offset is not correct
    */
   public int getOffset () throws TinyVMException
   {
      assert _offset != -1: "Precondition: _offset != -1";

      if (_offset <= 0 || _offset > 0xFFFF)
      {
         throw new TinyVMException("Offset out of range (" + _offset + ")");
      }

      assert _offset >= 0 && _offset <= 0xFFFF: "Postcondition: result >=0 && result <= 0xFFFF";
      return _offset;
   }
}