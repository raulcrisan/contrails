package ro.spaceapps.contrails.opencv;

import org.opencv.core.Point;

public class LineDTO {

	private Point p1;
	private Point p2;
	private double[] line;
	
	public LineDTO(double[] line) {
		this.line = line;
		p1 = new Point(line[0], line[1]);
		p2 = new Point(line[2], line[3]);
	}
	
	public LineDTO(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
		
		line = new double[4];
		line[0] = p1.x;
		line[1] = p1.y;
		line[2] = p2.x;
		line[3] = p2.y;
	}
	
	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public double[] getLine() {
		return line;
	}

	public void setLine(double[] line) {
		this.line = line;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineDTO other = (LineDTO) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}
}
