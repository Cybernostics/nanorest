import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class InterfaceTest {

	public static void main(String[] args) {
		InvocationHandler handler = new MyInvocationHandler();
		MyInterface proxy = (MyInterface) Proxy.newProxyInstance(
		                            MyInterface.class.getClassLoader(),
		                            new Class[] { MyInterface.class },
		                            handler);

		System.out.println(proxy.someMethod("Woohoo"));
	}

	public static class MyInvocationHandler implements InvocationHandler{

		  public Object invoke(Object proxy, Method method, Object[] args)
		  throws Throwable {
			  //System.out.println(method.getName());
			  //System.out.println(args[0]);
			return "Dynamic "+args[0].toString();
		    //do something "dynamic"
		  }
		}

	public static interface MyInterface
	{
		String someMethod(String someArg);
	}
}
