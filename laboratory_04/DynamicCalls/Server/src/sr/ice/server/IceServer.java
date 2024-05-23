package sr.ice.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;


public class IceServer {
	public void t1(String[] args) {

		int status = 0;
		Communicator communicator = null;
		ObjectAdapter adapter;

		try {
			communicator = Util.initialize(args);
			adapter = communicator.createObjectAdapterWithEndpoints("SimplePrinterAdapter", "default -p 10000");

			CalcI calcServant1 = new CalcI();
			adapter.add(calcServant1, communicator.stringToIdentity("DynamicCalls"));

			adapter.activate();
			System.out.println("Entering event processing loop...");
			communicator.waitForShutdown();

		} catch (Exception e) {
			e.printStackTrace(System.err);
			status = 1;
		}
		if (communicator != null) {
			try {
				communicator.destroy();
			} catch (Exception e) {
				e.printStackTrace(System.err);
				status = 1;
			}
		}
		System.exit(status);
	}


	public static void main(String[] args) {
		IceServer app = new IceServer();
		app.t1(args);
	}
}