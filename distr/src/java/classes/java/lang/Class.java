package java.lang;

/**
 * Not functional. It's here to satisfy javac and jikes.
 */
public class Class {
	/**
	 * Used to lock at class level, as sometime we need in static methods.
	 */
	public final static Object lock = new Object();

	/**
	 * @exception java.lang.ClassNotFoundException Thrown always in TinyVM.
	 */
	public static Class forName(String aName) throws ClassNotFoundException {
		throw new ClassNotFoundException();
	}
}
