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
		if( factoriesMap.get(assetClass) != null )
		return;
		
		FactoryContainer fc = new FactoryContainer(assetClass, placeableClass, factories);
		
		if( index >= 0 && index < factoriesList.size() )
		factoriesList.add(index, fc);
		else
		factoriesList.add(fc);
		
		factoriesMap.put(assetClass, fc);
	}
	
	public static void declareAsset(String assetClass, String placeableClass, AbstractFactories<? extends Asset> factories) {
		declareAsset(-1, assetClass, placeableClass, factories);
	}
	
	public static void declareAsset(int index, String assetClass) {
		declareAsset(index, assetClass, null, null);
	}
	
	public static void declareAsset(String assetClass) {
		declareAsset(-1, assetClass);
	}
	
	public static void declarePlaceableClass(String assetClass, String placeableClass) {
		FactoryContainer fc = factoriesMap.get(assetClass);
		
		if( fc.placeableClass == null )
		fc.placeableClass = placeableClass;
	}
	
	public static void declarePlaceableFactories(String assetClass, AbstractFactories<? extends Asset> factories) {
		FactoryContainer fc = factoriesMap.get(assetClass);
		
		if( fc.factories == null )
		fc.factories = factories;
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
