package java.util;
/*
* $Log$
* Revision 1.5  2003/08/17 14:59:42  mpscholz
* enhanced Vector
* added Stack and Queue and associated exception classes
*
*/

/**
 * A dynamic array.
 */

public class Vector
{
  //private Object[] iElements;
  //private int iSize;

  protected Object[] elementData;

  protected int capacityIncrement;

  protected int elementCount;



  public Vector(int initialCapacity, int capacityIncrement)

  {

    if (initialCapacity < 0) initialCapacity = 0;

    elementData = new Object[initialCapacity];

    this.capacityIncrement = capacityIncrement;

    elementCount = 0;

  }



  public Vector(int initialCapacity)

  {

    this(initialCapacity,0);

  }





  public Vector()

  {

    this(7);

  }





  public synchronized void addElement (Object aObj)

  {

    //int pOldSize = elementCount;

    //setSize (pOldSize + 1);

    ensureCapacityHelper(elementCount + 1);

    elementData[elementCount] = aObj;

    elementCount++;

  }



  public int capacity()

  {

    return elementData.length;

  }





  public void clear()

  {

    removeAllElements();

  }





  public synchronized Object elementAt (int aIndex)

  {

    if ((aIndex >= 0) && (aIndex < elementCount))

    {

      return elementData[aIndex];

    }else{

      throw new ArrayIndexOutOfBoundsException();

    }

  }



  public synchronized void ensureCapacity(int minCapacity)

  {

    ensureCapacityHelper(minCapacity);

  }





  //internal unsynchronized method for better performance

  private void ensureCapacityHelper(int minCapacity)

  {

    if (elementData.length < minCapacity)

    {

      int pNewCapacity;

      if (capacityIncrement > 0)

      {

        pNewCapacity = elementData.length + capacityIncrement;

      }else{

        pNewCapacity = elementData.length * 2;

      }

      if (pNewCapacity < minCapacity) pNewCapacity = minCapacity;

      Object oldData[] = elementData;

      elementData = new Object[pNewCapacity];

      arraycopy(oldData,0, elementData, 0, elementCount);



    }

  }





  public boolean equals(Object aObj)

  {

    return super.equals(aObj);

  }





  public synchronized int indexOf(Object aObj)

  {

    return indexOf(aObj, 0);

  }





  public synchronized int indexOf(Object aObj, int aIndex)

  {

    if (aObj == null)

    {

      for (int i = aIndex; i < elementCount; i++)

      {

        if (elementData[i] == null) return i;

      }

    }else{

      for (int i = aIndex; i < elementCount; i++)

      {

        if (elementData[i].equals(aObj)) return i;

      }

    }

    return -1;

  }



  public synchronized void insertElementAt (Object aObj, int aIndex)

  {

    if (aIndex > elementCount)

    {

      throw new ArrayIndexOutOfBoundsException();

    }else{

      ensureCapacityHelper(elementCount + 1);

      for (int i = elementCount; i > aIndex; i--)

      {

        elementData[i] = elementData[i - 1];

      }

      //arraycopy(elementData, aIndex, elementData, aIndex + 1, elementCount - aIndex);

      elementData[aIndex] = aObj;

      elementCount++;

    }

  }





  public boolean isEmpty()

  {

    return (elementCount == 0);

  }

	/////////////////////////////////////////////////////
  	/**
	* delivers the index of the last occurrence of the object
	* @param anObject the object 
	* @return the index of the last occurrence of the object
	* or -1, if object is not found
	*/
	public synchronized int lastIndexOf(Object anObject) {
		return lastIndexOf(anObject,elementCount-1);
	} // lastIndexOf()

	/////////////////////////////////////////////////////
	/**
	* delivers the index of the last occurrence of the object
	* starting from some index
	* @param anObject the object
	* @param anIndex the starting index 
	* @return the index of the last occurrence of the object
	* or -1, if object is not found
	* @throws ArrayIndexOutOfBoundsException
	*/
	public synchronized int lastIndexOf(Object anObject, 
		int anIndex) throws ArrayIndexOutOfBoundsException {
		// valid index?
		if(anIndex>=elementCount)
			throw new ArrayIndexOutOfBoundsException();
		// null object?
		if(anObject==null) {
			// find last null object
			for(int i=anIndex;i>=0;i--)
				if(elementData[i]==null)
					return i;
		} else {
			// find last equal object
		for (int i=anIndex;i>=0;i--)
			if(anObject.equals(elementData[i]))
				return i;
		} // else
		// not found
		return -1;
	} // lastIndexOf()





  public void removeAllElements()

  {

    for (int i = 0; i < elementCount; i++) elementData[i] = null;

    elementCount = 0;

  }



  public synchronized boolean removeElement (Object aObj)

  {

    int index = indexOf(aObj);

    if (index >= 0)

    {

      removeElementAt(index);

      return true;

    }else{

      return false;

    }

  }



  public synchronized void removeElementAt (int aIndex)

  {

    if ((aIndex >= 0) && (aIndex < elementCount))

    {

      int j = elementCount - aIndex - 1;

      if (j > 0)

      {

        arraycopy(elementData, aIndex + 1, elementData, aIndex, j);

      }

      elementCount--;

      elementData[elementCount] = null;

    }else{

      throw new ArrayIndexOutOfBoundsException();

    }

  }



  public synchronized void setElementAt (Object aObj, int aIndex)

  {

    if (aIndex >= elementCount)

    {

      throw new ArrayIndexOutOfBoundsException();

    }else{

      elementData[aIndex] = aObj;

    }

  }









  public synchronized void setSize (int aSize)

  {

    //elementCount = aSize;

    if (elementCount < aSize)

    {

      /*int pNewCapacity;

      if (capacityIncrement > 0) {

        pNewCapacity = elementData.length + capacityIncrement;

      }else{

        pNewCapacity = elementData.length * 2;

      }



      if (pNewCapacity < aSize) pNewCapacity = aSize;

      Object[] pNewElements = new Object[pNewCapacity];

      arraycopy (elementData, 0, pNewElements, 0, elementData.length);

      elementData = pNewElements;*/

      ensureCapacityHelper(aSize);

    }else{

      for (int i = aSize; i < elementCount; i++) elementData[i] = null;

    }

    elementCount = aSize;

  }



  public synchronized int size()

  {

    return elementCount;

  }



  public synchronized Object[] toArray()

  {

    Object[] result = new Object[elementCount];

    arraycopy(elementData, 0, result, 0, elementCount);

    return result;

  }





  /*public String toString() {

    if (elementCount < 1) {

      return "[]";

    }else{

      String str = "[";

        for (int i = 0; i < elementCount - 1;i++) str += String.valueOf(elementData[i]) + ", ";

      str += String.valueOf(elementData[elementCount - 1]) + "]";

      return str;

    }

  }*/



  public synchronized void trimToSize()

  {

    if (elementCount < elementData.length)

    {

      Object oldData[] = elementData;

      elementData = new Object[elementCount];

      arraycopy(oldData, 0, elementData, 0, elementCount);

    }

  }

  static void arraycopy (Object[] src, int srcoffset, Object[] dest, int destoffset, int length)

    {

      for (int i = 0; i < length; i++)

        dest[i + destoffset] = src[i + srcoffset];

  }

  //private native void arraycopy (Object aSource, int aOffset1, Object aDest, int aOffset2, int aLength);

}

