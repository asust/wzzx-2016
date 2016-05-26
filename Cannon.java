package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//炮
public class Cannon extends AbstractChessman {

	public Cannon(Point start, String color) {

		point = start;
		String name = color + "_p";
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
		if (!((point.x == end.x && point.y != end.y) || (point.y == end.y && point.x != end.x))) {
			return false;
		}
		// 必须要走直线
		int startz;
		int endz;
		int wecan = 0;
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
						wecan++;
					}
					i += 60;
				}
			}
		} else if (point.y == end.y && point.x != end.x) {
			System.out.println("aaa");
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
						wecan++;
					}
					i += 60;
				}
			}
		}

		if (!((wecan == 1) || (wecan == 0))) {
			return false;
		}

		System.out.println("bbbb");
		// 如果中间没有检核 且落点没有子的情况下可以落子

		if (wecan == 0) {
			boolean flag = true;
			for (Chessman c : list) {
				Point p = c.getPoint();
				if (p.x == end.x && p.y == end.y) {
					flag = false;
				}
			}
			return flag;
		} else if (wecan == 1) {
			boolean flag = false;
			// 如果等于1的话落点必须有子
			for (Chessman c : list) {
				Point p = c.getPoint();
				if (p.x == end.x && p.y == end.y) {
					flag = true;
				}
			}
			return flag;
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
