package com.chadcover.cs472;


public class Cache {
	
	private int valid;
	private int slot;
	private int tag;
	private short[] block;
	
	public Cache(int slotNumber, int validBit, int tagNumber, short[] blockContents) {
		valid = validBit;
		slot = slotNumber;
		tag = tagNumber;
		block = blockContents;

	}
	
	public Cache() {
		valid = 0;
		slot = 0;
		tag = 0;
		short[] emptyBlock = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		block = emptyBlock;
	}
	
	
	public Cache(int slotNumber) {
		slot = slotNumber;
		valid = 0;
		tag = 0;
		short[] emptyBlock = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		block = emptyBlock;
	}
	
	public Cache(short[] blockContents) {
		valid = 0;
		tag = 0;
		block = blockContents;
	}
	
	public void setContents(int slotNumber, int validBit, int tagNumber, short[] blockContents) {
		this.valid = validBit;
		this.slot = slotNumber;
		this.tag = tagNumber;
		this.block = blockContents;
	}
	
	public Object[] getContents() {
		Object[] cache = {valid,slot,tag,block};
		return cache;
	}
	
	public int getValidBit() {
		return valid;
	}
	
	public void setValidBit() {
		valid = 1;
	}
	
	public void unsetValidBit() {
		valid = 0;
	}
	
	public int getSlotNumber() {
		return slot;
	}
	
	public void setSlotNumber(int slotNumber) {
		slot = slotNumber;
	}
	
	public int readSetNumber(int hexAddress) {
		int setNumber = ((hexAddress & 0x000000F0) >>> 4);
		return setNumber;
	}

	public int getTagNumber() {
		return tag;
	}
	
	public int readTagNumber(int hexAddress) {
		int tagNumber = ((hexAddress & 0xFFFFFF00) >>> 8);
		return tagNumber;
	}
	
	public void setTagNumber(int tagNumber) {
		tag = tagNumber;
	}
		
	public short getBlockContents(int index) {
		return block[index];
	}
	
	public short[] getBlockContents() {
		return block;
	}
	
	
	public void setBlockContents(short blockContents, int index) {
		block[index] = blockContents;
	}
	
	
	public void setBlockContent(int blockNumber, int blockValue) {
		block[blockNumber] = (short)blockValue;
	}
	
	public int readBlock(int hexAddress) {
		int blockOffset = (hexAddress & 0x0000000F);
		int blockContent = block[blockOffset];
		return blockContent;
	}
}
