package net.technowizardry;

import net.technowizardry.JavaXMLStreamFactory;
import net.technowizardry.XMLStreamFactory;

public class XMLStreamFactoryFactory {
	public static XMLStreamFactory newInstance() {
		String vmName = System.getProperty("java.vm.name");
		if (vmName.equals("Dalvik")) {
			return new AndroidXMLStreamFactory();
		} else {
			return new JavaXMLStreamFactory();
		}
	}
}
