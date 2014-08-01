package com.tomchm.space;

import java.util.Random;

public class Paver {
	private Room room;
	private int x, y, deltaX = 0, deltaY = 0, direction, prob = 0, movements = 0, maxMovements;
	private int midX, midY, turnCount = -1, border, turns = 0;
	private char[][] grid;
	private boolean foundEmpty = false;
	private char prevBore = ' ';
	
	public Paver(Room room, char[][] grid, int border){
		this.room = room;
		x = room.getX();
		y = room.getY();
		this.grid = grid;
		this.border = border;
		midX = grid.length /2;
		midY = grid[0].length / 2;
		
		maxMovements = (grid.length + grid[0].length) / 3;
		changeDirection(3);
		pave();
	}

	
	private void pave() {
		boolean found = false;
		while(!found && (movements<maxMovements)){
			
			if(!foundEmpty){
				move();
			}
			else if(movements == turnCount){
				turn();
				move();
			}
			else if(checkFound()){
				move();
				found = true;
			}
			else if(grid[x+3*deltaX][y+3*deltaY] == 'X'){
				move();
			}
			else if(grid[x+3*deltaX][y+3*deltaY] == 'O' || grid[x+3*deltaX][y+3*deltaY] == 'D'){
				turn();
				move();
			}
			else if(turns==1 && movements == maxMovements/2){
				//System.out.println("Turned");
				turn();
				move();
			}
			else{
				move();
			}
			
		}
		//System.out.println("Turn"+movements);
	}
	
	private boolean checkFound(){
		if(grid[x+2][y] == 'X' && grid[x+2][y+1] == 'X' && grid[x+2][y-1] == 'X' && direction!=1) return true;
		if(grid[x-2][y] == 'X' && grid[x-2][y+1] == 'X' && grid[x-2][y-1] == 'X'  && direction!=0) return true;
		if(grid[x][y+2] == 'X' && grid[x+1][y+2] == 'X' && grid[x-1][y+2] == 'X'  && direction!=3) return true;
		if(grid[x][y-2] == 'X' && grid[x+1][y-2] == 'X' && grid[x-1][y-2] == 'X'   && direction!=2) return true;
		return false;
	}
	
	private void turn(){
		turns++;
		if(direction == 2 || direction == 3){
			if(x < midX){
				changeDirection(0);
			}
			else{
				changeDirection(1);
			}
		}
		else{
			if(y < midY){
				changeDirection(2);
			}
			else{
				changeDirection(3);
			}
		}
	}
	
	
	private void changeDirection(int direction){
		this.direction = direction;
		switch(direction){
		case 0:
			deltaX = 1;
			deltaY = 0;
			break;
		case 1:
			deltaX = -1;
			deltaY = 0;
			break;
		case 2:
			deltaX = 0;
			deltaY = 1;
			break;
		case 3:
			deltaX = 0;
			deltaY = -1;
			break;
		}
	}
	
	private void move() {
		
		if(!outOfBounds()){
			prevBore = grid[x][y];
			
			x += deltaX;
			y += deltaY;
			
			if(!foundEmpty && (grid[x+deltaX][y+deltaY] =='.' || grid[x+deltaX][y+deltaY] =='X')){
				grid[x][y] = 'D';
				foundEmpty = true;
				turnCount = movements + 4;
			}
			for(int i=-1; i<=1; i++){
				for(int j=-1; j<=1; j++){
					if(grid[x+i][y+j] == '.'){
						grid[x+i][y+j] = 'X';
					}
				}
			}	
		}
		else{
			turn();
		}
		movements += 1;
	}
	
	private boolean outOfBounds(){
		if(x + deltaX >= grid.length-border) return true;
		if(x + deltaX < border) return true;
		if(y + deltaY >= grid[0].length-border) return true;
		if(y + deltaY < border) return true;
		
		return false;
	}
}
