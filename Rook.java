package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//车
public class Rook extends AbstractChessman {

	public Rook(Point start, String color) {

		point = start;
		String name = color + "_c";
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
		// 车是走直线的我们只用判断它是不是可以走直线即可
		if (point.x == end.x && point.y == end.y) {
			return true;
		}
		if (!((point.x == end.x && point.y != end.y) || (point.y == end.y && point.x != end.x))) {
			return false;
		}
		int startz;
		int endz;
		boolean flag = true;
		if (point.x == end.x && point.y != end.y) {
			if (point.y > end.y) {
				startz = end.y;
				endz = point.y;
			} else {
				startz = point.y;
				endz = end.y;
			}

			for (Chessman c : list) {
				// 看看有没有东西阻碍这我
				Point p = c.getPoint();
				for (int i = startz + 60; i < endz;) {

					if (p.y == i && p.x == point.x) {
						System.out.println("有人档");
						flag = false;
					}
					i += 60;
				}
			}
		} else if (point.y == end.y && point.x != end.x) {
			if (point.x > end.x) {
				startz = end.x;
				endz = point.x;
			} else {
				endz = end.x;
				startz = point.x;
			}
			// System.out.println(startz+"-"+endz);
			for (Chessman c : list) {
				for (int i = startz + 60; i < endz;) {
					// System.out.println(i+"   "+point.y);
					// 看看有没有东西阻碍这我
					Point p = c.getPoint();
					if (p.x == i && p.y == point.y) {
						System.out.println("中间有人");
						flag = false;
					}
					i += 60;
				}
			}
		}
		return flag;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public boolean getName() {
		return false;
	}
}
