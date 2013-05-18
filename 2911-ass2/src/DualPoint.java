/**
 * DualPoint interface.
 * 
 * Provides the interface for a "node" that consists of 
 *  two points - a point "from" and a point "to". This 
 *  interfaces allows access to these two pairs of points, 
 *  as well as providing access to the distance between these
 *  two points, and the distance between "to" of one point, to
 *  the "from" of a second point.
 * 
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 15th May 2013
 */
public interface DualPoint
{
	/**
	 * Return distance between "destination" and "origin"
	 *  x and y coordinates of this DualPoint object
	 * @return Distance between "destination" and "origin" 
	 *  coordinates
	 */
	public int getInternalDistance();
	
	/**
	 * Return distance between "destination" of one DualPoint, 
	 *  to the "origin" of another DualPoint
	 * @param pointTo DualPoint containing "origin" that requires
	 *  finding the distance to from "this" destination
	 * @return Distance between "destination" of this DualPoint,
	 *  and "origin" of another DualPoint
	 */
	public int getExternalDistanceTo(DualPoint pointTo);
	
	/**
	 * Return x value of origin point
	 * @return x-coordinate of "origin" point
	 */
	public int getFromX();
	
	/**
	 * Return y value of origin point
	 * @return y-coordinate of "origin" point
	 */
	public int getFromY();
	
	/**
	 * Return x value of destination point
	 * @return x-coordinate of "destination" point
	 */
	public int getToX();
	
	/**
	 * Return y value of destination point
	 * @return y-coordinate of "destination" point
	 */
	public int getToY();	
	
	public boolean isSamePoint();
	
	public String toString();
}