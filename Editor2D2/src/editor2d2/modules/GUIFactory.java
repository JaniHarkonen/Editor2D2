package editor2d2.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import editor2d2.gui.body.PropertiesPane;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;

public final class GUIFactory {
	
	private static class FactoryContainer {
		public String assetClass = "";
		public String placeableClass = "";
		public Function<ModalWindow, ModalView<? extends Asset>> modalFactory;
		public Function<Placeable, PropertiesPane> propertiesPaneFactory;
		
		public FactoryContainer(String assetClass) {
			this.assetClass = assetClass;
		}
	}
	

	private static final ArrayList<FactoryContainer> factoryList = new ArrayList<FactoryContainer>();
	private static final Map<String, FactoryContainer> factoryMap = new HashMap<String, FactoryContainer>();
	
	private static boolean isInitialized = false;
	
		// Do not instantiate
	private GUIFactory() { }
	
	
	public static void initialize() {
		if( isInitialized )
		return;
		
		DeclarationBootstrap.bootstrap();
		isInitialized = true;
	}
	
	
	/************************* DECLARATIONS ****************************/
	
	public static void declareAsset(int index, String assetClass, String placeableClass) {
		FactoryContainer fcont = new FactoryContainer(assetClass);
		fcont.placeableClass = placeableClass;
		
		if( index >= 0 && index < factoryList.size() )
		factoryList.add(index, fcont);
		else
		factoryList.add(fcont);
		
		factoryMap.put(assetClass, fcont);
	}
	
	public static void declareAsset(String assetClass, String placeableClass) {
		declareAsset(-1, assetClass, placeableClass);
	}
	
	public static void declareModalFactory(String assetClass, Function<ModalWindow, ModalView<? extends Asset>> factory) {
		FactoryContainer target = factoryMap.get(assetClass);
		target.modalFactory = factory;
	}
	
	public static void declarePropertiesPaneFactory(String assetClass, Function<Placeable, PropertiesPane> factory) {
		FactoryContainer target = factoryMap.get(assetClass);
		target.propertiesPaneFactory = factory;
	}
	
	
		/***************************** FACTORIES ********************************/
	
	public static String[] getClassTypes() {
		String[] types = new String[factoryList.size()];
		
		for( int i = 0; i < types.length; i++ )
		types[i] = factoryList.get(i).assetClass;
			
		return types;
	}
	
	public static String getPlaceableClass(String assetClass) {
		return factoryMap.get(assetClass).placeableClass;
	}
	
	public static ModalView<? extends Asset> createModalView(String assetClass, ModalWindow host) {
		return factoryMap.get(assetClass).modalFactory.apply(host);
	}
	
	public static PropertiesPane createPropertiesPane(String assetClass, Placeable source) {
		return factoryMap.get(assetClass).propertiesPaneFactory.apply(source);
	}
}
