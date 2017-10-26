import java.util.*;
import java.lang.Class;
import java.lang.Object;
import java.lang.reflect.*;

public class Inspector {
		private static final String seperator = "****************************************";

		public Inspector() {
			
		}
		
		public void inspect(Object obj, boolean recursive) {
			Class objClass = obj.getClass();
				
			// Declaring class name
			System.out.println("Declaring class: " + objClass.getSimpleName());
			
			// Immediate superclass name
			Class superClass = objClass.getSuperclass();
			System.out.println("Super class: \t" + superClass.getSimpleName());
						
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
		
			System.out.println(seperator);
			
			// Methods the class declares
			System.out.println("Methods: ");
			Method[] classMethods = objClass.getDeclaredMethods();
			for(Method m: classMethods) {
				printMethodInformation(m);
			}
			
			System.out.println(seperator);
			
			// Class Constructors
			System.out.println("Constructors: ");
			Constructor[] classConstructors = objClass.getDeclaredConstructors();
			for(Constructor c: classConstructors) {
				printClassConstructorInformation(c);
			}
			
			System.out.println(seperator);

			// Class fields
			System.out.println("Fields: ");
			Field[] classFields = objClass.getDeclaredFields();
			for(Field f: classFields) {
				printFieldInformation(f, obj, recursive);
			}
			
			System.out.println("======================================================");
		}
		
		public void printMethodInformation(Method m) {
			System.out.println("  Name: \t" + m.getName());
			
			// Thrown exceptions of the method
			Class[] exceptionTypes = m.getExceptionTypes();
			if (exceptionTypes.length != 0) {
				System.out.print("    Exception: \t  ");
				for(Class e: exceptionTypes) {
					System.out.print(e.getSimpleName() + " ");
				}
				System.out.println();
			}
			
			// Parameter types
			Class[] parameterTypes = m.getParameterTypes();
			if (parameterTypes.length != 0) {
				System.out.print("    Parameter: \t  ");
				for (Class p: parameterTypes) {
					System.out.print(p.getSimpleName() + " ");
				}
				System.out.println();
			}
			
			// Return type
			Class returnType = m.getReturnType();
			System.out.println("    Return Type:  " + returnType.getSimpleName());
			
			// Modifiers
			int modifiers = m.getModifiers();
			if (modifiers != 0) {
				System.out.println("    Modifiers: \t  " + Modifier.toString(modifiers));
			}
			System.out.println();
		}
		
		public void printClassConstructorInformation(Constructor c) {
			System.out.println("  Name: \t" + c.getName());
			
			// parameter types
			Class[] parameterTypes = c.getParameterTypes();
			if(parameterTypes.length != 0) {
				System.out.print("    Parameter: \t  ");
				for (Class p: parameterTypes) {
					System.out.print(p.getSimpleName() + " ");
				}
				System.out.println();
			}
			
			// constructor modifiers
			int modifiers = c.getModifiers();
			if (modifiers != 0) {
				System.out.println("    Modifiers: \t  " + Modifier.toString(modifiers));
			}
			System.out.println();
		}
		
		
		public void printFieldInformation(Field f, Object obj, boolean recursive) {
			System.out.println("  Name: \t" + f.getName());
			
			if (!f.getType().isArray()) {
				// field type
				System.out.println("    Field Type:     " + f.getType().getSimpleName());
				// field modifiers
				int modifiers = f.getModifiers();
				if (modifiers != 0) {
					System.out.println("    Modifiers:      " + Modifier.toString(modifiers));
				}
				// field values
				try {
					f.setAccessible(true);
					if (f.get(obj) != null) {	
						Object fieldValue = f.get(obj);
						if (!f.getType().isPrimitive() && (!recursive))  {
							System.out.println("    Field Value:    " + f.getType().getSimpleName() + "  " + System.identityHashCode(fieldValue));
						} else {
							System.out.println("    Field Value:    " + fieldValue.toString());
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			
				System.out.println();
			}
			else {
				// Array's component type
				Class arrayComponentType = f.getType().getComponentType();
				System.out.println("    Component Type: " + arrayComponentType);
				try {
					// Array's length
					f.setAccessible(true);
					if (f.get(obj) != null) {
						int len = Array.getLength(f.get(obj));
						System.out.println("    Length: \t    " + len);
						// Array Contents
						if ((f.getType().getComponentType().isPrimitive()) && (!recursive)) {
							System.out.print("    Array Content:  [");
							if (len > 0) {
								for (int i = 0; i < len-1; i++) {
									Object arrayContent = Array.get(f.get(obj), i);
									System.out.print(arrayContent + ", ");
								}
								System.out.print(Array.get(f.get(obj), len-1));
							}
							System.out.println("]");
						} else {
							
							System.out.print("    Array Content:  [");
							//" + f.getType().getComponentType().getSimpleName() + "  " + System.identityHashCode(fieldValue));
							if (len > 0) {
								for (int i = 0; i < len-1; i++) {
									Object arrayContent = Array.get(f.get(obj), i);
									System.out.print(f.getType().getComponentType().getSimpleName() + " " + System.identityHashCode(arrayContent) + ", ");
								}
								System.out.println(f.getType().getComponentType().getSimpleName() + " " + System.identityHashCode(Array.get(f.get(obj), len-1)) + "]");
							}

						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				System.out.println();
			}
		}
}
