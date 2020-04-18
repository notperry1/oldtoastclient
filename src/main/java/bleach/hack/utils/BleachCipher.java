package bleach.hack.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oshi.SystemInfo;

public class BleachCipher {

	private Range range = new Range((int)(getProcessorID() % 0xFFFF), (int)(getProcessorID() % 0xFFFF) + 0x0FFF);
	private Random rand = new Random(6942013376969L);
	
	public String encrypt(String string) {
		try {
			List<Integer> salt = new ArrayList<>();
			
			String output = "";
			
			for(Character c: string.toCharArray()) {
				int count = rand.nextInt(new Random().nextInt(50)+50);
				salt.add(count);
				
				for(int i = 0; i < count; i++) {
					output += intToString(range.ints.get(new Random().nextInt(range.ints.size())));
				}
				
				output += intToString(range.min + (int)c);
			}
			
			for(Integer i: salt) output += intToString(range.min + i);
			output += intToString(range.min + salt.size());
			
			return output;
		}catch(Exception e) {return string;}
	}
	
	public String decrypt(String string) {
		try {
			int size = (int) string.charAt(string.length() - 1) - range.min;
			List<Integer> salt = new ArrayList<>();
			
			/* Anti-salt */
			String newString = string;
			for(int i = 0; i < size; i++) {
				salt.add((int) string.charAt(string.length() - 1 - size + i) - range.min);
				newString = newString.replace(newString.substring(i, i+salt.get(i)), "");
			}
			newString = newString.substring(0, newString.length() - 1 - size);
			
			/* Recreate string from the remaining chars */
			String finalString = "";
			for(Character c: newString.toCharArray()) {
				finalString += intToString((int)c - range.min);
			}
			
			return finalString;
		}catch(Exception e) {return string;}
	}
	
	private String intToString(int i) { return String.copyValueOf(Character.toChars(i)); }
	
	public long getProcessorID() {
		String id = new SystemInfo().getHardware().getProcessors()[0].getIdentifier();
		String nums = "";
		for(Character c: id.toCharArray()) nums += (int)c;
		if(nums.length() > 18) nums = nums.substring(0, 18);
		return Long.parseLong(nums);
	}
}

class Range {
	public int min;
	public int max;
	
	public List<Integer> ints = new ArrayList<>();
	
	public Range(int min, int max) {
		this.min = min;
		this.max = max;
		for(int i = min; i < max; i++) ints.add(i);
	}
}
