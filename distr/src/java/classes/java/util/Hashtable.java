package java.util;

/**
 * Maps keys to objects. It has a fixed-size table, so don't
 * expect it to scale well.
 */
public class Hashtable<K, V>
{
	private static final int TABLE_SIZE = 32;
	private Object[] iTable;

	public Hashtable()
	{
		this.iTable = new Object[TABLE_SIZE];
	}

	public synchronized V get (K aKey)
	{
		Object pElement = iTable[getTableIndex(aKey)];
		if (pElement != null)
		{
			KeyValuePair<K,V> pKeyValuePair = getKeyValuePair(pElement, aKey);
			if (pKeyValuePair != null)
			{
				return pKeyValuePair.iValue;
			}
		}
		return null;
	}
  
	@SuppressWarnings("unchecked")
	public synchronized V put (K aKey, V aValue)
	{
		V r;
		int pIndex = getTableIndex(aKey);    
		Object pElement = iTable[pIndex];
		if (pElement == null)
		{
			r = null;
			iTable[pIndex] = new KeyValuePair<K,V>(aKey, aValue);
		}
		else
		{
			KeyValuePair<K,V> pKeyValuePair = getKeyValuePair (pElement, aKey);
			if (pKeyValuePair != null)
			{
				r = pKeyValuePair.iValue;
				pKeyValuePair.iValue = aValue;
			}
			else
			{
				r = null;
				pKeyValuePair = new KeyValuePair<K,V>(aKey, aValue);
				if (pElement instanceof Vector)
					((Vector<KeyValuePair<K,V>>)pElement).addElement(pKeyValuePair);
				else
				{
					Vector<KeyValuePair<K,V>> pVector = new Vector<KeyValuePair<K,V>>();
					pVector.addElement((KeyValuePair<K,V>)pElement);
					pVector.addElement(pKeyValuePair);
					iTable[pIndex] = pVector;
				}
			}
		}
		return r;
	}

	/**
	 * 
	 * @return An Enumeration object containing all keys for this Hashtable
	 */
	public Enumeration<K> keys()
	{
		return new Enumeration<K>()
		{
			int cur = 0;
			/*
			 * Our Hashtable stores more than one object in iTable if
			 * getTableIndex() hashes it to the same slot. When this happens it
			 * stores multiple items in a Vector. In this case, nextElement()
			 * needs to keep track of which vector it is at via curVector.
			 */
			int curVector = 0;

			public boolean hasMoreElements()
			{
				/*
				 * Difficult to work with our current Hashtable code due to
				 * iTable gaps.
				 */
				while (cur < TABLE_SIZE)
				{
					Object element = iTable[cur];
					if (element != null)
					{
						if (element instanceof Vector<?>)
						{
							Vector<?> v = (Vector<?>) element;
							if (curVector < v.size())
								return true;

							curVector = 0;
						}
						else
						{
							return true;
						}
					}
					cur++;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			public K nextElement()
			{
				/*
				 * Difficult to work with our current Hashtable code due to
				 * iTable gaps.
				 */
				Object r = null;
				while (cur < TABLE_SIZE)
				{
					Object element = iTable[cur];
					if (element != null)
					{
						if (element instanceof Vector)
						{
							Vector<KeyValuePair<K,V>> v = (Vector<KeyValuePair<K,V>>) element;
							if (curVector < v.size())
							{
								KeyValuePair<K,V> kvp = v.elementAt(curVector);
								r = kvp.iKey;
								curVector++;
								break;
							}

							curVector = 0;
						}
						else
						{
							KeyValuePair<K,V> kvp = (KeyValuePair<K,V>) element;
							r = kvp.iKey;
							cur++;
							break;
						}
					}
					cur++;
				}
				return (K) r;
			}
		};
	}
  
	@SuppressWarnings("unchecked")
	private KeyValuePair<K,V> getKeyValuePair(Object aPivot, Object aKey)
	{
		if (aPivot instanceof Vector<?>)
		{
			Vector<KeyValuePair<K,V>> pVec = (Vector<KeyValuePair<K,V>>) aPivot;
			int pSize = pVec.size();
			for (int i = 0; i < pSize; i++)
			{
				KeyValuePair<K,V> pPair = pVec.elementAt(i);
				if (aKey.equals(pPair.iKey))
				{
					return pPair;
				}
			}
		}
		else
		{
			// Not a Vector, must be a lone KeyValuePair
			KeyValuePair<K,V> pPair = (KeyValuePair<K,V>)aPivot;
			if (aKey.equals(pPair.iKey))
			{
				return pPair;
			}
		}
		return null;
	}
  
	private int getTableIndex (Object aKey)
	{
		int pHash = aKey.hashCode() % TABLE_SIZE;
		if (pHash < 0)
			return pHash + TABLE_SIZE;
		return pHash;
	}

	private static class KeyValuePair<K, V>
	{ 
		K iKey;
		V iValue;
		
		public KeyValuePair(K key, V value)
		{
			this.iKey = key;
			this.iValue = value;
		}
	}
}
