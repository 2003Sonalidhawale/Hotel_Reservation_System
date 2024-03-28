import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.*;
import java.sql.Statement;
import java.sql.ResultSet;

public class HotelReservationSystem {		
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db","root","sonali30");
			while(true) {
				System.out.println("");
				System.out.println("HOTEL MANAGEMENT SYSTEM");
				Scanner scanner = new Scanner(System.in);
				System.out.println("1. Reserve a room \n2. View Reservation \n3.Get Room Number \n4.Update Reservations \n5. Delete Reservations \n0.Exit");
				System.out.println("Choose an option: ");
				int choice = scanner.nextInt();
				switch(choice) {
				case 1:
					reserveRoom(connection , scanner);
					break;
					
				case 2:
					viewReservations(connection);
					break;
					
				case 3:
					getRoomNumber(connection, scanner);
					break;
					
				case 4:
					updateReservation(connection, scanner);
					break;
					
				case 5:
					deleteReservation(connection,scanner);
					break;
					
				case 0:
					exit();
					//reservationExists(connection,scanner);
					scanner.close();
					return;
				default:
					System.out.println("Invalid choice. Try again.");
				}
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
		}
	
	}
	
	private static void reserveRoom(Connection connection, Scanner scanner) {
		try {
			System.out.println("Enter guest name: ");
			String guestName = scanner.next();
			System.out.println("Enter room Number: ");
			int roomNumber = scanner.nextInt();
			System.out.println("Enter contact number: ");
			String contactNumber = scanner.next();

			String sql = "INSERT INTO reservations (guest_name, room_number, conatact_number)" + "VALUES('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

			try (Statement statement = connection.createStatement()){
				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0 ) {
					System.out.println("Reservation Successful!");
				}else{
					System.out.println("Reservation failed.");
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	private static void viewReservations(Connection connection) throws SQLException{
		String sql = "SELECT reservation_id, guest_name, room_number, room_number, reservation_date FROM reservations";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){

			System.out.println("Current Reservation: ");
			System.out.println("+-----------------+---------------------|----------------+-----------------+----------------------+");
			System.out.println("| Reservation ID  | Guest               |  Room NUmber   | Contact Number  | Reservation Date     |");
			System.out.println("+-----------------+---------------------|----------------+-----------------+----------------------+");
			
			while (resultSet.next()) {
				int reservationId = resultSet.getInt("reservation_id");
				String guestName = resultSet.getString("guest_name");
				int roomNumber = resultSet.getInt("room_number");
				String contactNumber = resultSet.getString("room_number");
				String reservationDate = resultSet.getString("reservation_date").toString();

				System.out.printf("| %-17d | %-21s | %-16d | %-17s | %-22s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
			}

			System.out.println("+-----------------+---------------------|----------------+-----------------+----------------------+");

		}
	}

	private static void getRoomNumber(Connection connection, Scanner scanner){
		try{
			System.out.println("Enter Reservation Id:");
			int reservationId = scanner.nextInt();
			System.out.println("Enter guest Name: ");
			String guestName = scanner.next();

			String sql = "SELECT room_number FROM reservations" + "WHERE reservation_id = " + reservationId + "AND guest_name = '" + guestName + "'";

			try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){

				if (resultSet.next()) {
					
					int roomNumber = resultSet.getInt("room_number");
					System.out.println("Room number for Reservation Id " + reservationId + "and Guest " + guestName + "is: " + roomNumber);
				}else{
					System.out.println("Reservation not found for the given ID and guest Name.");
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	private static void updateReservation(Connection connection, Scanner scanner){
		try {
			System.out.println("Enter reservation ID to update: ");
			int reservationId = scanner.nextInt();
			scanner.nextLine();

			if (!reservationExists(connection, reservationId)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}

			System.out.println("Enter new guest name: ");
			String newguestName = scanner.nextLine();
			System.out.println("Enter new room Number: ");
			int newroomNumber = scanner.nextInt();
			System.out.println("Enter new contact number: ");
			String newcontactNumber = scanner.next();

			String sql = "UPDATE reservations SET guest_name = '" + newguestName + "', " + "room_number = " + newroomNumber + ", " + " contact_number = '" + newcontactNumber  + "'  " + "WHERE reservation_id = " + reservationId;

			try(Statement statement = connection.createStatement()){
				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) {
					System.out.println("Reservation updated Successfully!");
				}else{
					System.out.println("Reservation update failed.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteReservation(Connection connection, Scanner scanner){
		try {
			System.out.println("Enter reservation ID to delete: ");
			int reservationId = scanner.nextInt();

			if (!reservationExists(connection, reservationId)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}

			String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

			try(Statement statement = connection.createStatement()){
				int affectedRows = statement.executeUpdate(sql);

				if (affectedRows > 0) {
					System.out.println("Reservation deleted Successfully!");
				}else{
					System.out.println("Reservation deletion failed.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean reservationExists(Connection connection,int  reservationId){
		try {
			String sql = "SELECT reservstion_id FROM reservations WHERE reservation_id = " + reservationId;

			try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){

				return resultSet.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void exit() throws InterruptedException{
		System.out.println("Exiting System");
		int i=5;
		while (i!=0) {
			System.out.println(".");
			Thread.sleep(450);
			i--;
	}
	System.out.println();
		System.out.println("Thank You For Using Hotel Reservation System!!!");
	}

}
