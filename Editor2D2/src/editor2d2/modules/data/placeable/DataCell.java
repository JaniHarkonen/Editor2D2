package editor2d2.modules.data.placeable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import editor2d2.common.grid.Grid;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.data.asset.Data;

public class DataCell extends Placeable {

		// Cellular width of the data cell
	private int cellWidth;
	
		// Cellular height of the data cell
	private int cellHeight;
	
	
	public DataCell() {
		
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
		
		Data src = getData();
		Color srcColor = src.getColor();
		String srcValue = src.getValue();
		
		Rectangle2D shape = new Rectangle2D.Double(f_x, f_y, f_w, f_h);
		rctxt.gg.setColor(srcColor);
		rctxt.gg.fill(shape);
		
			// Draw value
		int colr = srcColor.getRed(),
			colg = srcColor.getGreen(),
			colb = srcColor.getBlue();
		
		Color invColor = Color.BLACK;
		float invColorBrightness = 1 - Color.RGBtoHSB(colr, colg, colb, null)[2];	// determine the inverse color
		
			// Prevent the inverse color from blending in edge-cases
		if( invColorBrightness < 0.45 || invColorBrightness > 0.55 )
		invColor = new Color(Color.HSBtoRGB(0, 0, invColorBrightness));
		
		rctxt.gg.setColor(invColor);
		
			// Center the value text
		FontMetrics fm = rctxt.gg.getFontMetrics();
		Rectangle2D textDimensions = fm.getStringBounds(srcValue, rctxt.gg);
		
		float 	tx = (float) (f_x + ((f_w / 2) - textDimensions.getCenterX())),
				ty = (float) (f_y + ((f_w / 2) - textDimensions.getCenterY()));
		
		rctxt.gg.drawString(srcValue, tx, ty);
		
			// Highlight if selected
		rctxt.gg.setColor(Color.RED);
		drawSelection(rctxt.gg, (int) f_x, (int) f_y, (int) f_w - 1, (int) f_h - 1, 0d);
	}
	
	@Override
	public Placeable duplicate() {
		DataCell dupl = new DataCell();
		copyAttributes(this, dupl);
		
		return dupl;
	}
	
	
		// Returns the cellular width of the data cell
	public int getCellWidth() {
		return this.cellWidth;
	}
	
		// Returns the cellular height of the data cell
	public int getCellHeight() {
		return this.cellHeight;
	}
	
		// Returns a reference to the Data object this cell is derived from
	public Data getData() {
		return (Data) this.asset;
	}
	
	@Override
	public Asset getReferencedAsset() {
		return getData();
	}
	
	
		// Sets the cellular dimensions of the data cell
	public void setCellDimensions(int cw, int ch) {
		this.cellWidth = cw;
		this.cellHeight = ch;
	}
	
		// Changes the Data object reference
	public void setData(Data data) {
		this.asset = data;
	}
}
