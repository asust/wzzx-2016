package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//将
public class General extends AbstractChessman {

	// 将领也会有一个状态值
	private String color;

	public General(Point start, String color) {
		point = start;
		String name = color + "_j";
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
		boolean flag1 = point.x >= startx && point.x <= endx && point.y >= starty
				&& point.y <= endy;
		boolean flag2 = (end.x >= startx) && end.x <= endx && end.y >= starty
				&& end.y <= endy;
		if (!(flag1 && flag2)) {
			return false;
		}
		// 给定横竖位移的方法
		if (point.x == end.x && point.y != end.y) {
			int max;
			int min;
			if (point.y > end.y) {
				max = point.y;
				min = end.y;
			} else {
				max = end.y;
				min = point.y;
			}
			if (max - min == 60) {
				return true;
			}
		} else if (point.y == end.y && point.x != end.x) {
			int max;
			int min;
			if (point.x > end.x) {
				max = point.x;
				min = end.x;
			} else {
				max = end.x;
				min = point.x;
			}
			if (max - min == 60) {
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
		return true;
	}
}
