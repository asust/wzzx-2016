package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

public class Horse extends AbstractChessman {
	public Horse(Point start, String color) {

		point = start;
		String name = color + "_m";
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
		// 马走日子 且会被别马腿
		// 1 . 先判断走的是不是日字
		// 1.横着走
		int endx;
		int startx;
		int starty;
		int endy;
		// 1-1 赋值
		if (end.x > this.point.x) {
			startx = point.x;
			endx = end.x;
		} else {
			startx = end.x;
			endx = point.x;
		}
		if (end.y > this.point.y) {
			starty = point.y;
			endy = end.y;
		} else {
			starty = end.y;
			endy = point.y;
		}

		System.out.println(startx + "," + endx);
		System.out.println(starty + "," + endy);
		boolean issun = false;

		// 1-2 是不是走的日字
		/*
		 * if ((((endx - startx) == 120) && (endy - starty == 60)) || (((endy -
		 * starty) == 120) && (endx - startx == 60))) { issun = true; }
		 */
		if ((endx - startx == 120) && ((endy - starty) == 60)) {
			issun = true;
		} else if (((endx - startx) == 60) && ((endy - starty) == 120)) {
			issun = true;
		}
		//不是日的就直接走人
		if (!issun) {
			return false;
		}
		boolean flag = true;

		System.out.println("我在判断");
		// 2 .如果是日字的话就要判断是不是别这马腿了
		Point flagP = null;
		// 如果是横着走
		if (((endx - startx) == 120) && ((endy - starty) == 60)) {
			int x = (endx + startx) / 2;
			int y = point.y;
			flagP = new Point(x, y);
			for (Chessman c : list) {
				Point p = c.getPoint();
				if (p.x == flagP.x && p.y == flagP.y) {
					flag = false;
				}
			}

		} else {
			// 如果是竖着走的
			int y = (endy + starty) / 2;
			int x = point.x;
			flagP = new Point(x, y);
			for (Chessman c : list) {
				Point p = c.getPoint();
				if (p.x == flagP.x && p.y == flagP.y) {
					flag = false;
				}
			}

		}
		System.out.println(flag);
		return flag;
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
