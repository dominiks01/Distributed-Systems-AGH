package sr.ice.server;


import Demo.Calc;
import com.zeroc.Ice.Current;
import java.io.Serializable;

public class CalcI implements Calc, Serializable {

	@Override
	public long add(int a, int b, Current __current) {
		System.out.println("ADD FUNCTION: [" + a + " + " +  b + "]") ;

		if (a > 1000 || b > 1000) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		if (!__current.ctx.values().isEmpty()) {
			System.out.println("There are some properties in the context");
		}

		return a + b;
	}

	@Override
	public long subtract(int a, int b, Current __current) {
		System.out.println("SUBTRACT FUNCTION: [" + a + " - " +  b + "]") ;
		return a - b;
	}

	@Override
	public void print(int[] list, Current current) {
		System.out.println("PRINT SEQUENCE OF INTS");

		for(int i : list)
			System.out.println(i);
	}



}