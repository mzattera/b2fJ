package java.lang;
import java.lang.annotation.Annotation;

import lejos.nxt.VM;

/**
 * Not fully functional. 
 */
public class Class<T>
{
    // Note the following fields are mapped on to read only flash entries
    // held within the VM. They should not be changed. New fields should not
    // be added to this class unless changes are also made to the VM.
    // The CIA fields are really a C union (with different contents for
    // (C)lasses (I)nterfaces and (A)rrays), but it is not obvious how to
    // implement a union in Java so instead we use shared fields.
    private short CIAData1;
    private short CIAData2;
    private byte CIACnt1;
    private byte CIACnt2;
    private byte parentClass;
    private byte flags;
    
    @SuppressWarnings("unchecked")
	public <U> Class<? extends U> asSubclass(Class<U> cls)
    {
    	if (!cls.isAssignableFrom(this))
    		throw new ClassCastException();
    	
    	return (Class<? extends U>)this;
    }
    
	@SuppressWarnings("unchecked")
	public T cast(Object o)
	{
		if (!this.isInstance(o))
			throw new ClassCastException();
			
		return (T)o;
	}
	
	/**
	 * Always return false.
	 * @return True if asserts are enabled false if they are disabled.
	 */
	public boolean desiredAssertionStatus()
	{
		return (VM.getVMOptions() & VM.VM_ASSERT) != 0;
	}
	
	/**
	 * @exception ClassNotFoundException Thrown always in TinyVM.
	 */
	@SuppressWarnings("unused")
	public static Class<?> forName (String aName)
		throws ClassNotFoundException
	{
		throw new ClassNotFoundException();
	}
	
	public Class<?> getComponentType()
	{
		if (!this.isArray())
			return null;
		
		return VM.getClass(this.CIAData2 & 0xFF);
	}
	
	public Class<?>[] getInterfaces()
	{
        // Note this is not a correct implementation. We will return all
        // of the interfaces that are implemented by this class and any super
        // classes, we will also return any super interfaces that are also
        // implemented. We could possible fix up some of this, but I'm not
        // sure it is worth the effort. Andy
        int interfaceCnt = 0;
        int i = 0;
        // First work out the max number of interfaces
        for(;;)
        {
            Class<?> cls = VM.getClass(i++);
            if (cls == null) break;
            if (cls.isInterface() && VM.isAssignable(this, cls)) 
                interfaceCnt++;
        }
        Class<?>[] interfaces = new Class<?>[interfaceCnt];
        // now get them
        i = 0;
        interfaceCnt = 0;
        for(;;)
        {
            Class<?> cls = VM.getClass(i++);
            if (cls == null) break;
            if (cls.isInterface() && VM.isAssignable(this, cls))
                interfaces[interfaceCnt++] = cls;
        }
        return interfaces;
    }
		
	@SuppressWarnings("unchecked")
	public Class<? super T> getSuperclass()
	{
		if (0 != (flags & (VM.VMClass.C_INTERFACE | VM.VMClass.C_PRIMITIVE)) || this == Object.class)
			return null;
		
		return (Class<? super T>)VM.getClass(this.parentClass & 0xFF);
	}

	public boolean isAnnotation()
	{
		return this.isInterface() && Annotation.class != this && Annotation.class.isAssignableFrom(this);
	}	
	
	public boolean isArray()
	{
		return 0 != (flags & VM.VMClass.C_ARRAY);
	}	
	
	public boolean isAssignableFrom(Class<?> cls)
	{
		return VM.isAssignable(cls, this);
	}	
	
	public boolean isEnum()
	{
		return this.getSuperclass() == Enum.class;
	}	
	
	public boolean isInstance(Object obj)
	{
		if (obj == null)
			return false;
		
		return this.isAssignableFrom(obj.getClass());
	}
	
	public boolean isInterface()
	{
		return 0 != (flags & VM.VMClass.C_INTERFACE);
	}
	
	public boolean isPrimitive()
	{
		return 0 != (flags & VM.VMClass.C_PRIMITIVE);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (this.isInterface())
			sb.append("interface ");
		else
			sb.append("class ");
		
		sb.append(VM.getClassNumber(this));
		
		return sb.toString();
	}
}
