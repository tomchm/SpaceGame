package com.tomchm.space;

public class RoomRenderer {
	private static int topWall = 3, bottomWall = 2, sideWall = 1, wall = 0, floor = 1000;
	
	
	public static void renderRoom(Room room, char[][] grid, int[][] renderGrid){
		int l = 1, r = 1;
		if(grid[room.getLeft()][room.getBottom()] == 'D'){
			renderGrid[room.getLeft()][room.getBottom()] = bottomWall;
			renderGrid[room.getLeft()+1][room.getBottom()] = floor;
			l = 2;
		}
		else{
			renderGrid[room.getLeft()][room.getBottom()] = bottomWall;
			renderGrid[room.getLeft()+1][room.getBottom()] = wall;
		}
		
		if(grid[room.getRight()][room.getBottom()] == 'D'){
			renderGrid[room.getRight()][room.getBottom()] = bottomWall;
			renderGrid[room.getRight()-1][room.getBottom()] = floor;
			r = 2;
		}
		else{
			renderGrid[room.getRight()][room.getBottom()] = bottomWall;
			renderGrid[room.getRight()-1][room.getBottom()] = wall;
		}
		
		if(grid[room.getLeft()][room.getTop()] == 'D'){
			renderGrid[room.getLeft()][room.getTop()] = topWall;
			renderGrid[room.getLeft()+1][room.getTop()] = floor;
			l = 2;
		}
		else{
			renderGrid[room.getLeft()][room.getTop()] = topWall;
			renderGrid[room.getLeft()+1][room.getTop()] = wall;
		}
		
		if(grid[room.getRight()][room.getTop()] == 'D'){
			renderGrid[room.getRight()][room.getTop()] = topWall;
			renderGrid[room.getRight()-1][room.getTop()] = floor;
			r = 2;
		}
		else{
			renderGrid[room.getRight()][room.getTop()] = topWall;
			renderGrid[room.getRight()-1][room.getTop()] = wall;
		}
		
		for(int i=room.getLeft()+l;i<=room.getRight()-r;i++){
			if(grid[i][room.getTop()] == 'D'){
				renderGrid[i][room.getTop()] = floor;
			}
			else{
				renderGrid[i][room.getTop()] = wall;
			}
			
			if(grid[i][room.getBottom()] == 'D'){
				renderGrid[i][room.getBottom()] = floor;
			}
			else{
				renderGrid[i][room.getBottom()] = wall;
			}
		}
		
		for(int i=room.getTop()-1;i>=room.getBottom()+1;i--){
			if(grid[room.getLeft()][i] == 'D'){
				if(grid[room.getLeft()][i+1] == 'O'){
					renderGrid[room.getLeft()][i+1] = bottomWall;
				}
				
				renderGrid[room.getLeft()][i] = floor;
			}
			else{
				renderGrid[room.getLeft()][i] = sideWall;
			}
			
			if(grid[room.getRight()][i] == 'D'){
				if(grid[room.getRight()][i+1] == 'O'){
					renderGrid[room.getRight()][i+1] = bottomWall;
				}
				renderGrid[room.getRight()][i] = floor;
			}
			else{
				renderGrid[room.getRight()][i] = sideWall;
			}
		}
		
		for(int i=room.getLeft()+1;i<room.getRight();i++){
			for(int j=room.getBottom()+1;j<room.getTop();j++){
				renderGrid[i][j] = floor;
			}
		}
		
		
	}
}
