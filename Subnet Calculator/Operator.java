
public class Operator {

	public static void main(String[] args) {
		int x;
		SubnetCalculator run = null;
		do {
			x = 1;
			try {
				run = new SubnetCalculator();
			}
			catch (IllegalArgumentException Ill){
				System.out.println("You have endered an invalid IP Address");
				x = 0;
			}
			
		} while(x == 0);
		
		
		System.out.println("\n***************************************************************************************");
		System.out.format("%-30s%-35s%-15s\n%-30s%-35s%-15s\n", "IP address: " + run.getIPAddress(), "Subnet mask: " + run.getSubnet(),
			"Number of Subnets: " + run.numberOfSubnets(), "IP class: " + run.ipClass(), "Subnet ID: " + run.subnetID(), "Number of hosts: " + run.usableHosts());
		System.out.println("***************************************************************************************\n");
		
		System.out.format("%-15s%-20s%-20s%-20s%-20s\n","Subnet #", "Subnet ID", "First Host IP", "Last Host IP", "BroadcastAddress");
		
		//Sets the rawIP variable to the rawClassAddress value so that we can iterate through all the subnets
		run.rawIP = run.rawClassAddress;
		
		for(int i = 0; i < run.numberOfSubnets(); i++) {
			System.out.format("%-15s%-20s%-20s%-20s%-20s\n", "#" + (i + 1), run.subnetID(), run.firstHostIP(), run.lastHostIP(), run.broadcastAddress());
			
			run.rawIP += (1 << run.hostBits);
		}
	}
}
