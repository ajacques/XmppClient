package net.technowizardry.xmppclient.networking;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class ExternalLibraryBasedDnsResolver implements DnsResolver {
	public SortedSet<ServiceRecord> performSRVLookup(String domain) throws TextParseException, UnknownHostException {
		Lookup lookup = new Lookup(domain, Type.SRV, DClass.IN);
		lookup.setResolver(new SimpleResolver());
		lookup.run();

		validateResultCode(lookup);

		SortedSet<ServiceRecord> results = new TreeSet<ServiceRecord>();

		for (Record record : lookup.getAnswers()) {
			SRVRecord srv = (SRVRecord)record;
			results.add(new ServiceRecord(srv.getTarget().toString(), srv.getPort(), srv.getPriority(), srv.getWeight()));
		}

		return results;
	}

	public Set<ARecord> performALookup(String domain) throws UnknownHostException, TextParseException {
		Lookup lookup = new Lookup(domain, Type.A, DClass.IN);
		lookup.setResolver(new SimpleResolver());
		lookup.run();

		validateResultCode(lookup);

		Set<ARecord> result = new HashSet<ARecord>();

		for (Record record : lookup.getAnswers()) {
			ARecord a = (ARecord)record;
			result.add(a);
		}

		return result;
	}

	private void validateResultCode(Lookup lookup) {
		switch (lookup.getResult())
		{
		case Lookup.SUCCESSFUL:
			return;
		default:
			throw new RuntimeException("Un-expected error occurred while attempting DNS lookup");
		}
	}
}
