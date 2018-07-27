import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class Books_funtions {
	
	// This function will tell the database what book to look for.
	private void search_books(String user_n, String user_p) {
		
		try(
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", user_n, user_p);
			
			Statement stmt = conn.createStatement();
		){
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String Selected_book = "", strSelect_book = "";
			int Selected_book_id = 0, record_count = 0;
			boolean isid = false, search_all = false;
			
			System.out.print("Type a specific book name, id or type 'all' to bring up the entire database.\n==>");
			Selected_book = input.nextLine();
			
			// a try/catch block to see if the user entered a number or a word
			try {
				Integer Snum = Integer.valueOf(Selected_book);
				Selected_book_id = Snum;
				isid = true;
			}catch(NumberFormatException e) {
				isid = false;
			}
			
			if(Selected_book.equals("all") || Selected_book.equals("All")) {
				strSelect_book = "Select * from books";
				search_all = true;
			}
			
			if(isid == false) {
				if(search_all == false) {
					Selected_book = "'" + Selected_book + "'";
					strSelect_book = "Select * from books where Title = " + Selected_book;
					System.out.println("Searching for book named: " + Selected_book);
				}

				ResultSet rset = stmt.executeQuery(strSelect_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book + "\n====");
				}
				else {
					while(rset.next()) {
						int id = rset.getInt("id");
						String Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
						
						record_count++;
					}
					System.out.println(record_count + " record(s) found..");
				}
			}
			else {
				strSelect_book = "Select * from books where id = " + Selected_book_id;
				System.out.println("Searching for book id: " + Selected_book_id);
				
				ResultSet rset = stmt.executeQuery(strSelect_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book_id + "\n====");
				}
				else {
					while(rset.next()) {
						int id = rset.getInt("id");
						String Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
					}
					System.out.println("1 record found:");
				}
			}
			
			System.out.print("Continue?(yes):");
			input.hasNext();
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// This function will tell the database to enter a new record.
	private void enter_books(String user_n, String user_p) {
		
		try(
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", user_n, user_p);
				
			Statement stmt = conn.createStatement();
		){
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			int id = 0, Qty = 0;
			String Title = "", Author = "";
			boolean empty_String = false, unique_test = true;
			
			System.out.println("To enter a new record into the database you must enter 4 values...");
			
			// a try/catch block to stop user input mismatch. 
			try {
				System.out.print("id: ");
				id = input.nextInt();
				
				input.nextLine();
				System.out.print("\nTitle: ");
				Title = input.nextLine();
				
				System.out.print("\nAuthor: ");
				Author = input.nextLine();
				
				System.out.print("\nQuantity: ");
				Qty = input.nextInt();
				
				// two if statements to make sure the Title and the Author cannot be null
				if(Title.length() == 0) {
					System.out.println("ERROR: Title needs at least one letter...");
					empty_String = true;
				}
				if(Author.length() == 0) {
					System.out.println("ERROR: Title needs at least one letter...");
					empty_String = true;
				}
				
				String str_database_check = "Select * from books"; 
				ResultSet rset = stmt.executeQuery(str_database_check);
				
				// a while loop to check if id or a Title is the same in the database.
				while(rset.next()) {
					int test_id = rset.getInt("id");
					String test_Title = rset.getString("Title");
					test_Title = test_Title.toLowerCase();
					
					if(test_id == id) {
						unique_test = false;
						System.out.println("ERROR: another id with same value in database.");
					}
					if(test_Title.equals(Title.toLowerCase())) {
						System.out.println("ERROR: another Title with same name in database.");
						unique_test = false;
					}
				}
				
				// an if statement to stop info being enter into the database if the data is invalid or not unique.
				if(empty_String == false && unique_test == true) {
					String sqlinsert = "insert into books values" +
							"(" + id + ", '" + Title + "', '" + Author + "', " + Qty + ")";
					
					stmt.executeUpdate(sqlinsert);
					System.out.println("1 record added.");
				}
				
				System.out.print("Continue?(yes):");
				input.hasNext();
			}catch(InputMismatchException e) {
				System.out.println("ERROR: Invalid input..");
			}
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// This function will tell the database to update an existing record.
	private void update_books(String user_n, String user_p) {
		
		try(
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", user_n, user_p);
					
			Statement stmt = conn.createStatement();
		){
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String Selected_book = "", strSelect_book = "", strUpdate_book = "", Title = "", new_Title = "", new_Author = "";
			int Selected_book_id = 0, update_choice = 0, new_id = 0, new_Qty = 0;
			boolean isid = false, no_record = false, unique_test = true;
			
			System.out.print("To update an existing book please type the books name or id\n==>");
			Selected_book = input.nextLine();
			
			// a try/catch block to see if the user entered a number or a word
			try {
				Integer Snum = Integer.valueOf(Selected_book);
				Selected_book_id = Snum;
				isid = true;
			}catch(NumberFormatException e) {
				isid = false;
			}
			
			if(isid == false) {
				Selected_book = "'" + Selected_book + "'";
				strSelect_book = "Select * from books where Title = " + Selected_book;
				System.out.println("Searching for book named: " + Selected_book);

				ResultSet rset = stmt.executeQuery(strSelect_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book + "\n====");
					no_record = true;
				}
				else {
					System.out.println("1 record found:");
					while(rset.next()) {
						int id = rset.getInt("id");
						Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
					}
				}
			}
			else {
				strSelect_book = "Select * from books where id = " + Selected_book_id;
				System.out.println("Searching for book id: " + Selected_book_id);
				
				ResultSet rset = stmt.executeQuery(strSelect_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book_id + "\n====");
					no_record = true;
				}
				else {
					System.out.println("1 record found:");
					while(rset.next()) {
						int id = rset.getInt("id");
						Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
					}
				}
			}
			
			// an if statement to see if the record is in the database
			if(no_record == false) {
				
				// getting input from the user and then processing the request
				try {
					System.out.print("What do you want to update?\n<=1. id=>\n<=2. Title=>\n<=3. Author=>\n<=4. Quantity=>\n==>");
					update_choice = input.nextInt();
				}catch(InputMismatchException e) {
					System.out.println("ERROR: Invalid input..");
				}
				
				if(update_choice == 1) {
					try {
						System.out.print("Please enter new id: ");
						new_id = input.nextInt();
						strUpdate_book = "update books set id = " + new_id + " where Title = '" + Title + "'";
					}catch(InputMismatchException e) {
						System.out.println("ERROR: Invalid input..");
					}
				}
				else if(update_choice == 2) {
					input.nextLine();
					System.out.print("Please enter new Title: ");
					new_Title = input.nextLine();
					strUpdate_book = "update books set Title = '" + new_Title + "' where Title = '" + Title + "'";
				}
				else if(update_choice == 3) {
					input.nextLine();
					System.out.print("Please enter new Author: ");
					new_Author = input.nextLine();
					strUpdate_book = "update books set Author = '" + new_Author + "' where Title = '" + Title + "'";
				}
				else if(update_choice == 4) {
					try {
						System.out.print("Please enter new Quantity: ");
						new_Qty = input.nextInt();
						strUpdate_book = "update books set Qty = " + new_Qty + " where Title = '" + Title + "'";
					}catch(InputMismatchException e) {
						System.out.println("ERROR: Invalid input..");
					}
				}
				else {
					System.out.println("ERROR: Wrong input..");
				}
				
				// added this so users cannot add values that copy other values in the database
				strSelect_book = "Select * from books";
				ResultSet rset = stmt.executeQuery(strSelect_book);
				while(rset.next()) {
					int test_id = rset.getInt("id");
					String test_Title = rset.getString("Title");
					test_Title = test_Title.toLowerCase();
					
					if(test_id == new_id) {
						unique_test = false;
						System.out.println("ERROR: another id with same value in database.");
						break;
					}
					if(test_Title.equals(new_Title.toLowerCase())) {
						System.out.println("ERROR: another Title with same name in database.");
						unique_test = false;
						break;
					}
				}
				
				if(unique_test == true) {
					stmt.executeUpdate(strUpdate_book);
					System.out.println("1 record updated...");
				}
			}
			System.out.print("Continue?(yes):");
			input.hasNext();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	
	// This function will tell the database to delete a record.
	private void delete_books(String user_n, String user_p) {
		
		try(
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", user_n, user_p);
						
			Statement stmt = conn.createStatement();
		){
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String Selected_book = "", strSelected_book = "", Title = "", user_c = "";
			int Selected_book_id = 0;
			boolean isid = false, no_record = false, user_delete = false;
			
			System.out.print("To delete a record please type the id or the Title of the record\n==>");
			Selected_book = input.nextLine();
			
			// a try/catch block to see if the user entered a number or a word
			try {
				Integer Snum = Integer.valueOf(Selected_book);
				Selected_book_id = Snum;
				isid = true;
			}catch(NumberFormatException e) {
				isid = false;
			}
			
			if(isid == false) {
				Selected_book = "'" + Selected_book + "'";
				strSelected_book = "Select * from books where Title = " + Selected_book;
				System.out.println("Searching for book named: " + Selected_book);
				
				ResultSet rset = stmt.executeQuery(strSelected_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book + "\n====");
					no_record = true;
				}
				else {
					System.out.println("1 record found:");
					while(rset.next()) {
						int id = rset.getInt("id");
						Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
					}
				}
			}
			else {
				strSelected_book = "Select * from books where id = " + Selected_book_id;
				System.out.println("Searching for book id: " + Selected_book_id);
				
				ResultSet rset = stmt.executeQuery(strSelected_book);
				
				// if else statement to see if a record exists in the data base.
				if(rset.isBeforeFirst() == false) {
					System.out.println("====\nNo records found for " + Selected_book_id + "\n====");
					no_record = true;
				}
				else {
					System.out.println("1 record found:");
					while(rset.next()) {
						int id = rset.getInt("id");
						Title = rset.getString("Title");
						String Author = rset.getString("Author");
						int Qty = rset.getInt("Qty");
					
						System.out.println("<---->\nid: " + id + "\n" + "Title: " + Title + "\n" 
											+ "Author: " + Author + "\n" + "Qty: " + Qty + "\n<---->\n");
					}
				}
			}
			
			if(no_record == false) {
				System.out.print("Are you sure you want to delete this record?(yes/no)\n==>");
				user_c = input.nextLine();
				
				if(user_c.equals("yes") || user_c.equals("Yes")) {
					user_delete = true;
				}
				
				if(user_delete == true) {
					strSelected_book = "delete from books where Title = '" + Title + "'";
					stmt.executeUpdate(strSelected_book);
					System.out.println("1 record deleted");
				}
				else {
					System.out.println("Action canceled...");
				}
			}
			
			System.out.print("Continue?(yes):");
			input.hasNext();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// This function handles the request of the user
	private void request_handler(int request_num, String user_n, String user_p) {
		Books_funtions request = new Books_funtions();
		
		if(request_num == 1) {
			request.search_books(user_n, user_p);
		}
		else if(request_num == 2) {
			request.enter_books(user_n, user_p);
		}
		else if(request_num == 3) {
			request.update_books(user_n, user_p);
		}
		else {
			request.delete_books(user_n, user_p);
		}
	}
	
	// This function is the interface.
	public boolean main_interface(String user_n, String user_p) {
		Scanner input = new Scanner(System.in);
		input_handler user_input = new input_handler();
		boolean test_input = false;
		String useri = "";
		int tested_number = 0;
		
		while(test_input == false) {
			System.out.print("<============================>\nWelcome to the book interface!\nWhat action do you want to attempt?"
					+ "\n\nChoose a number\n<=1. Search for books=>\n<=2. Enter new books=>\n"
					+ "<=3. Update books=>\n<=4. Delete books=>\n<=0. Exit=>\n==>");
			useri = input.nextLine();
		
			test_input = user_input.input_type("interface", useri);
		}
		
		try {
			Integer Rnum = Integer.valueOf(useri);
			tested_number = Rnum;
		}catch(NumberFormatException e) {
			input.close();
			System.out.println("FATAL ERROR..");
		}
		
		if(tested_number != 0) {
			request_handler(tested_number, user_n, user_p);
		}
		else {
			input.close();
			return false;
		}
		
		return true;
	}
}
