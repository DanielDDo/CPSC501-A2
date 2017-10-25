import java.util.*;
import java.lang.Class;
import java.lang.Object;
import java.lang.reflect.*;

public class Inspector {
	

		public Inspector() {
			
		}
		
		public void inspect(Object obj, boolean recursive) {
			Class objClass = obj.getClass();
				
			// Declaring class name
			System.out.println("Declaring class: " + objClass.getSimpleName());
			
			// Immediate superclass name
			Class superClass = objClass.getSuperclass();
			System.out.println("Super class: \t" + superClass.getSimpleName());
						
			System.out.println();

			// Interfaces the class implements
			Class[] interfaces = objClass.getInterfaces();
			System.out.print("Interfaces:\t");
			if (interfaces.length == 0) {
				System.out.println();
			} else {
				System.out.println(interfaces[0].getSimpleName());
				for(int i = 1; i < interfaces.length; i++) {
					System.out.println("\t\t" + interfaces[i].getSimpleName());
				}
			}
		
			System.out.println();
			
			// Methods the class declares
			Method[] classMethods = objClass.getDeclaredMethods();
			for(Method m: classMethods) {
				printMethodInformation(m);
			}
			
			
			// Class Constructors
			Constructor[] classConstructors = objClass.getDeclaredConstructors();
			for(Constructor c: classConstructors) {
				printClassConstructorInformation(c);
			}
			
			
			// Class fields
			Field[] classFields = objClass.getDeclaredFields();
			for(Field f: classFields) {
				printFieldInformation(f, obj, recursive);
			}
		}
		
		public void printMethodInformation(Method m) {
			System.out.println("Method: \t" + m.getName());
			// Thrown exceptions of the method
			Class[] exceptionTypes = m.getExceptionTypes();
			System.out.print("  Exception: \t  ");
			for(Class e: exceptionTypes) {
				System.out.print(e.getSimpleName() + " ");
			}
			System.out.println();
			
			// Parameter types
			Class[] parameterTypes = m.getParameterTypes();
			System.out.print("  Parameter: \t  ");
			for (Class p: parameterTypes) {
				System.out.print(p.getSimpleName() + " ");
			}
			System.out.println();
			
			// Return type
			Class returnType = m.getReturnType();
			System.out.println("  Return Type: \t  " + returnType.getSimpleName());
			
			// Modifiers
			int modifiers = m.getModifiers();
			System.out.println("  Modifiers: \t  " + Modifier.toString(modifiers));
			System.out.println();
		}
		
		public void printClassConstructorInformation(Constructor c) {
			System.out.println("Constructor: \t" + c.getName());
			// parameter types
			Class[] parameterTypes = c.getParameterTypes();
			System.out.print("  Parameter: \t  ");
			for (Class p: parameterTypes) {
				System.out.print(p.getSimpleName() + " ");
			}
			System.out.println();
			// constructor modifiers
			int modifiers = c.getModifiers();
			System.out.println("  Modifiers: \t  " + Modifier.toString(modifiers));
			System.out.println();
		}
		
		
		public void printFieldInformation(Field f, Object obj, boolean recursive) {
			System.out.println("Field: \t\t" + f.getName());
			if (!f.getType().isArray()) {
				// field type
				System.out.println("  Field Type: \t  " + f.getType().getSimpleName());
				// field modifiers
				int modifiers = f.getModifiers();
				System.out.println("  Modifiers: \t  " + Modifier.toString(modifiers));
				// field values
				try {
					f.setAccessible(true);
					Object fieldValue = f.get(obj);
					if (!f.getType().isPrimitive() && (!recursive))  {
						System.out.println("  Field Value: \t  " + f.getType().getSimpleName() + "  " + System.identityHashCode(fieldValue));
					} else {
						System.out.println("  Field Value: \t  " + fieldValue.toString());
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			
				System.out.println();
			}
			else {
				// Array's component type
				Class arrayComponentType = f.getType().getComponentType();
				System.out.println("  Component Type: " + arrayComponentType);
				try {
					// Array's length
					f.setAccessible(true);
					int len = Array.getLength(f.get(obj));
					System.out.println("  Length: \t  " + len);
					// Array Contents
					System.out.print("  Array Content:  [");
					for (int i = 0; i < len-1; i++) {
						Object arrayContent = Array.get(f.get(obj), i);
						System.out.print(arrayContent + ", ");
					}
					System.out.println(Array.get(f.get(obj), len-1) + "]");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				System.out.println();
			}
		}
}
