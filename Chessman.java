package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public interface Chessman {
	public BufferedImage getImage();
	public Point getPoint();
	boolean Move(Point end, List<Chessman> list);
	void setPoint(Point point);
	boolean getName();
}
