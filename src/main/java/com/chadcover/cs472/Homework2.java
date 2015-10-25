package com.chadcover.cs472;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Homework2 {
	
	public static void main(String[] args) throws IOException {
		
		// initialize the main memory
		short[] mainMemory = new short[2048];
	
		int counter = 0;		
		for (int i = 0; i < 0x08; i++)
			for (short j = 0; j <= 0xFF; j++ )
			{
				// System.out.printf("%02X\n",j);
				mainMemory[counter] = j;
				counter++;
			}
			
		// initialize the cache
		Cache[] cacheSimulator = new Cache[16];
		for (int i = 0; i < 16; i++) { 
			cacheSimulator[i] = new Cache(i);
		}
		
		try {
            // homework input file 
			File hw_input = new File("C:\\Users\\ccover\\Documents\\BU\\CS472\\cs472\\src\\main\\resources\\homework2-input.txt");
            // test input file
            File hw_test_input = new File("C:\\Users\\ccover\\Documents\\BU\\CS472\\cs472\\src\\test\\resources\\homework2-test-input.txt");
            
            //  Get data from file using a file reader. 
            FileReader fr = new FileReader(hw_test_input);
            // Store contents read via Buffered File Reader
            BufferedReader br = new BufferedReader(fr);                                                 
            // Read br and store a line in 'entry'
            String entry;
            while((entry = br.readLine()) != null) 
            {                                   
    			entry = entry.toLowerCase();
    			menu(entry,cacheSimulator,mainMemory,br);
    		}
            
        } catch(IOException e) {
        	File error = new File("C:\\Temp\\homework2-error.log");
    		PrintWriter error_log = new PrintWriter(new FileOutputStream(error,true));
            error_log.append("There was an IO error.\n");
            error_log.close();
        }
}
	
	public static void menu(String choice, Cache[] cacheSimulator, short[] memoryArray, BufferedReader br) throws IOException {
	
		short[] tempBlock = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			
		// homework output file
	    File hw_output = new File("C:\\Users\\ccover\\Documents\\BU\\CS472\\cs472\\src\\main\\resources\\homework2-output.txt");
	    // PrintWriter hw = new PrintWriter(new FileOutputStream(hw_output,true));
	    
	    // test output file
	    File hw_test_output = new File("C:\\Users\\ccover\\Documents\\BU\\CS472\\cs472\\src\\test\\resources\\homework2-test-output.txt");
	    PrintWriter hw = new PrintWriter(new FileOutputStream(hw_test_output,true));
				
		// Display the cache.	
		if (choice.equals("d")) { 
		
			displayCache(cacheSimulator,hw);
			
		// Read byte in cache.
		} else if (choice.equals("r")) { 
		
			// System.out.println("What address would you like to read?");
			Scanner readInput = new Scanner(System.in);
			String hexString = ("0x" + br.readLine());
			// String hexString = ("0x" + readInput.nextLine());
			int[] addressInfo = getAddressInfo(hexString);

			int startBlock = addressInfo[0];
			int tag = addressInfo[1];
			int slot = addressInfo[2];
			int offset = addressInfo[3];
			
			int validBit = cacheSimulator[slot].getValidBit();
			int currentTag = cacheSimulator[slot].getTagNumber();
			
			// if cache hit
			if (validBit == 1 && tag == currentTag) {
				short address = cacheSimulator[slot].getBlockContents(offset);
				String cacheHit = String.format("At that byte there is the value %02X (Cache Hit).\n",address);
				hw.append(cacheHit);
				
		    // else cache miss
			} else {
			
				for (int i = 0; i <= 0xF; i++) {
					tempBlock[i] =  memoryArray[startBlock];
					cacheSimulator[slot].setBlockContents(tempBlock[i],i);
					startBlock++;
			}
				cacheSimulator[slot].setValidBit();
				cacheSimulator[slot].setTagNumber(tag);
				short address = cacheSimulator[slot].getBlockContents(offset);
				String cacheMiss = String.format("At that byte there is the value %02X (Cache Miss).\n",address);
				hw.append(cacheMiss);
			}
			
			
			// Write byte in cache.
		} else { 
			String hexString = ("0x" + br.readLine());
			int[] addressInfo = getAddressInfo(hexString);
			int memAddress = Integer.decode(hexString);
			int tag = addressInfo[1];
			int slot = addressInfo[2];
			int offset = addressInfo[3];
			
			int validBit = cacheSimulator[slot].getValidBit();
			int currentTag = cacheSimulator[slot].getTagNumber();
			
			short blockValue =  memoryArray[memAddress];
			cacheSimulator[slot].setBlockContents(blockValue,offset);
			
			String blockContentStr = ("0x" + br.readLine());
			int newBlockContent = Integer.decode(blockContentStr);
			cacheSimulator[slot].setBlockContent(offset, newBlockContent);
			short newValue = cacheSimulator[slot].getBlockContents(offset);
			memoryArray[memAddress] = (short)newBlockContent;
			
			if (validBit == 1 && tag == currentTag) {
				String cacheHit = String.format("Value %2X has been written to address 0x%06X. (Cache Hit)",newValue,memAddress);
				hw.append(cacheHit);
				String newLine = "\n";
				hw.append(newLine);
		    // else cache miss
			} else {
				String cacheMiss = String.format("Value %2X has been written to address 0x%06X. (Cache Miss)",newValue,memAddress);
				hw.append(cacheMiss);
				String newLine = "\n";
				hw.append(newLine);
			}
		}		
		hw.close();
	}
	
	
	/**
	 * Returns the starting block address, tag, slot, and offset of 
	 * the given address.	
	 * 
	 * @param  hexString  A stringified version of the address.
	 * @return An integer array {tag & slot, tag, slot, offset}
	 * 
	 */
	
	
	private static int[] getAddressInfo(String hexString) {
		int hexAddress = Integer.decode(hexString);
		
		int tag = ((hexAddress & 0xFFFFFF00) >>> 8);
		String tagStr = Integer.toHexString(tag);
		
		int slot = ((hexAddress & 0x000000F0) >>> 4);
		String slotStr = Integer.toHexString(slot);
		
		int offset = (hexAddress & 0x0000000F);
		
        // Combine tag & slot values
		String startBlockStr = ("0x" + tagStr.concat(slotStr) + "0");
		int startBlock = Integer.decode(startBlockStr);
		
		// create the int array
		int[] addressInfo = {startBlock,tag,slot,offset};
		return addressInfo;
	}

	/**
	 * Displays the cache
	 * 	
	 * @param  cache  A Cache[] containing data
	 * 
	 */
	
	
	private static void displayCache(Cache[] cache, PrintWriter pw) {
		String slot = "Slot",
				   valid = "Valid",
				   tag = "Tag",
				   data = "Data";
			
		
			String slotHeader = String.format("%8s",slot);
			String validHeader = String.format("%8s",valid);
			String tagHeader = String.format("%9s",tag);
			String dataHeader = String.format("%14s",data);
			String newLine = "\n";
			
			pw.append(slotHeader);
			pw.append(validHeader);
			pw.append(tagHeader);
			pw.append(dataHeader);
			pw.append(newLine);
						
			for (int i = 0; i <= 0xF; i++) {
		
				String slotNumber = String.format("%5s %01X"," ",cache[i].getSlotNumber());
				String validBit = String.format("%5s %01X"," ",cache[i].getValidBit());
				String tagNumber = String.format("%7s %01X"," ",cache[i].getTagNumber());
				String spacer = String.format("%11s"," ");
				
				pw.append(slotNumber);
				pw.append(validBit);
				pw.append(tagNumber);
				pw.append(spacer);
				
				for (int j = 0; j < cache[i].getBlockContents().length; j++) {
					short[] blockContent = cache[i].getBlockContents();
					String blockData = String.format("%02X   ",blockContent[j]);
					pw.append(blockData);
				}
				pw.append(newLine);
			}
	}
	
	
}
