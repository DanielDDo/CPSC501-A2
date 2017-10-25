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
			
			
			// Class Constructors
			Constructor[] classConstructors = objClass.getDeclaredConstructors();
			for(Constructor c: classConstructors) {
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
			
			
			// Class fields
			Field[] classFields = objClass.getDeclaredFields();
			for(Field f: classFields) {
				System.out.println("Field: \t\t" + f.getName());
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
						System.out.println("  Field Value: \t  " + f.getType().getSimpleName() + " " + System.identityHashCode(f));
					} else {
						System.out.println("  Field Value: \t  " + fieldValue.toString());
					}
					/*
					if (f.getType().isMemberClass() && (!recursive)) {
						System.out.println("  Field Value: \t  " + fieldValue.toString() + " " + fieldValue.hashCode());
					} else {
						System.out.println("  Field Value: \t  " + fieldValue.toString());
					}*/
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			
				System.out.println();

			}
		}
}
