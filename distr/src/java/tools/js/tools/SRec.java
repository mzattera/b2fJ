package js.tools;

import java.io.IOException;

/**
 * Respresentation of an H8300 S-record
 */
public class SRec
{

   private static final int SREC_DATA_SIZE = 256;

   // Valid hex characters and their values

   private static final byte[] ctab =
   {
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
      8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1,
   };

   // Address lengths for all valid record types

   private static final int[] ltab =
   {
      4, 4, 6, 8, 0, 4, 0, 8, 6, 4
   };

   public byte type;
   public int addr;
   public byte count;
   public byte[] data;

   /**
    * Return a single hex digit
    */
   private static byte c1 (char[] l, int p)
   {
      return (byte) ctab[l[p]];
   }

   /**
    * Return byte for hex pair
    */
   private static byte c2 (char[] l, int p)
   {
      return (byte) ((c1(l, p) << 4) | c1(l, p + 1));
   }

   /**
    * Decode an S record
    * 
    * A valid S record: - is at least 4 bytes long - byte 0 is "S" - all other
    * charcters are hex - byte 1 is the type (0-9 with some gaps) - bytes 2 and
    * 3 are the count of the the data hex pairs - the data follows an address
    * which is 4, 6, or 8 bytes hex pairs depending on the type - the last hex
    * pair is a checksum
    */
   public SRec (char[] line, int len) throws IOException
   {
      int pos, rest, alen, sum;

      if (line == null || len > line.length)
         throw new IOException("Null String error");

      if (len < 4 || line[0] != 'S')
         throw new IOException("Invalid Header");

      for (pos = 1; pos < len; pos++)
         if (c1(line, pos) < 0)
            throw new IOException("Invalid character");

      type = c1(line, 1);
      rest = c2(line, 2);

      if (type > 9)
         throw new IOException("Invalid type");

      alen = ltab[type];

      if (alen == 0)
         throw new IOException("Invalid type");

      if (len < alen + 6)
         throw new IOException("Line too short");

      if (rest > alen + SREC_DATA_SIZE + 2)
         throw new IOException("Line too long");

      if (len != rest * 2 + 4)
         throw new IOException("Length error");

      sum = rest;

      // Decode the address

      addr = 0;
      for (pos = 4; pos < alen + 4; pos += 2)
      {
         byte value = c2(line, pos);
         int val = (value < 0? value + 256 : value);
         addr = (addr << 8) | val;
         sum += val;
      }

      // Decode the data

      count = (byte) (rest - (alen / 2) - 1);

      data = new byte[count];

      int i = 0;
      for (pos = alen + 4; pos < len - 2; pos += 2, i++)
      {
         byte value = c2(line, pos);
         data[i] = value;
         sum += value;
      }

      sum += c2(line, pos);

      if ((sum & 0xff) != 0xff)
         throw new IOException("Checksum error");
   }
}

