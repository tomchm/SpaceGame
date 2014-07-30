package com.tomchm.space;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpaceGame extends ApplicationAdapter{
	SpriteBatch batch;
	Texture img;
	TextureRegion[][] regions;
	TextureRegion black, red, blue, white;
	TextureRegion[] shade = new TextureRegion[8];
	
	char[][] grid;
	Room[] rooms;
	
	int totalFails = 0, totalConnects = 0, randomDoubleValue = 5, maxConnectivity = 1;
	private static int maxConnects = 2000000;
	double roomAverageX = 0, roomAverageY = 0;
	
	private ArrayList<RoomConnection> connections;
	
	
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
			
			Room test = new Room(width, length, x, y, i);
			
			for(int j = 0; j < i; j++){
				Room compareRoom = rooms[j];
				if(compareRoom.noOverlap(test) == false){
					findEmpty = true;
					totalFails += 1;
					break;
				}
			}
		}
		
		rooms[i] = new Room(width, length, x, y, i);
		
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
		int n = connections.size();
		Random r = new Random();
		for(int i=0; i < n; i++){
			//int x = r.nextInt(connections.get(i).getRoomA().getWidth()) + connections.get(i).getRoomA().getLeft();
			//int y = r.nextInt(connections.get(i).getRoomA().getLength()) + connections.get(i).getRoomA().getBottom();
			//int targetX = r.nextInt(connections.get(i).getRoomB().getWidth()) + connections.get(i).getRoomB().getLeft();
			//int targetY = r.nextInt(connections.get(i).getRoomB().getLength()) + connections.get(i).getRoomB().getBottom();
			
			int x = connections.get(i).getRoomA().getX();
			int y = connections.get(i).getRoomA().getY();
			int targetX = connections.get(i).getRoomB().getX();
			int targetY = connections.get(i).getRoomB().getY();
			
			
			new Tunneler(x, y, targetX, targetY, grid);
		}
	}
	
	private void addConnections(){
		int size = rooms.length;
		for(int i=0; i < size; i++){
			RoomConnection[] rc = new RoomConnection[size-1];
			int add = 0;
			for(int j=0; j < size-1; j++){
				if(i == j){
					add = 1;
				}
				rc[j] = new RoomConnection(rooms[i], rooms[j+add]);
				
			}
			rooms[i].addConnections(rc);
		}
	}
	
	private void finalizeConnections(){
		int size = rooms.length;
		Random r = new Random();
		for(int i=0; i < size; i++){
			int n = numPath(r.nextInt(100));
			RoomConnection[] minrc = rooms[i].mininumConnections(rooms.length-1);
			int num = 0;
			for(int j=0; num < n; j++){
				boolean canAdd = true;
				
				for(int k=0; k < connections.size(); k++){
					if(connections.get(k).compareTo(minrc[j]) == 0){
						canAdd = false;
					}
				}
				
				if(canAdd){
					connections.add(minrc[j]);
					num++;
				}
				
			}
		}
	}
	
	private void checkConnections(boolean verify){
		for(int i=0; i<rooms.length; i++){
			boolean check[] = new boolean[rooms.length];
			rooms[i].setConnectivity(checkHelper(rooms[i], check, verify));
		}
	}
	
	private int checkHelper(Room room, boolean check[], boolean verify){
		int n = 1;
		if(totalConnects > maxConnects){
			return n;
		}
		check[room.getID()] = true;
		for(int i=0; i<connections.size(); i++){
			Room test = connections.get(i).compareEnd(room);
			if(test != null){
				if(!check[test.getID()]){
					boolean checker[];
					if(verify){
						checker = check;
					}
					else{
						checker = new boolean[check.length];
						for(int j=0; j<check.length; j++){
							checker[j] = check[j];
						}
					}
					totalConnects += 1;
					n += checkHelper(test, checker, verify);
				}
			}
		}
		return n;
	}
	
	private int maxConnectivity(){
		int connectivity = 0;
		int newConnectivity = 0;
		for(Room room : rooms){
			newConnectivity = room.getConnectivity();
			if(newConnectivity > connectivity){
				connectivity = newConnectivity;
			}
		}
		return connectivity;
	}
	
	private boolean correctConnectivity(){
		int topConnectivity = maxConnectivity * 7 / 8;
		int count = 0;
		for(Room room : rooms){
			if(room.getConnectivity() >= topConnectivity){
				count++;
			}
		}
		System.out.print("Count: "+count+" ");
		return (count <= connections.size() / 8);
	}
	
	private int numPath(int r){
		if(r < randomDoubleValue){
			return 2;
		}
		else if( r < 95){
			return 1;
		}
		return 3;
		
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
		
		for(int i=0; i<8; i++){
			shade[i] = regions[2][i];
		}
		
		grid = new char[128][128];
		boolean verify = true;
		long curTime = System.nanoTime();
		while(verify){
			verify = createWorld();
		}
		
		System.out.println((System.nanoTime()-curTime)/1000000000.0);
		
	}
	
	private boolean createWorld(){
		
		totalConnects = 0;
		totalFails = 0;
		connections = new ArrayList<RoomConnection>();
		blankGrid();
		addRooms(grid.length * grid[0].length / 320);
		addConnections();
		finalizeConnections();
		addTunnels();
		checkConnections(true);
		boolean verify = false;
		for(int i=0; i<rooms.length; i++){
			//System.out.println(i+":"+rooms[i].getConnectivity());
			if(rooms[i].getConnectivity() < rooms.length){
				verify = true;
				break;
			}
			
		}
		checkConnections(false);
		if(totalConnects >= maxConnects){
			verify = true;
		}
		else if(connections.size() > rooms.length / 2 * 3){
			verify = true;
		}
		else{
			//verify = false;
			//randomDoubleValue += 5;
			
			maxConnectivity = maxConnectivity();
			if(!correctConnectivity()){
				verify = true;
			}
		}
		System.out.println("Value: "+verify+" "+connections.size());
		return verify;
	}
	
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			 createWorld();
		 }
		
		batch.begin();
		
		
		for(int i=0; i<rooms.length; i++){
			for( int xx=rooms[i].getLeft(); xx <= rooms[i].getRight(); xx++){
				for (int yy=rooms[i].getBottom(); yy <= rooms[i].getTop(); yy++){
					batch.draw(shade[rooms[i].getConnectivity() * 8 / (maxConnectivity+1)], xx*8, yy*8);
				}
			}
		}
		
		
		for (int i=0; i< grid.length; i++){
			for (int j=0; j < grid[0].length; j++){
				if(grid[i][j] == '.'){
					batch.draw(black, i*8, j*8);
				}
				else if(grid[i][j] == 'X'){
					batch.draw(white, i*8, j*8);
				}
				else if(grid[i][j] == 'D'){
					batch.draw(blue, i*8, j*8);
				}
				/*
				else{
					batch.draw(red, i*8, j*8);
				}
				*/
			}
		}
		
		//System.out.println(connections.size());
		batch.end();
	}
	
	
}
