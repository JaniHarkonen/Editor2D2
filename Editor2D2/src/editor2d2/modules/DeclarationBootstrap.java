package editor2d2.modules;

public class DeclarationBootstrap{

	public static void bootstrap() {
		editor2d2.modules.image.Declaration.declare();
		editor2d2.modules.object.Declaration.declare();
		editor2d2.modules.data.Declaration.declare();
	}
}
