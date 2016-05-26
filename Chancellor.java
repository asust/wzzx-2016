package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//相
public class Chancellor extends AbstractChessman {

	// 作为相的话要多一个属性 作为它的参数值来确定他是红是黑
	// 因为有一个边界值的判断
	private String who = null;

	public Chancellor(Point start, String color) {

		this.point = start;
		this.who = color;
		String name = color + "_x";
		Class<?> obj;
		try {
			obj = Class.forName("chessui.ChessUI");
			Field[] f = obj.getDeclaredFields();
			for (Field field : f) {
				field.setAccessible(true);
				if (name.equals(field.getName())) {
					Image = (BufferedImage) field.get(obj.newInstance());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImage() {
		return Image;
	}

	public Point getPoint() {
		return point;
	}

	@Override
	public boolean Move(Point end, List<Chessman> list) {
		if (point.x == end.x && point.y == end.y) {
			return true;
		}
		// 开始判断边界值
		if ("r".equals(this.who) && end.y < 330) {
			return false;
		} else if ("b".equals(this.who) && end.y > 270) {
			return false;
		}
		int x = (this.point.x + end.x) / 2;
		int y = (this.point.y + end.y) / 2;
		Point flagP = new Point(x, y);
		for (Chessman c : list) {
			Point p = c.getPoint();
			if (p.x == flagP.x && p.y == flagP.y) {
				return false;
			}

		}

		return true;
	}

	@Override
	public void setPoint(Point point) {
		this.point = point;
	}
	
	@Override
	public boolean getName() {
		return false;
	}

}
