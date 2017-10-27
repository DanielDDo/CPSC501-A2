import java.util.*;
import java.lang.Class;
import java.lang.Object;
import java.lang.reflect.*;

public class Inspector {
		private static final String seperator = "****************************************";

		private LinkedList<Class> classQueue = new LinkedList<Class>();
		private LinkedList<Integer> objectQueue = new LinkedList<Integer>();
		private HashMap<Integer, Object> objectMap = new HashMap<Integer, Object>();
		private HashSet<Integer> visited = new HashSet<Integer>();
		
		private boolean recursive;
		private Object obj;
	
		public Inspector() {
			
		}
		
		// Put class into queue, class hashcode and object into objectmap, and hashcode into visited when visited
		public void inspect(Object obj, boolean recursive) {
			this.obj = obj;
			this.recursive = recursive;
			Class objClass = obj.getClass();
			
			classQueue.add(objClass);
			
			// Declaring class name
			
			while (!classQueue.isEmpty()) {
				objClass = classQueue.poll();
					visited.add(System.identityHashCode(objClass));
					System.out.println("Declaring class: " + objClass.getSimpleName());
					
					// Immediate superclass name
					Class superClass = objClass.getSuperclass();
					if (superClass != null) {
						System.out.println("Super class: \t" + superClass.getSimpleName());
						classQueue.add(superClass);
					} else {
						System.out.println("Super class: \t");
					}
					
								
					// Interfaces the class implements
					Class[] interfaces = objClass.getInterfaces();
					System.out.print("Interfaces:\t");
					if (interfaces.length == 0) {
						System.out.println();
					} else {
						for (Class i: interfaces) {
							classQueue.add(i);
						}
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
						printFieldInformation(f);
					}
					
					System.out.println("======================================================");
					
			}
			while (!objectQueue.isEmpty()) {
				Integer idhashCode = objectQueue.poll();
				Object idObject = objectMap.get(idhashCode);
				inspect(idObject, recursive);
				visited.add(idhashCode);
			}
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
		
		
		public void printFieldInformation(Field f) {
			System.out.println("  Name: \t" + f.getName());
			
			// if field is an array
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
						if (!f.getType().isPrimitive())  {
							if (!recursive) {
								System.out.println("    Field Value:    " + f.getType().getSimpleName() + "  " + System.identityHashCode(fieldValue));
							} else {
								// put object hashcode into hashmap with the object reference, then add to the object queue
								objectMap.put(System.identityHashCode(fieldValue), fieldValue);
								objectQueue.add(System.identityHashCode(fieldValue));
							}
						} else {
							System.out.println("    Field Value:    " + fieldValue.toString());
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			
				System.out.println();
			}
			// field is not an array
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
						if (f.getType().getComponentType().isPrimitive()) {
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
							Object arrayContent;
							if (len > 0) {
								for (int i = 0; i < len-1; i++) {
									arrayContent = Array.get(f.get(obj), i);
									if (arrayContent != null) {
										System.out.print(f.getType().getComponentType().getSimpleName() + " " + System.identityHashCode(arrayContent) + ", ");
										if (recursive) {
											objectMap.put(System.identityHashCode(arrayContent), arrayContent);
											objectQueue.add(System.identityHashCode(arrayContent));
										}
									} else {
										System.out.print("null, ");
									}
								}
								arrayContent = Array.get(f.get(obj), len-1);
								if(arrayContent != null) {
									System.out.println(f.getType().getComponentType().getSimpleName() + " " + System.identityHashCode(arrayContent) + "]");
									if (recursive) {
										objectMap.put(System.identityHashCode(arrayContent), arrayContent);
										objectQueue.add(System.identityHashCode(arrayContent));
									}
								} else {
									System.out.println("null]");
								}
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
