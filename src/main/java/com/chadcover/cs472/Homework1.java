/**
 * 
 */
package com.chadcover.cs472;


/**
 * @author Chad David Cover
 * CS472: Homework 1
 * October 1, 2015
 */
public class Homework1 {
	
	public static void main(String[] args) {
		
		// test cases 
		// x00A63820 -> add $7,$5,$6
		// x8E5B0004 -> lw $27,4($18)
		// xAEDDFFFC -> sw $29,-4,($22)
		int[] instructions_test = {0x00A63820, 0x8E5B0004,0xAEDDFFFC};
		// test cases: instructions from assignment all jumbled up
		int[] instructions_jumbled = {0x158FFFF6,0x8E59FFF0,0x022DA822,0x8EF30018,0xAD930018,0x02697824,0xAD8FFFF4,
				0x018C6020,0x02A4A825,0x12A70004,0x02689820};
		// keep track of the order of jumbled instructions
		int[] instructions_order = {10,11,1,2,5,6,7,8,9,3,4};
		
		
		// instructions from assignment 1 - MIPS Disassembler
		int[] instructions = {0x022DA822,0x8EF30018,0x12A70004,0x02689820,0xAD930018,0x02697824,0xAD8FFFF4,
				0x018C6020,0x02A4A825,0x158FFFF6,0x8E59FFF0};
				
		
		// bitmasks and shifts
		int op_code_bitmask = 0xFC000000,
            op_code_shift = 26,
			rs_bitmask = 0x03E00000,
            rs_shift = 21,
            rt_bitmask = 0x001F0000,
            rt_shift = 16,
            rd_bitmask = 0x0000F800,
            rd_shift = 11,
            func_bitmask = 0x0000003F,
            offset_bitmask = 0x0000FFFF,
            instruction = 0x000000,
            address = 0x7A060;
		String func_name = "Empty";
			 
		
		for (int i = 0; i < instructions.length; i++) {
		
			instruction = instructions[i];
			String address_String = String.format("%05X", address);
            address_String.toUpperCase();
        
			// Get op code
			int instruction_op_code = (instruction & op_code_bitmask) >>> op_code_shift;
			String format = "x-format";
        
			// Determine format based upon op code value
			if (instruction_op_code == 0x00) {
				format = "r";
			} else {
				format = "i";
			}
			        
        
			// determine registers & offset values depending upon instruction format type
        
	        if (format == "r") {
	        	
	        	int func_code = (instruction & func_bitmask);
	        	if (func_code == 0x20) {
	        		func_name = "add";
	        	} else if (func_code == 0x24) {
	        		func_name = "add";
	        	} else if (func_code == 0x25) {
	        		func_name = "or";
	        	} else if (func_code == 0x2A) {
	        		func_name = "slt";
	        	} else  { // (func_code == 0x23)
	        		func_name = "sub";
	        	}
	        	
        		// Get value of register S
        		int rs_code = (instruction & rs_bitmask) >>> rs_shift;
            
        		// Get value of register T
        		int rt_code = (instruction & rt_bitmask) >>> rt_shift;
            
        		// Get value of register D
        		int rd_code = (instruction & rd_bitmask) >>> rd_shift;
            
        		// Print assembly instruction
        		// for testing // System.out.print("Instruction #" + instructions_order[i] + " - ");
        		System.out.println(address_String + "   " + func_name + " $" + rd_code + ", $" + rs_code + ", $" + rt_code);
        		
        
	        } else if (format == "i" && (instruction_op_code == 0x23 || instruction_op_code == 0x2B)) {
	        	
	        	if (instruction_op_code == 0x23) {
	        		func_name = "lw";
	        	} else  { // (instruction_op_code == 0x2B)
	        		func_name = "sw";
	        	} 
	        	
	    		// Get value of register S
	            int rs_code = (instruction & rs_bitmask) >>> rs_shift;
	            
	            // Get value of register T
	            int rt_code = (instruction & rt_bitmask) >>> rt_shift;
	    		
	            // Get value of offset
	            short offset = (short)(instruction & offset_bitmask);
	            
	            // print assembly instruction
	            // for testing // System.out.print("Instruction #" + instructions_order[i] + " - ");
	            System.out.println(address_String + "   " + func_name + " $" + rt_code + ", " + offset + "($" + rs_code + ")");
	    	
	        } else { // i-format but calculates pc-offset, i.e., bne or beq
	        	
	        	int func_code = (instruction & func_bitmask);
	        	if (instruction_op_code == 0x04) {
	        		func_name = "beq";
	        	} else  { // (func_code == 0x05)
	        		func_name = "bne";
	        	}
	    	
	    	
	        	// Get value of register S
        		int rs_code = (instruction & rs_bitmask) >>> rs_shift;
            
        		// Get value of register T
        		int rt_code = (instruction & rt_bitmask) >>> rt_shift;
        		
        		// Get compressed value of offset
        		int offset_int = (instruction & offset_bitmask);
        		// Decompress offset 
        		offset_int = offset_int << 2;
        		// Cast offset into short to handle signed ints
        		short offset = (short)(offset_int);
        		// Increment the address counter
        		int address_offset = 0x04;
        		// Calculate address to go to
        		int go_to = offset + address_offset + address;
        		
        		// for testing // System.out.print("Instruction #" + instructions_order[i] + " - ");
        		System.out.print(address_String + "   " + func_name + " $" + rt_code + ", $" + rs_code + " address: ");
        		System.out.printf("%05X",go_to);
        		System.out.println();
	        }
	        
	        // Go to next address
	        address += 0x04;
	        
	      }
        
	}

}
