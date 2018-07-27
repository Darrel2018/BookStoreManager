
public class input_handler {
	
	// This function handles the if/else block from the interface
	private boolean interface_input(String useri) {
		int value = 0;
		
		try {
			Integer Snum = Integer.valueOf(useri);
			value = Snum;
		}catch(NumberFormatException e) {
			System.out.println("\n----ERROR: user did not enter a number.----\n");
			return false;
		}
		
		if(value == 1) {
			return true;
		}
		else if(value == 2) {
			return true;
		}
		else if(value == 3) {
			return true;
		}
		else if(value == 4) {
			return true;
		}
		else if(value == 0) {
			return true;
		}
		else {
			System.out.println("\n----ERROR: Invaild input----\n");
			return false;
		}
	}
	
	// This function allows access to all the functions in this file
	public boolean input_type(String type, String useri) {
		if(type == "interface") {
			boolean correct_input = false;
			correct_input = interface_input(useri);
			return correct_input;
		}
		
		return false;
	}
}
