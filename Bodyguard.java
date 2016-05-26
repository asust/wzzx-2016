package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//仕
public class Bodyguard extends AbstractChessman {

	private String color;

	public Bodyguard(Point start, String color) {

		point = start;
		String name = color + "_s";
		this.color = color;
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
		// 用来标记颜色
		boolean wflag;
		if ("r".equals(color)) {
			wflag = true;
		} else {
			wflag = false;
		}
		int startx;
		int endx;
		int starty;
		int endy;
		if (wflag) {
			startx = 210;
			endx = 330;
			starty = 450;
			endy = 570;
		} else {
			startx = 210;
			endx = 330;
			starty = 30;
			endy = 150;
		}
		boolean flag1 = point.x >= startx && point.x <= endx
				&& point.y >= starty && point.y <= endy;
		boolean flag2 = (end.x >= startx) && end.x <= endx && end.y >= starty
				&& end.y <= endy;
		if (!(flag1 && flag2)) {
			return false;
		}
		int maxx;
		int maxy;
		int minx;
		int miny;
		System.out.println("aaaa");
		if (end.x > point.x) {
			maxx = end.x;
			minx = point.x;
			if (end.y > point.y) {
				maxy = end.y;
				miny = point.y;
			} else {
				maxy = point.y;
				miny = end.y;
			}
			if (maxx - minx == 60 && maxy - miny == 60) {
				return true;
			}
		} else {
			maxx = point.x;
			minx = end.x;
			if (end.y > point.y) {
				maxy = end.y;
				miny = point.y;
			} else {
				maxy = point.y;
				miny = end.y;
			}
			if (maxx - minx == 60 && maxy - miny == 60) {
				return true;
			}
		}

		return false;
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
