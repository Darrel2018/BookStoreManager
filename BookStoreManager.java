import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

// This file is the application entry point.
public class BookStoreManager {
	
	public static void main(String[] args) {
		Books_funtions book = new Books_funtions();
		Scanner input = new Scanner(System.in);
		boolean running = true;
		String user_n, user_p;
		
		System.out.print("Hi, welcome to the BookStoreManager program.\nPlease enter your username\n==>");
		user_n = input.nextLine();
		
		System.out.print("Please enter your password\n==>");
		user_p = input.nextLine();
		
		try(
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", user_n, user_p);
		){
		}catch(SQLException ex) {
			System.out.println("ERROR: username or password is incorrect...");
			running = false;
			System.out.print("Shutting Down(yes):");
			input.nextLine();
			input.close();
		}
		
		while(running == true) {
			running = book.main_interface(user_n, user_p);
		}
		input.close();
	}
}
