package editor2d2.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.model.project.Asset;

public class FactoryService {

	private static class FactoryContainer {
		public String assetClass;
		public String placeableClass;
		public AbstractFactories<? extends Asset> factories;
		
		public FactoryContainer(String assetClass, String placeableClass, AbstractFactories<? extends Asset> factories) {
			this.assetClass = assetClass;
			this.placeableClass = placeableClass;
			this.factories = factories;
		}
	}
	
	
	private static final Map<String, FactoryContainer> factoriesMap = new HashMap<String, FactoryContainer>();
	private static final ArrayList<FactoryContainer> factoriesList = new ArrayList<FactoryContainer>();
	
	private static boolean isInitialized = false;
	
	public static void initialize() {
		if( isInitialized )
		return;
		
		ModuleDeclarations.bootstrap();
		isInitialized = true;
	}
	
	public static void declareAsset(int index, String assetClass, String placeableClass, AbstractFactories<? extends Asset> factories) {
		FactoryContainer fc = new FactoryContainer(assetClass, placeableClass, factories);
		
		if( index >= 0 && index < factoriesList.size() )
		factoriesList.add(index, fc);
		else
		factoriesList.add(fc);
		
		factoriesMap.put(assetClass, fc);
	}
	
	
	public static String[] getClassTypes() {
		String[] types = new String[factoriesList.size()];
		
		for( int i = 0; i < types.length; i++ )
		types[i] = factoriesList.get(i).assetClass;
			
		return types;
	}
	
	public static AbstractFactories<? extends Asset> getFactories(String assetClass) {
		return factoriesMap.get(assetClass).factories;
	}
	
	public static String getPlaceableClass(String assetClass) {
		return factoriesMap.get(assetClass).placeableClass;
	}
}
