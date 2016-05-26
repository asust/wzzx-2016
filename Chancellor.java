package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//��
public class Chancellor extends AbstractChessman {

	// ��Ϊ��Ļ�Ҫ��һ������ ��Ϊ���Ĳ���ֵ��ȷ�����Ǻ��Ǻ�
	// ��Ϊ��һ���߽�ֵ���ж�
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
		// ��ʼ�жϱ߽�ֵ
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
