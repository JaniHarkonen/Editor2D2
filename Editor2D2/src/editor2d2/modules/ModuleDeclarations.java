package editor2d2.modules;

class ModuleDeclarations{

	public static void bootstrap() {
		editor2d2.modules.image.Factories.declare();
		editor2d2.modules.data.Factories.declare();
		editor2d2.modules.object.Factories.declare();
	}
}
