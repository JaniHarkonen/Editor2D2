package editor2d2.modules.data.placeable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import editor2d2.common.grid.Grid;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.data.asset.Data;

public class DataCell extends Placeable {
	
		// Inverse color that is used to draw the value
		// stored in the cell
	private Color inverseColor;
	
	
	public DataCell() {
		this.inverseColor = null;
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		Camera cam = rctxt.camera;
		Grid objs = getLayer().getObjectGrid();
		double cam_z = cam.getZ();
		
		double f_x = cam.getOnScreenX(getX());
		double f_y = cam.getOnScreenY(getY());
		double f_w = objs.getCellWidth() * cam_z;
		double f_h = objs.getCellHeight() * cam_z;
		
		drawPlaceable(rctxt.gg, f_x, f_y, f_w, f_h);
		
			// Highlight if selected
		rctxt.gg.setColor(Color.RED);
		drawSelection(rctxt.gg, (int) f_x, (int) f_y, (int) f_w - 1, (int) f_h - 1, 0d);
	}
	
	@Override
	public void drawPlaceable(Graphics2D gg, double dx, double dy, double dw, double dh) {
		Data src = getData();
		Color srcColor = src.getColor();
		String srcValue = src.getValue();
		
		Rectangle2D shape = new Rectangle2D.Double(dx, dy, dw, dh);
		gg.setColor(srcColor);
		gg.fill(shape);
		
		gg.setColor(this.inverseColor);
		
			// Center the value text
		FontMetrics fm = gg.getFontMetrics();
		Rectangle2D textDimensions = fm.getStringBounds(srcValue, gg);
		
		float 	tx = (float) (dx + ((dw / 2) - textDimensions.getCenterX())),
				ty = (float) (dy + ((dh / 2) - textDimensions.getCenterY()));
		
		gg.drawString(srcValue, tx, ty);
	}
	
	@Override
	public Placeable duplicate() {
		DataCell dupl = new DataCell();
		copyAttributes(this, dupl);
		
		dupl.inverseColor = this.inverseColor;
		
		return dupl;
	}
	
		// Returns a reference to the Data object this cell is derived from
	public Data getData() {
		return (Data) this.asset;
	}
	
	@Override
	public Asset getReferencedAsset() {
		return getData();
	}
	
		// Changes the Data object reference
	public void setData(Data data) {
		this.asset = data;
		
			// Determine inverse color
		Color srcColor = data.getColor();
		
		int colr = srcColor.getRed(),
			colg = srcColor.getGreen(),
			colb = srcColor.getBlue();
		
		Color invColor = Color.BLACK;
		float invColorBrightness = 1 - Color.RGBtoHSB(colr, colg, colb, null)[2];	// inverse color
		
			// Prevent the inverse color from blending in edge-cases
		if( invColorBrightness < 0.45 || invColorBrightness > 0.55 )
		invColor = new Color(Color.HSBtoRGB(0, 0, invColorBrightness));
		
		this.inverseColor = invColor;
	}
}
