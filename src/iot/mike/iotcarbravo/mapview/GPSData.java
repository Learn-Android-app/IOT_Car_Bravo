package iot.mike.iotcarbravo.mapview;

public class GPSData {
	private int xD;
	private int yD;
	public int getxD() {
		return xD;
	}
	public void setxD(int xD) {
		this.xD = xD;
	}
	public int getyD() {
		return yD;
	}
	public void setyD(int yD) {
		this.yD = yD;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xD;
		result = prime * result + yD;
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
		GPSData other = (GPSData) obj;
		if (xD != other.xD)
			return false;
		if (yD != other.yD)
			return false;
		return true;
	}
}
