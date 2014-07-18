package com.tomchm.space;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpaceGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion[][] regions;
	TextureRegion black, red, blue, white;
	
	char[][] grid;
	Room[] rooms;
	Tunneler[] tunnels;
	
	int totalFails = 0;
	double roomAverageX = 0, roomAverageY = 0;
	
	
	private void blankGrid(){
		for (int i=0; i< grid.length; i++){
			for (int j=0; j < grid[0].length; j++){
				grid[i][j] = '.';
			}
		}
	}
	
	private void addRooms(int n){
		rooms = new Room[n];
		
		for (int i=0; i < n; i++){
			addRectangle(i);
		}
	}
	
	private void addRectangle(int i){
		
		Random r = new Random();
		int maxX = grid.length-2*10;
		int maxY = grid[0].length-2*10;
		
		int x = 0, y = 0;
		
		boolean findEmpty = true;
		int width = triDist(5, 10);
		int length = triDist(5, 10);
		
		while(findEmpty && totalFails < 10000000){
			findEmpty = false;
			
			x = 10 + r.nextInt(maxX);
			y = 10 + r.nextInt(maxY);
			
			Room test = new Room(width, length, x, y);
			
			for(int j = 0; j < i; j++){
				Room compareRoom = rooms[j];
				if(compareRoom.noOverlap(test) == false){
					findEmpty = true;
					totalFails += 1;
					break;
				}
			}
		}
		
		rooms[i] = new Room(width, length, x, y);
		
		reAverageRooms(x, y, i);
		
		for( int xx=rooms[i].getLeft(); xx <= rooms[i].getRight(); xx++){
			for (int yy=rooms[i].getBottom(); yy <= rooms[i].getTop(); yy++){
				grid[xx][yy] = 'O';
			}
		}
	}
	
	private int triDist(int min, int max){
		int size = max - min;
		int randomMultiplier = size * (size + 1) / 2;
		Random r = new Random();
		int randomInt = r.nextInt(randomMultiplier);
		
		int number = 0;
		for (int i=size; randomInt >= 0; i--){
			randomInt -= i;
			number++;
		}
		
		return number + min;
	}
	
	private void reAverageRooms(int x, int y, int i){
		roomAverageX = ((roomAverageX * (i-1)) + x) / i;
		roomAverageY = ((roomAverageY * (i-1)) + y) / i;
	}
	
	private void addTunnels(){
		int n = rooms.length;
		Random r = new Random();
		
		for(int i=0; i < n; i++){
			int x = r.nextInt(rooms[i].getWidth()) + rooms[i].getLeft();
			int y = r.nextInt(rooms[i].getLength()) + rooms[i].getBottom();
			new Tunneler(x, y, grid);
		}
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("tex_box.png");
		regions = TextureRegion.split(img, 8, 8);
		black = regions[0][0];
		red = regions[1][0];
		blue = regions[0][1];
		white = regions[1][1];
		
		grid = new char[128][128];
		blankGrid();
		addRooms(75);
		addTunnels();
	}
	

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (int i=0; i< grid.length; i++){
			for (int j=0; j < grid[0].length; j++){
				if(grid[i][j] == '.'){
					batch.draw(black, i*8, j*8);
				}
				else if(grid[i][j] == 'X'){
					batch.draw(white, i*8, j*8);
				}
				else{
					batch.draw(red, i*8, j*8);
				}
			}
		}
		
		System.out.println(totalFails);
		batch.end();
	}
	
	
}
