package com.tomchm.space;

public class RoomConnection implements Comparable<RoomConnection>{
	private Room roomA, roomB;
	private double distance;
	
	public RoomConnection(Room roomA, Room roomB){
		this.roomA = roomA;
		this.roomB = roomB;
		this.distance = calculateDistance();
	}
	
	public Room getRoomA(){
		return roomA;
	}
	
	public Room getRoomB(){
		return roomB;
	}
	
	@Override
	public int compareTo(RoomConnection rc){
		if((roomA.getID() == rc.getRoomA().getID() && roomB.getID() == rc.getRoomB().getID()) || (roomA.getID() == rc.getRoomB().getID() && roomB.getID() == rc.getRoomA().getID())){
			return 0;
		}
		else{
			return (int) (distance - rc.getDistance());
		}
	}
	
	public double getDistance(){
		return distance;
	}
	
	public Room compareEnd(Room room){
		if(room.getID() == roomA.getID()){
			return roomB;
		}
		if(room.getID() == roomB.getID()){
			return roomA;
		}
		return null;
	}
	
	private double calculateDistance(){
		double xa = (double) roomA.getX();
		double ya = (double) roomA.getY();
		double xb = (double) roomB.getX();
		double yb = (double) roomB.getY();
		
		return (xa - xb) * (xa - xb) + (ya - yb) * (ya - yb);
	}
}
