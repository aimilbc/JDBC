/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 *
 * @author Vanessa Montiel, Aimi Ross
 */
class JavaApplication3 {
    //  Database credentials
    static String DBNAME;
    
    //This is the specification for the printout 
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  
    //All of our output in this test are strings.
    // These formats are for the WritingGroups, Publishers, and Books
    static final String displayWGFormat="%-30s%-30s%-20s%-40s\n";
    static final String displayPubFormat="%-40s%-30s%-30s%-12s\n";
    static final String displayBookFormat="%-30s%-40s%-30s%-20s%-5s\n";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/CECS323_JDBC_Project";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int currentLine, menuOpt, yearFormed, listSub, listSubSub, insertSub, yPublished, numOfPages;
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            // Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            // Displaying the Main Menu and getting an integer between 1-3
                    // 1) List data
                    // 2) Insert/Update data
                    // 3) Remove book
                    // 4) Exit
            menuOpt = getMainOption();
            // Getting the current line number.
            currentLine = new Throwable().getStackTrace()[0].getLineNumber();
            
            // if menuOpt = 0, the function "getMainOption()" is not working.
            // this will terminate the system.
            if(menuOpt == 0){
                System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                System.exit(0);
            }
            
            // Goes into this while loop for execution
            while (menuOpt > 0 && menuOpt < 4){
                switch (menuOpt) {
                    // Start List data
                    case 1: 
                        // Displaying the Sub Menu of List data
                        // getListOption() is getting an integer between 1-3
                                // 1) Writing Group
                                // 2) Publisher
                                // 3) Book
                                // 4) Exit
                        listSub = getListOption();
                        // Getting the current line number.
                        currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                        
                        // if listSub = 0, the function "getListOption()" is not working.
                        // this will terminate the system.
                        if(listSub == 0){
                            System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                            System.exit(0);
                        }
                        
                        // Goes into this switch case for execution
                        switch (listSub) {
                            
                            // List Writing Group
                            case 1: 
                                // Displaying the List Data Menu for Writing Group
                                        // 1) List All
                                        // 2) List only a specific one
                                        // 3) Exit
                                listSubSub = getLISTSubOption();
                                // Getting the current line number.
                                currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                                
                                // if listSub = 0, the function "getLISTSubOption()" is not working.
                                // this will terminate the system.
                                if(listSubSub == 0){
                                    System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                    System.exit(0);
                                }
                                
                                // List All Writing Groups 
                                else if (listSubSub == 1) {
                                    stmt = conn.createStatement();
                                    rs  = stmt.executeQuery("SELECT GroupName, HeadWriter, Subject, YearFormed FROM WritingGroups");
                                    displayWG(rs);
                                }
                                
                                // List only a specific writing group
                                else if (listSubSub == 2) {
                                    ps = conn.prepareStatement("SELECT GroupName, HeadWriter, Subject, YearFormed from WritingGroups where GroupName = ?");
                                    System.out.print("Enter Writing Group Name: ");
                                    String groupName = in.nextLine();
                                    
                                    // Outputs an error message if the lengh is out of range
                                    while (groupName.length() > 30){
                                        System.out.print("Entered Writing Group Name was too long. Please enter within 30 characters: ");
                                        groupName = in.nextLine();
                                    }
                                    while (groupName.length() == 0){
                                        System.out.print("Entered Writing Group Name was not valid. Please enter within 30 characters: ");
                                        groupName = in.nextLine();
                                    }
                                    
                                    // Check if the Writing Group is existing in the database
                                    ps.setString(1, groupName);
                                    rs = ps.executeQuery();
                                    
                                    // Goes into this while loop if the entered group is NOT stored in the database.
                                    while (!rs.next()){
                                        System.out.println("Entered Writing Group is NOT in the database.");  
                                        System.out.print("Please re-enter existing publisher name: "); 
                                        groupName = in.nextLine();
                                        ps.setString(1, groupName);
                                        rs = ps.executeQuery();
                                    }
                                    
                                    //Once having existing writing group, list the specific group's work 
                                    ps  = conn.prepareStatement("SELECT GroupName, HeadWriter, Subject, YearFormed FROM WritingGroups WHERE GroupName = ?");
                                    ps.setString(1, groupName);
                                    rs = ps.executeQuery();
                                    displayWG(rs);
                                }
                                break;
                            
                            // List Publishers
                            case 2: 
                                // Displaying the List Data Menu for Publishers
                                        // 1) List All
                                        // 2) List only a specific one
                                        // 3) Exit
                                listSubSub = getLISTSubOption();
                                // Getting the current line number.
                                currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                                
                                if(listSubSub == 0){
                                    System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                    System.exit(0);
                                }
                                
                                // List All Publishers
                                if (listSubSub == 1) {
                                    stmt = conn.createStatement();
                                    rs  = stmt.executeQuery("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM Publishers");
                                    displayPub(rs);
                                }
                                
                                // List only a specific publisher
                                else if (listSubSub == 2) {
                                    ps = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone from Publishers where PublisherName = ?");
                                    System.out.print("Enter Publisher Name: ");
                                    String pubName = in.nextLine();
                                    
                                    // Outputs an error message if the lengh is out of range
                                    while (pubName.length() > 40){
                                        System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                        pubName = in.nextLine();
                                    }
                                    while (pubName.length() == 0){
                                        System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                        pubName = in.nextLine();
                                    }
                                    // Check if the publisher is existing in the database.
                                    ps.setString(1, pubName);
                                    rs = ps.executeQuery();
                                    
                                    // Goes into this while loop if the entered Publisher is NOT stored in the database.
                                    while (!rs.next()){
                                        System.out.println("Entered Publisher is NOT in the database.");  
                                        System.out.print("Please re-enter existing publisher name: "); 
                                        pubName = in.nextLine();
                                        ps.setString(1, pubName);
                                        rs = ps.executeQuery();
                                    }
                                    
                                    //Once having existing writing group, list the specific group's work 
                                    ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone from Publishers where PublisherName = ?");
                                    ps.setString(1, pubName);
                                    rs = ps.executeQuery();
                                    displayPub(rs);
                                }
                                break;
                            
                            // List Books
                            case 3: 
                                // Displaying the List Data Menu for Books
                                        // 1) List All
                                        // 2) List only a specific one
                                        // 3) Exit
                                listSubSub = getLISTSubOption();
                                // Getting the current line number.
                                currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                                
                                if(listSubSub == 0){
                                    System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                    System.exit(0);
                                }
                                
                                // List All Books
                                if (listSubSub == 1) {
                                    stmt = conn.createStatement();
                                    rs  = stmt.executeQuery("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books");
                                    displayB(rs);
                                }
                                
                                // List only a specific book
                                else if (listSubSub == 2) {
                                    ps = conn.prepareStatement("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books WHERE BookTitle = ?");
                                    System.out.print("Enter Book Title: ");
                                    String bookTitle = in.nextLine();
                                    
                                    // Outputs an error message if the lengh is out of range
                                    while (bookTitle.length() > 20){
                                        System.out.print("Entered Book Title was too long. Please enter within 20 characters: ");
                                        bookTitle = in.nextLine();
                                    }
                                    while (bookTitle.length() == 0){
                                        System.out.print("Entered Book Title was not valid. Please enter within 20 characters: ");
                                        bookTitle = in.nextLine();
                                    }
                                    
                                    // Check if the book is existing in the database
                                    ps.setString(1, bookTitle);
                                    rs = ps.executeQuery();
                                    
                                    // Goes into this while loop if the entered Book Title is NOT stored in the database.
                                    while (!rs.next()){
                                        System.out.println("Entered Book Title is NOT in the database.");  
                                        System.out.print("Please re-enter existing Book Title name: "); 
                                        bookTitle = in.nextLine();
                                        ps.setString(1, bookTitle);
                                        rs = ps.executeQuery();
                                    }
                                    
                                    //Once having existing writing group, list the specific group's work 
                                    ps = conn.prepareStatement("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books WHERE BookTitle = ?");
                                    ps.setString(1, bookTitle);
                                    rs = ps.executeQuery();
                                    displayB(rs);
                                }
                                break;
                        }
                        break;  // End List data
                        
                    // Start Insert/Update data
                    case 2:
                        // Displaying the Menu for Insert/Update data
                                // 1) Insert a New Book
                                // 2) Update publisher
                                // 3) Exit
                        insertSub = getINSERTOption();
                        // Getting the current line number.
                        currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                        
                        // if listSub = 0, the function "getINSERTOption()" is not working.
                        // this will terminate the system.
                        if(insertSub == 0){
                            System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                            System.exit(0);
                        }
                        
                        // Insert New Book
                        if (insertSub == 1) {
                            String wgName, bTitle, pubName, yrPub, numPages;
                            System.out.println("The existing books are below. "+
                                    "You can't add a book which has the same Publisher and Book Title");
                            stmt = conn.createStatement();
                            rs  = stmt.executeQuery("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books");
                            displayB(rs);
                            
                            System.out.print("Enter Publishers Name: ");
                            pubName = in.nextLine();
                            
                            // Outputs an error message if the lengh is out of range
                            while (pubName.length() > 40){
                                System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                pubName = in.nextLine();
                            }
                            
                            while (pubName.length() == 0){
                                System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                pubName = in.nextLine();
                            }
                            
                            // Check if the Publisher is existing in the database
                            ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM Publishers WHERE PublisherName = ?");
                            ps.setString(1, pubName);
                            rs = ps.executeQuery();
                            
                            // Goes into this if statement to add the new publisher if the publisher is NOT existing int the database
                            if (!rs.next()){
                                System.out.println("\n\tThe new publisher's name : " + pubName + " will be added to the database.");
                                System.out.print("\tEnter the publisher's address: ");
                                String pubAdds = in.nextLine();
                                
                                // Outputs an error message if the lengh is out of range
                                while (pubAdds.length() > 30){
                                    System.out.print("Entered address was too long. Please enter within 30 characters: ");
                                    pubAdds = in.nextLine();
                                }
                                while (pubAdds.length() == 0){
                                    System.out.print("Entered address was not valid. Please enter within 30 characters: ");
                                    pubName = in.nextLine();
                                }
                                
                                System.out.print("\tEnter the publisher's email address: ");
                                String pubEmail  = in.nextLine();
                                while (pubEmail.length() > 30){
                                    System.out.print("Entered email address was too long. Please enter within 30 characters: ");
                                    pubEmail = in.nextLine();
                                }
                                while (pubEmail.length() == 0){
                                    System.out.print("Entered email address was not valid. Please enter within 30 characters: ");
                                    pubEmail = in.nextLine();
                                }
                                
                                System.out.print("\tEnter the publisher's phone: ");
                                String pubPhn = in.nextLine();
                                while (pubEmail.length() > 12){
                                    System.out.print("Entered phone number was too long. Please enter within 12 integers: ");
                                    pubEmail = in.nextLine();
                                }
                                while (pubEmail.length() == 0){
                                    System.out.print("Entered phone number was not valid. Please enter within 12 integers: ");
                                    pubEmail = in.nextLine();
                                }
                                
                                ps = conn.prepareStatement("INSERT INTO Publishers (PublisherName, PublisherAddress, PublisherEmail, PublisherPhone) "
                                + "values (?, ?, ?, ?)");
                                ps.setString(1, pubName);
                                ps.setString(2, pubAdds);
                                ps.setString(3, pubEmail);
                                ps.setString(4, pubPhn);
                            
                                ps.executeUpdate();
                                
                                System.out.println("\t" + pubName + " has been added tino Publishers.\n");
                            }
                            
                            System.out.print("Enter Book Title: ");
                            bTitle = in.nextLine();
                            
                            // Outputs an error message if the lengh is out of range
                            while (bTitle.length() > 20){
                                    System.out.println("Entered Book Title was too long. Please enter within 20 characters:");
                                    bTitle = in.nextLine();
                                }
                            while (bTitle.length() == 0){
                                    System.out.print("Entered Book Title was not valid. Please enter within 20 characters: ");
                                    bTitle = in.nextLine();
                                }
                            
                            // check if the combination of the Publisher and Book Title are not existing in the database
                            ps  = conn.prepareStatement("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books WHERE BookTitle = ? AND PublisherName = ?");
                            ps.setString(1, bTitle);
                            ps.setString(2, pubName);
                            rs = ps.executeQuery();
                            
                            // Goes into this while loop if the new book has the same publisher and the same book title with the existing database
                            while (rs.next()){
                                
                                    System.out.println("The same publisher and book title is existing in the database.");
                                    System.out.print("Re-enter Publishers Name: ");
                                    pubName = in.nextLine();
                                    if(pubName.length() > 40){
                                        System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                        pubName = in.nextLine();
                                    }

                                    System.out.print("Enter Book Title: ");
                                    bTitle = in.nextLine();

                                    while (bTitle.length() > 20){
                                        System.out.print("Entered Book Title was too long. Please enter within 20 characters: ");
                                        bTitle = in.nextLine();
                                    }
                                    while (bTitle.length() == 0){
                                    System.out.print("Entered Book Title was not valid. Please enter within 20 characters: ");
                                    bTitle = in.nextLine();
                                    }

                                    ps  = conn.prepareStatement("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books WHERE BookTitle = ? AND PublisherName = ?");
                                    ps.setString(1, bTitle);
                                    ps.setString(2, pubName);
                                    rs = ps.executeQuery();
                             }
                            
                            // Continue procedure of inserting a new book
                            System.out.print("Enter Writer Group Name: ");
                            wgName = in.nextLine();
                            
                            // Outputs an error message if the lengh is out of range
                            while (wgName.length() > 30){
                                        System.out.print("Entered Writer Group Name was too long. Please enter within 30 characters: ");
                                        wgName = in.nextLine();
                            }
                            while (wgName.length() == 0){
                                    System.out.print("Entered Writer Group Name was not valid. Please enter within 30 characters: ");
                                    wgName = in.nextLine();
                            }
                            
                            // Check if the Writer Group is existing in the database
                            ps  = conn.prepareStatement("SELECT GroupName, HeadWriter, Subject, YearFormed FROM WritingGroups WHERE GroupName = ?");
                            ps.setString(1, wgName);
                            rs = ps.executeQuery();
                            
                            // Goes into this if statement to add the new WritingGroups if the WritingGroups is NOT existing int the database
                            if (!rs.next()){
                                System.out.println("\n\tThe new WritingGroups's name : " + wgName + " will be added to the database.");
                                System.out.print("\tEnter the WritingGroups's Head Writer: ");
                                String hWriter = in.nextLine();
                                // Outputs an error message if the lengh is out of range
                                while (hWriter.length() > 30){
                                            System.out.print("Entered Head Writer was too long. Please enter within 30 characters: ");
                                            hWriter = in.nextLine();
                                }
                                while (hWriter.length() == 0){
                                        System.out.print("Entered Head Writer was not valid. Please enter within 30 characters: ");
                                        hWriter = in.nextLine();
                                }
                                
                                System.out.print("\tEnter the WritingGroups's Subject: ");
                                String subj  = in.nextLine();
                                while (subj.length() > 20){
                                            System.out.print("Entered Subject was too long. Please enter within 20 characters: ");
                                            subj = in.nextLine();
                                }
                                while (subj.length() == 0){
                                        System.out.print("Entered Subject was not valid. Please enter within 20 characters: ");
                                        subj = in.nextLine();
                                }
                                
                                System.out.print("\tEnter the WritingGroups's YearFormed. ");
                                // Check if entered string can be converted to int
                                yearFormed = stringToInt();
                                // Getting the current line number.
                                currentLine = new Throwable().getStackTrace()[0].getLineNumber();
                                
                                // if yearFormed = 0, the function "stringToInt()" is not working.
                                // this will terminate the system.
                                if(yearFormed == 0){
                                    System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                    System.exit(0);
                                }
                                
                                // Convert back from int to String
                                String yFormed = Integer.toString(yearFormed);
                                ps = conn.prepareStatement("INSERT INTO WritingGroups (GroupName, HeadWriter, Subject, YearFormed) "
                                + "values (?, ?, ?, ?)");
                                ps.setString(1, wgName);
                                ps.setString(2, hWriter);
                                ps.setString(3, subj);
                                ps.setString(4, yFormed);
                            
                                ps.executeUpdate();
                                System.out.println("\t" + wgName + " has been added into WritingGroups.\n");
                            }
                            
                            // Continue procedure of inserting a new book
                            System.out.print("Enter the Year Published. ");
                            // Check if entered string can be converted to int
                            yPublished = stringToInt();
                            // Getting the current line number.
                            currentLine = new Throwable().getStackTrace()[0].getLineNumber();

                            // if yPublished = 0, the function "stringToInt()" is not working.
                            // this will terminate the system.
                            if(yPublished == 0){
                                System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                System.exit(0);
                            }
                            
                            // Convert back from int to String
                            yrPub = Integer.toString(yPublished);
                            
                            System.out.print("Enter Number of Pages. ");
                            // Check if entered string can be converted to int
                            numOfPages = stringToInt();
                            // Getting the current line number.
                            currentLine = new Throwable().getStackTrace()[0].getLineNumber();

                            // if numOfPages = 0, the function "stringToInt()" is not working.
                            // this will terminate the system.
                            if(numOfPages == 0){
                                System.out.println("Something went wrong at LINE " + (currentLine  - 2) + " , goodbye!");
                                System.exit(0);
                            }
                            
                            // Convert back from int to String
                            numPages = Integer.toString(numOfPages);
                            
                            // Add a book into the Books table
                            ps = conn.prepareStatement("INSERT INTO Books (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) "
                            + "values (?, ?, ?, ?, ?)");
                            ps.setString(1, wgName);
                            ps.setString(2, bTitle);
                            ps.setString(3, pubName);
                            ps.setString(4, yrPub);
                            ps.setString(5, numPages);
                            
                            ps.executeUpdate();
                            System.out.println("\t" + bTitle + " has been added into Books as below.");
                            
                            // Display the Books table
                            stmt = conn.createStatement();
                            rs  = stmt.executeQuery("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books");
                            displayB(rs);
                        }
                        
                        // Update publisher
                        else if (insertSub == 2) {
                            String oldPub, newPub;
                            
                            System.out.println("What old Publisher would you like to replace?");
                            stmt = conn.createStatement();
                            rs  = stmt.executeQuery("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM Publishers");
                            displayPub(rs);
                            System.out.print("Enter the old Publisher's name: ");
                            oldPub = in.nextLine();
                            
                            // Outputs an error message if the lengh is out of range
                            while (oldPub.length() > 40){
                                System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                oldPub = in.nextLine();
                            }
                            
                            while (oldPub.length() == 0){
                                System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                oldPub = in.nextLine();
                            }
                            
                            // check if the publisher is existing in the database
                            ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM publishers WHERE PublisherName = ?");
                            ps.setString(1, oldPub);
                            rs = ps.executeQuery();
                            
                            // Goes into this if statement if the old publisher is NOT existing int the database
                            while (!rs.next()){
                                System.out.println("Entered publisher is NOT in the database.");  
                                System.out.print("Please re-enter existing publisher name: "); 
                                oldPub = in.nextLine();
                                
                                // Outputs an error message if the lengh is out of range
                                while (oldPub.length() > 40){
                                    System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                    oldPub = in.nextLine();
                                }
                                while (oldPub.length() == 0){
                                    System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                    oldPub = in.nextLine();
                                }
                                // Re-check if the publisher is existing in the database 
                                ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM publishers WHERE PublisherName = ?");
                                ps.setString(1, oldPub);
                                rs = ps.executeQuery();
                            }
                            
                            System.out.print("Enter name of new Publisher: ");
                            newPub = in.nextLine();
                            
                            // Outputs an error message if the lengh is out of range
                            while (newPub.length() > 40){
                                System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                newPub = in.nextLine();
                            }
                            while (newPub.length() == 0){
                                System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                newPub = in.nextLine();
                            }
                            
                            // Check if the new publisher is existing in the database
                            ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM publishers WHERE PublisherName = ?");
                            ps.setString(1, newPub);
                            rs = ps.executeQuery();
                            
                            // Goes into this while loop if the new publisher name is existing in the database.
                            while (rs.next()){
                                System.out.print("Entered Publisher Name was found in the database. Please enter new publisher's name within 40 characters: ");
                                newPub = in.nextLine();
                                
                                // Outputs an error message if the lengh is out of range
                                while (newPub.length() > 40){
                                    System.out.print("Entered Publisher Name was too long. Please enter within 40 characters: ");
                                    newPub = in.nextLine();
                                }
                                while (newPub.length() == 0){
                                    System.out.print("Entered Publisher Name was not valid. Please enter within 40 characters: ");
                                    newPub = in.nextLine();
                                }
                                
                                // Re-check if the new publisher is existing in the database
                                ps  = conn.prepareStatement("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM publishers WHERE PublisherName = ?");
                                ps.setString(1, newPub);
                                rs = ps.executeQuery();
                            }
                            
                            // take off the relation(foreign key) between books and publisher
                            ps = conn.prepareStatement("ALTER TABLE Books DROP CONSTRAINT PUBLISHERS_BOOKS_2");
                            ps.executeUpdate();
                            
                            // updating publisher's name in Publishers table
                            ps = conn.prepareStatement("UPDATE Publishers SET PublisherName = ? WHERE PublisherName = ?");
                            ps.setString(1, newPub);
                            ps.setString(2, oldPub);
                            ps.executeUpdate();
                            
                            // updating publisher's name in Books table
                            ps = conn.prepareStatement("UPDATE Books SET PublisherName = ? WHERE PublisherName = ?");
                            ps.setString(1, newPub);
                            ps.setString(2, oldPub);
                            ps.executeUpdate();
                            
                            // adding a relation(foreign key) between books and publisher
                            ps = conn.prepareStatement("ALTER TABLE Books ADD CONSTRAINT PUBLISHERS_BOOKS_2 FOREIGN KEY (PublisherName) REFERENCES Publishers (PublisherName)");
                            ps.executeUpdate();
                            
                            System.out.println("\t" + oldPub + " has been updated to " + newPub + "as below.");
                            
                            // Display the updated Publishers table
                            stmt = conn.createStatement();
                            rs  = stmt.executeQuery("SELECT PublisherName, PublisherAddress, PublisherEmail, PublisherPhone FROM Publishers");
                            displayPub(rs);
                        }
                        break;  // End Insert data
                        
                    // Start Remove book
                    case 3:
                        // Check if there is a book in the Books table
                        ps  = conn.prepareStatement("SELECT GroupName, PublisherName, BookTitle, YearPublished, NumberPages FROM books");
                        rs = ps.executeQuery();
                        
                        // Goes in this if statement if there was no books in the books table
                        if (rs.next() == false){
                            System.out.println("There are no book record in the database.");
                        }
                        // Goes into else statement if there were books in the Books table
                        else {
                            System.out.print("Enter Book Title to delete: ");
                            String bookTitle = in.nextLine();

                            // Outputs an error message if the lengh is out of range
                            while (bookTitle.length() > 20){
                                System.out.print("Entered Book Title was too long. Please enter within 20 characters: ");
                                bookTitle = in.nextLine();
                            }
                            while (bookTitle.length() == 0){
                                System.out.print("Entered Book Title was not valid. Please enter within 20 characters: ");
                                bookTitle = in.nextLine();
                            }
                            
                            // Check if the book is existing in the database 
                            ps  = conn.prepareStatement("SELECT GroupName, PublisherName, BookTitle, YearPublished, NumberPages FROM books WHERE BookTitle = ?");
                            ps.setString(1, bookTitle);
                            rs = ps.executeQuery();

                            // Goes into this if statement if the book is NOT existing int the database
                            while (!rs.next()){
                                System.out.println("Entered Book Title is NOT in the database.");  
                                System.out.print("Please re-enter existing Book Title: "); 
                                bookTitle = in.nextLine();
                                
                                // Re-check if the book is existing in the database 
                                ps  = conn.prepareStatement("SELECT GroupName, PublisherName, BookTitle, YearPublished, NumberPages FROM books WHERE BookTitle = ?");
                                ps.setString(1, bookTitle);
                                rs = ps.executeQuery();
                            }

                            // Execute delete procedure
                            ps = conn.prepareStatement("DELETE from Books where BookTitle = ?");
                            ps.setString(1, bookTitle);
                            ps.executeUpdate();
                            System.out.println("Book " + bookTitle + " has been deleted as below.");
                            
                            // Display the updated Books table
                            stmt = conn.createStatement();
                            rs  = stmt.executeQuery("SELECT GroupName, BookTitle, PublisherName, YearPublished, NumberPages FROM Books");
                            displayB(rs);
                        }
                        break;  // End Remove book
            }
                menuOpt = getMainOption();
            } // end of the execute while loop from the Main Menu
            
            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main
    
    
    /**
     * Displays the Main Menu
     */
    public static void displayMainMenu(){
        System.out.println("Select the type of statement to run from below.");
        System.out.println("\t1) List data");
        System.out.println("\t2) Insert/Update data");
        System.out.println("\t3) Remove book");
        System.out.println("\t4) Exit");
        System.out.print("Enter option: ");
    }
    
