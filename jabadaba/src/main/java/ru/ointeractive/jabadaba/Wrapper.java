	package ru.ointeractive.jabadaba;
	
	import java.util.LinkedHashMap;
	import java.util.Map;
	
	public class Wrapper {
		
		public Map<String, Adapter> providers = new LinkedHashMap<> ();
		
		public final Wrapper addProvider (Adapter provider) {
			
			providers.put (provider.getName (), provider);
			return this;
			
		}
		
		public final Wrapper addProviders (Map<String, Adapter> providers) {
			
			this.providers = new LinkedHashMap<> ();
			
			for (String name : providers.keySet ())
				addProvider (providers.get (name));
			
			return this;
			
		}
		
		public Adapter getProvider (String name) {
			return providers.get (name);
		}
		
	}