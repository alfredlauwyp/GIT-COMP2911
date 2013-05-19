import java.util.ArrayList;
import java.lang.Math;

public class CourierDeliveryJob implements Node {
	
	public CourierDeliveryJob(ArrayList<Integer> coords) {
		xi = coords.get(INITIAL_X);
		yi = coords.get(INITIAL_Y);
		xf = coords.get(FINAL_X);
		yf = coords.get(FINAL_Y);
	}

	public int getManhattenDistance() {
		return Math.abs(xf - xi) + Math.abs(yf - yi); 
	}

	public int getXStart() {
		return xi;
	}

	public int getYStart() {
		return yi;
	}

	public int getXFinish() {
		return xf;
	}

	public int getYFinish() {
		return yf;
	}
	
	public int getTravelDistance(Node nextJob) {
		return Math.abs(xf - nextJob.getXStart()) + Math.abs(yf - nextJob.getYStart());
	}
	
	private int xi;
	private int yi;
	private int xf;
	private int yf;
	
	private static final int INITIAL_X = 0;
	private static final int INITIAL_Y = 1;
	private static final int FINAL_X = 2;
	private static final int FINAL_Y = 3;

}