    /**
     * Prompt the user to select the request from Main Menu
     * @return Opt OptionNumber in int
     */
    public static int getMainOption() {
        Scanner in = new Scanner(System.in);
        int Opt = 0;
        boolean loop = true;
        
        while (loop){
            try{
                // Display the main menu
                displayMainMenu();
                Opt = in.nextInt();
                in.nextLine();

                // -------- Dealing with integer input --------
                // Goes into this while loop if the input was out of range from 1 - 4
                while (Opt < 1 || Opt > 4){
                    System.out.print("The input wasn't correct. Please re-enter " +
                                        "between 1 - 4.\n");
                    displayMainMenu();
                    Opt = in.nextInt();
                    in.nextLine();
                }
                
                // Exit the system if the selection was 4.
                if(Opt == 4){
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                return Opt;
            }
            // -------- Dealing with NON-integer input --------
            catch (InputMismatchException em) {
                System.out.println("Invalid Input. Please re-enter " + 
                                    "an \"INTEGER\" between 1 - 4.\n");
                in.nextLine();
                continue;
            }
        }
        // returning integer between 1-3
        return Opt;
    }
    
    /**
     * Displays the List Menu
     */
    public static void displayListMenu(){
        System.out.println("Select the data type from below.");
        System.out.println("\t1) Writing Group");
        System.out.println("\t2) Publisher");
        System.out.println("\t3) Book");
        System.out.println("\t4) Exit");
        System.out.print("Enter option: ");
    }
    
    /**
     * Prompt the user to select the request from List Menu
     * @return Opt OptionNumber in int
     */
    public static int getListOption() {
        Scanner in = new Scanner(System.in);
        int Opt = 0;
        boolean loop = true;
        
        while (loop){
            try{
                // Display the sub menu
                displayListMenu();
                Opt = in.nextInt();
                in.nextLine();

                // -------- Dealing with integer input --------
                // Goes into this while loop if the input was out of range from 1 - 4
                while (Opt < 1 || Opt > 4){
                    System.out.print("The input wasn't correct. Please re-enter " +
                                        "between 1 - 4.\n");
                    displayListMenu();
                    Opt = in.nextInt();
                    in.nextLine();
                }
                // Exit the system if the selection was 4.
                if(Opt == 4){
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                return Opt;
            }
            // -------- Dealing with NON-integer input --------
            catch (InputMismatchException em) {
                System.out.println("Invalid Input. Please re-enter " + 
                                    "an \"INTEGER\" between 1 - 4.\n");
                in.nextLine();
                continue;
                }
            }
        // returning integer between 1-3
        return Opt;
    }  

    /**
     * Displays the List Sub Menu
     */
    public static void displayLISTSubMenu(){
        System.out.println("Select option from below ");
        System.out.println("\t1) List All");
        System.out.println("\t2) List only a specific one");
        System.out.println("\t3) Exit");
        System.out.print("Enter option: ");
    }
    
    /**
     * Prompt the user to select the request from List Sub Menu
     * @return Opt OptionNumber in int
     */
    public static int getLISTSubOption() {
        Scanner in = new Scanner(System.in);
        int Opt = 0;
        boolean loop = true;
        
        while (loop){
            try{
            // Display the sub menu
            displayLISTSubMenu();
            Opt = in.nextInt();
            in.nextLine();
            
            // -------- Dealing with integer input --------
            // Goes into this while loop if the input was out of range from 1 - 3
            while (Opt < 1 || Opt > 3){
                System.out.print("The input wasn't correct. Please re-enter " +
                                    "between 1 - 3.\n");
                displayLISTSubMenu();
                Opt = in.nextInt();
                in.nextLine();
            }
            // Exit the system if the selection was 4.
            if(Opt == 3){
                System.out.println("Goodbye!");
                System.exit(0);
            }
            return Opt;
        }
        // -------- Dealing with NON-integer input --------
        catch (InputMismatchException em) {
            System.out.println("Invalid Input. Please re-enter " + 
                                "an \"INTEGER\" between 1 - 3.\n");
            in.nextLine();
            continue;
            }
        }
        // returning integer between 1-2
        return Opt;
    }  
    
    /**
     * Displays the Insert Menu
     */
    public static void displayINSERTMenu(){
        System.out.println("Select option from below ");
        System.out.println("\t1) Insert a New Book");
        System.out.println("\t2) Update publisher");
        System.out.println("\t3) Exit");
        System.out.print("Enter option: ");
    }
    
    /**
     * Prompt the user to select the request from Insert Menu
     * @return Opt OptionNumber in int
     */
    public static int getINSERTOption() {
        Scanner in = new Scanner(System.in);
        int Opt = 0;
        boolean loop = true;
        
        while (loop){
            try{
                // Display the sub menu
                displayINSERTMenu();
                Opt = in.nextInt();
                in.nextLine();

                // -------- Dealing with integer input --------
                // Goes into this while loop if the input was out of range from 1 - 3
                while (Opt < 1 || Opt > 3){
                    System.out.print("The input wasn't correct. Please re-enter " +
                                        "between 1 - 3.\n");
                    displayINSERTMenu();
                    Opt = in.nextInt();
                    in.nextLine();
                }
                // Exit the system if the selection was 4.
                if(Opt == 3){
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                return Opt;
            }
            // -------- Dealing with NON-integer input --------
            catch (InputMismatchException em) {
                System.out.println("Invalid Input. Please re-enter " + 
                                    "an \"INTEGER\" between 1 - 3.\n");
                in.nextLine();
                continue;
            }
        }
        // returning integer between 1-2
        return Opt;
    }

    /**
    * Takes the rs ResultSet and displays the WiringGroups table.
    * @param rs The ResultSet.
    */
    public static void displayWG (ResultSet rs) throws SQLException {
        //STEP 5: Extract data from result set
        System.out.println("");
        System.out.printf(displayWGFormat, "Group Name", "Head Writer", "Subject", "Year Formed");
        while (rs.next()) {
            //Retrieve by column name
            String gn = rs.getString("GroupName");
            String hw = rs.getString("HeadWriter");
            String s = rs.getString("Subject");
            String yf = rs.getString("YearFormed");

            //Display values
            System.out.printf(displayWGFormat, 
                    dispNull(gn), dispNull(hw), dispNull(s), dispNull(yf));
        }
        System.out.println("");
    }
    
    /**
    * Takes the rs ResultSet and displays the publishers table.
    * @param rs The ResultSet.
    */
    public static void displayPub (ResultSet rs) throws SQLException {
        //STEP 5: Extract data from result set
        System.out.println("");
        System.out.printf(displayPubFormat, "Publisher Name", "Publisher Address", "Publisher Email", "Publisher Phone");
        while (rs.next()) {
            //Retrieve by column name
            String pn = rs.getString("PublisherName");
            String pa = rs.getString("PublisherAddress");
            String pe = rs.getString("PublisherEmail");
            String pp = rs.getString("PublisherPhone");

            //Display values
            System.out.printf(displayPubFormat, 
                    dispNull(pn), dispNull(pa), dispNull(pe), dispNull(pp));
        }
        System.out.println("");
    }
    
    
    /**
    * Takes the rs ResultSet and displays the books table.
    * @param rs The ResultSet.
    */
    public static void displayB (ResultSet rs) throws SQLException {
        //STEP 5: Extract data from result set
        System.out.println("");
        System.out.printf(displayBookFormat, "Group Name", "Publisher Name", "Book Title", "Year Published", "Number Pages");
        while (rs.next()) {
            //Retrieve by column name

            String gn = rs.getString("GroupName");
            String pn = rs.getString("PublisherName");
            String bt = rs.getString("BookTitle");
            String yp = rs.getString("YearPublished");
            String np = rs.getString("NumberPages");

            //Display values
            System.out.printf(displayBookFormat, 
                    dispNull(gn), dispNull(pn), dispNull(bt), dispNull(yp), dispNull(np));
        }
        System.out.println("");
    }
    
    
    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     * @param input The string to be mapped.
     * @return  Either the input string or "N/A" as appropriate.
     */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }

    /**
     * Takes the s string and convert to int i
     * @param s The string to be converted.
     * @return i int converted from string s.
     */
    private static int stringToInt() {
        Scanner in = new Scanner(System.in);
        int i = 0;
        String s = "";
        boolean loop = true;
        
        while (loop){
            try{
            System.out.print("Please enter an integer: ");
            s = in.nextLine();
            
            // Convert the input from String to int
            i = Integer.parseInt(s.trim());
            
            return i;
            }
            // -------- Dealing with NON-integer input --------
            catch (NumberFormatException nfe){
              System.out.println("Invalid Input. Please re-enter " + 
                                    "an \"INTEGER\".\n");
                in.nextLine();
              continue;
            }
        }
        return i;
    }
}
