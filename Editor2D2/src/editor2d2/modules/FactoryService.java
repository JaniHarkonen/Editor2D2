package editor2d2.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.modules.data.Factories;

public class FactoryService {

	private static class FactoryContainer {
		public String assetClass;
		public String placeableClass;
		public Factories factories;
		
		public FactoryContainer(String assetClass, String placeableClass, Factories factories) {
			this.assetClass = assetClass;
			this.placeableClass = placeableClass;
			this.factories = factories;
		}
	}
	
	
	private static final Map<String, FactoryContainer> factoriesMap = new HashMap<String, FactoryContainer>();
	private static final ArrayList<FactoryContainer> factoriesList = new ArrayList<FactoryContainer>();
	
	
	public static void declareAsset(int index, String assetClass, String placeableClass, Factories factories) {
		FactoryContainer fc = new FactoryContainer(assetClass, placeableClass, factories);
		
		if( index >= 0 && index < factoriesList.size() )
		factoriesList.set(index, fc);
		else
		factoriesList.add(fc);
		
		factoriesMap.put(assetClass, fc);
	}
}
