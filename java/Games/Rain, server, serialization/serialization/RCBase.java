package com.thecherno.raincloud.serialization;

public class RCBase {
	

	protected short nameLenght;
	protected byte[] name;
	protected int size = 2 + 4;
	
	public String getName(){
		return new String(name, 0, nameLenght);
	}
	
	public void setName(String name){
		assert(name.length() < Short.MAX_VALUE);
		
		if(this.name != null)
			size -= this.name.length;
		
		nameLenght = (short) name.length();
		this.name = name.getBytes();
		size += nameLenght;
	}

}
