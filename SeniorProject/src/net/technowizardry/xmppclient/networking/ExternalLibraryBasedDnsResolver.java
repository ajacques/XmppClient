package net.technowizardry.xmppclient.networking;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class ExternalLibraryBasedDnsResolver implements DnsResolver {
	public List<ServiceRecord> performSRVLookup(String domain) throws TextParseException, UnknownHostException {
		Lookup lookup = new Lookup(domain, Type.SRV, DClass.IN);
		lookup.setResolver(new SimpleResolver());
		lookup.run();

		List<ServiceRecord> results = new ArrayList<ServiceRecord>();

		for (Record record : lookup.getAnswers()) {
			SRVRecord srv = (SRVRecord)record;
			results.add(new ServiceRecord(srv.getTarget().toString(), srv.getPort(), srv.getPriority(), srv.getWeight()));
		}

		return results;
	}
}
