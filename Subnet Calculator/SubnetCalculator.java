import java.util.Scanner;

/**
 * Calculates the subnet mask and all of the related information for a given ip address
 * @author Olivier Lauzon
 */
public class SubnetCalculator {
	/**
	 * Contains a raw int value of the IP address
	 */
	public int rawIP;
	/**
	 * Contains a raw int value of the subnet mask
	 */
	public int rawSubmask;
	/**
	 * The raw int value of the network address
	 */
	public int rawNetAddress;
	/**
	 * The raw int value of the class address
	 */
	public int rawClassAddress;
	/**
	 * The amount of subnet bits
	 */
	public int subBits;
	/**
	 * The amount of host bits
	 */
	public int hostBits;
	/**
	 * The amount of bits owned by the class
	 */
	public int classBits;
	/**
	 * The amount of bits borrowed
	 */
	public int borrowedBits;
	/**
	 * Used to query for user input
	 */
	private static Scanner input = new Scanner(System.in);
	
	/**
	 * Constructor that creates the raw ip and submask values
	 * @param ipAddress The IP address to be calculated
	 */
	public SubnetCalculator() throws IllegalArgumentException, Exception {
		System.out.println("Please enter the address you are calculating");
		String userInput = input.next();
		
		rawIP = 0;
		rawSubmask = 0;
		//Creating the rawIP value from the user input
		String[] holder =  userInput.split("[/]");
		String[] ipPortion =  holder[0].split("[.]");
		if(holder.length != 2) {
			throw new Exception();
		}
		if(ipPortion.length != 4) {
			throw new Exception();
		}
		
		int CIDR = Integer.parseInt(holder[1]);
		if(CIDR < 0 || CIDR > 30) {
			throw new Exception();
		}
		
		int bitShift = 24;
		for(String s : ipPortion) {
			int octetValue = Integer.parseInt(s);
			if(octetValue > 255 || octetValue < 0) {
				throw new Exception();
			}
			if(octetValue == 127) {
				throw new IllegalArgumentException();
			}

			rawIP += octetValue << bitShift;
			bitShift -= 8;

		}

		//Creating the rawSubmask value
		rawSubmask = 0xffffffff;
		rawSubmask = rawSubmask << (32 - CIDR);

		//Creating the raw Network address value
		rawNetAddress = rawIP & rawSubmask;
		bits();
	}

	/**
	 * Takes a "raw" value and converts it to proper ip format
	 * @param numIP The raw value
	 * @return The ip format
	 */
	public String dotIP(Integer numIP) {
		StringBuffer ipFormat = new StringBuffer(15);

		for (int i = 24; i > 0; i -= 8) {
			ipFormat.append(Integer.toString((numIP >>> i) & 0xff));
			ipFormat.append('.');
		}
		ipFormat.append(Integer.toString(numIP & 0xff));

		return ipFormat.toString();
	}

	/**
	 * Gets the value of the subnet mask
	 * @return The subnet mask
	 */
	public String getSubnet() {
		return dotIP(rawSubmask);
	}

	/**
	 * Gets the value of the ip address inputed
	 * @return The ip address inputed
	 */
	public String getIPAddress() {
		return dotIP(rawIP);
	}

	/**
	 * Gets the value of the subnet ID
	 * @return The subnet ID
	 */
	public String subnetID() {
		rawNetAddress = rawIP & rawSubmask;
		return dotIP(rawNetAddress);
	}

	/**
	 * Gets the value of the broadcast address
	 * @return The broadcast address
	 */
	public String broadcastAddress() {
		Integer tempNum = (int) (rawNetAddress + usableHosts() + 1);
		return dotIP(tempNum);
	}

	/**
	 * Gets the value of the first host ip
	 * @return The first host ip
	 */
	public String firstHostIP() {
		Integer tempNum = (int) (rawNetAddress + 1);
		return dotIP(tempNum);
	}

	/**
	 * Gets the value of the last host ip
	 * @return The last host ip
	 */
	public String lastHostIP() {
		Integer tempNum = (int) (rawNetAddress + usableHosts());
		return dotIP(tempNum);
	}

	/**
	 * Gets the value of the class ip address
	 * @return The class ip address
	 */
	public String ipClass() { 
		int temp = (rawIP >>> 24);
		String cls = null;
		if(temp <= 126) {
			cls = "A";
			classBits = 8;
			rawClassAddress = rawNetAddress & 0xff000000;
		}
		else if(temp >= 128 && temp <= 191) {
			cls = "B";
			classBits = 16;
			rawClassAddress = rawNetAddress & 0xffff0000;
		}
		else if(temp > 191) {
			cls = "C";
			classBits = 24;
			rawClassAddress = rawNetAddress & 0xffffff00;
		}
		return cls;
	}
	
	/**
	 * Gets the amount of usable hosts
	 * @return The amount of usable hosts
	 */
	public int usableHosts() {
		return (int) (Math.pow(2, hostBits) -2);
	}

	/**
	 * Gets the amount of subnets
	 * @return The amount of subnets
	 */
	public int numberOfSubnets() {
		return (int) (Math.pow(2, borrowedBits));
	}
	
	/**
	 * Calculates the values for all the types of bits (subBits, hostBits, classBits, borrowedBits)
	 */
	public void bits() {
		for (int i = 0; i < 32; i++) {

			if ((rawSubmask << i) == 0) {
				subBits = i;
				hostBits = 32 - subBits;
				ipClass();
				borrowedBits = subBits - classBits;
				break;
			}	
		}
	}
}