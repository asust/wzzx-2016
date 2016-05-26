package chessman;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

//��
public class Soldier extends AbstractChessman {

	// ҲҪ��һ����������жϷ�Χ
	private String color;

	public Soldier(Point start, String color) {
		point = start;
		String name = color + "_z";
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
		// true Ϊ �� false Ϊ ��
		boolean flag = false;
		if ("r".equals(this.color)) {
			flag = true;
		}
		// �ڿ����ǲ���Խ���˱߽�
		if (flag) {
			if (point.y >= 330 && point.x == end.x && point.y - end.y == 60) {
				// δԽ���߽�
				return true;
			} else if (point.y < 330) {
				if (point.x == end.x && point.y - end.y == 60) {
					return true;
				}
				if (point.y == end.y
						&& (point.x - end.x == 60 || end.x - point.x == 60)) {
					return true;
				}
			}
		} else {
			if (point.y <= 270 && point.x == end.x && end.y - point.y == 60) {
				return true;
			} else if (point.y > 270) {
				if (end.x == point.x && end.y - point.y == 60) {
					return true;
				}
				if (point.y == end.y
						&& (end.x - point.x == 60 || point.x - end.x == 60)) {
					return true;
				}
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
