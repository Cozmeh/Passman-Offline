import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import javax.swing.JFileChooser; 
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


// MAIN CLASS 
public class Passman          
{                          
    
//  GLOBAL VARIABLES     
    private Map<String,ArrayList<String>> details = new HashMap<String,ArrayList<String>>();
    private String key = " ";                            // initialize them to access in the methods 
    private ArrayList<String> values ;
    private static int loginkey = 1;

//  ENCRYPTION / DECRYPTION VARIABLES
    private String Encrypteduname =" ";
    private String Encryptedpass = " ";
    private String Decrypteduname = "";
    private String Decryptedpass = "";

//  GLOBAL SCANNER 
    static Scanner scan = new Scanner(System.in);






// COPY METHOD 
    private void copy()
    {
        if (details.isEmpty()) 
        {    
            System.out.println("\n System : No records | Try 'add' or 'load'");
        }
        else
        {
            Set<String> keys = details.keySet();
            System.out.println("\nSelect the service name to copy its password to clipboard");
            System.out.println("-----------------------------------------------------");
            System.out.println("Availabe services :");
            for(String k : keys)
            {
                System.out.println("\n=> "+ConsoleColors.ROSY_PINK+k+ConsoleColors.RESET);         // prints out the keys ie. service names availabe
            }
            System.out.println("-----------------------------------------------------");
        }

        System.out.print("Copy : ");
        String sname = scan.nextLine();
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        if (!details.containsKey(sname))
        { 
            System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Invalid service name " + ConsoleColors.RESET + "[ " + sname + " ]");
        }
        else
        {
            for(Map.Entry<String,ArrayList<String>> entry: details.entrySet())
            {
                String key = entry.getKey();
                values = entry.getValue();
                decrypt(values.get(0),values.get(1));
                if(key.equalsIgnoreCase(sname))
                {
                    
                        StringSelection selection = new StringSelection(Decryptedpass);
                        clipboard.setContents(selection, selection);
                        System.out.println(ConsoleColors.TEAL + "\nSuccess : {" + sname + "}'s Password has been copied to clipboard "+ ConsoleColors.RESET + "Username: " + Decrypteduname); 
                    
                }        
            }
        }
    
    }


// UPDATE METHOD 
    private void getupdate()
    {
        try{
            new ProcessBuilder("cmd","/c","javac Passman.java").inheritIO().start().waitFor();
            new ProcessBuilder("cmd","/c","java Passman").inheritIO().start().waitFor();
            System.exit(0);
        }
        catch(Exception e)
        {
            System.out.println("Cannot clear screen at the moment!");
        }
    }


// CLEAR SCREEN METHOD
    private void clear()
    {
        try{
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }
        catch(Exception e)
        {
            System.out.println("Cannot clear screen at the moment!");
        }
    }


// SAVE METHOD
    private void save() throws IOException
    {
        String caution = "";
        if(details.isEmpty())
        {
            System.out.println(ConsoleColors.RED_BRIGHT + "\nCAUTION : No data available to save " + ConsoleColors.RESET);
            System.out.print("Do you want to save ? [y/n]: ");
            caution = scan.nextLine();
            switch(caution)
            {
                case "y":
                    FileOutputStream fileout = new FileOutputStream("Data");
                    ObjectOutputStream out = new ObjectOutputStream(fileout);
                    out.writeObject(details);
                    out.close();
                    fileout.close();
                    System.out.println(ConsoleColors.TEAL + "\nSuccess : Data saved (Data file empty)"+ ConsoleColors.RESET);
                    break;
                case "n":
                    System.out.println("System : Save Terminated");
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Error : Invalid option" + ConsoleColors.RESET);
                    break;
            }
        }
        else
        {
            FileOutputStream fileout = new FileOutputStream("Data");
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(details);
            out.close();
            fileout.close();
            System.out.println(ConsoleColors.TEAL + "\nSuccess : Data saved" + ConsoleColors.RESET);
        }
        
    }


// LOAD METHOD
    private void load() throws IOException, ClassNotFoundException
    {
        try
        {
            FileInputStream filein = new FileInputStream("Data");
            ObjectInputStream in = new ObjectInputStream(filein);
            details = (Map<String, ArrayList<String>>)in.readObject();
            in.close();
            filein.close();
            System.out.println(ConsoleColors.TEAL + "\nSuccess : Data loaded" + ConsoleColors.RESET);
        }
        catch(IOException e)
        {
            System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Data couldn't load " + e + ConsoleColors.RESET);
        }
    }


// HELP METHOD
    public static void help()
    {
        System.out.println("--------------------------------------------------------------------------------\n");
        System.out.println(" add     - To ADD credentials\n");
        System.out.println(" remove  - To REMOVE any existing credentials\n");
        System.out.println(" edit    - To EDIT existing credentials, 'NOTE - Service name is not editable!'\n");
        System.out.println(" display - To DISPLAY existing credentials\n"); 
        System.out.println(" help    - Congrats you understood this one!\n");
        System.out.println(" save    - To SAVE the data\n");
        System.out.println(" load    - To LOAD the saved data\n");
        System.out.println(" clear   - To CLEAR the screen\n");
        System.out.println(" export  - To EXPORT the saved data(Encrypted or Decrypted) into any type of text file (specify file extension)\n");
        System.out.println(" update  - To get UPDATE of the program\n");
        System.out.println(" copy    - To copy the password to clipboard \n");
        System.out.println(" exit    - To EXIT the program");
        System.out.println("\n--------------------------------------------------------------------------------");
    }   


// REMOVE/DELETE METHOD     
    private void remove() throws IOException
    {
        if (details.isEmpty()) 
        {    
            System.out.println("\n System : No records | Try 'add' or 'load'");
        }
        else
        {
            Set<String> keys = details.keySet();
            System.out.println("-----------------------------------------------------");
            System.out.println("Availabe services :");
            for(String k : keys)
            {
                System.out.println("\n=> "+ConsoleColors.ROSY_PINK+k+ConsoleColors.RESET);         // prints out the keys ie. service names availabe
            }
            System.out.println("-----------------------------------------------------");

            System.out.print("\nDelete/Remove : ");
            String sname = scan.nextLine().trim();
            String[] snames = sname.split(",");
            List<String> servicesList = new ArrayList<>(Arrays.asList(snames));

            for(int i = 0; i < servicesList.size();i++)
            {
                if(details.containsKey(servicesList.get(i))==false)
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Invalid service name " + ConsoleColors.RESET + "[ " + servicesList.get(i) + " ]");
                }
                else
                {
                    details.remove(servicesList.get(i));
                    System.out.println("\nSystem : Deleted [ " + servicesList.get(i) + " ]");
                    if(details.isEmpty())
                    {
                        System.out.println("\nSystem : The Record is Empty ");
                        save();
                    }  
                }
            } 
        }
    }


// EDIT METHOD 
    private void edit() throws IOException
    {
        if(details.isEmpty())
        { 
            System.out.println("\nSystem : No records | Try 'add' or 'load'"); 
        }
        else
        {
            Set<String> keys = details.keySet();
            System.out.println("-----------------------------------------------------");
            System.out.println("Available services :");
            for(String k : keys)
            {
                System.out.println("\n=> "+ConsoleColors.ROSY_PINK+k+ConsoleColors.RESET);         // prints out the keys ie. service names availabe
            }
            System.out.println("-----------------------------------------------------");

            System.out.print("\nEdit : ");
            String sname = scan.nextLine();
            if(details.get(sname)==null)
            {
                System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Invalid service name" + ConsoleColors.RESET + "["+sname+"]");
            }
            else
            {
                System.out.println("-----------------------------------------------------");
                System.out.print("New Email/Username: ");
                String uname = scan.nextLine();
                if(uname == "")
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Empty Email/Username" + ConsoleColors.RESET);
                }
                else
                {
                    System.out.print("New Password: ");
                    String pass = scan.nextLine();
                    if(pass == "")
                    {
                        System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Empty password" + ConsoleColors.RESET);
                    }
                    else
                    {
                        details.remove(sname);
                        encrypt(uname, pass);  // encrypting the edited username and password 
                        details.put(sname, new ArrayList<String>());
                        details.get(sname).add(Encrypteduname);                         
                        details.get(sname).add(Encryptedpass);
                        System.out.println(ConsoleColors.TEAL + "\nSuccess : Record updated" + ConsoleColors.RESET);
                        save();
                        System.out.println("-----------------------------------------------------");
                    } 
                } 
            }
        }
    }


// DISPLAY METHOD 
    private void display() throws IOException      
    {                                              
        Scanner Scan = new Scanner(System.in);           
        System.out.println("\n Display options : ");
        System.out.println("(1) Display all");
        System.out.println("(2) Display Single/Multiple");
        System.out.print("\nMake your selection : ");
        String display_option = Scan.nextLine();
        switch(display_option)
        {
            case "1":                                      // displays all the credentials with the service names 
                System.out.println();
                if (details.isEmpty())
                {
                    System.out.println("\nSystem : No records | Try 'add' or 'load'");
                }
                else
                {
                    System.out.println("-----------------------------------------------------");
                    for(Map.Entry<String,ArrayList<String>> entry: details.entrySet())
                    {
                        key = entry.getKey();                // NOTE: do not specify data type in order to access global variables 
                        values = entry.getValue();
                        decrypt(values.get(0),values.get(1));
                        System.out.println("\n=> "+ConsoleColors.ROSY_PINK + key + ConsoleColors.RESET + " = " +ConsoleColors.WHITE_UNDERLINED+ Decrypteduname + ConsoleColors.RESET +" | "+ ConsoleColors.CYAN + Decryptedpass + ConsoleColors.RESET);
                    }
                    System.out.println("\n-----------------------------------------------------");
                }
            break;

            case "2":                                                        // displays only the selected one
                if (details.isEmpty())
                {
                     System.out.println("\nSystem : No records | Try 'add' or 'load'"); 
                }          
                else
                {
                    Set<String> keys = details.keySet();
                    System.out.println("\nAvailable services :");
                    for(String k : keys)
                    {
                        System.out.println("\n=> "+ConsoleColors.ROSY_PINK+k+ConsoleColors.RESET);         // prints out the keys ie. service names availabe
                    }
                    System.out.println("-----------------------------------------------------");

                    BufferedReader bufread = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("\nDisplay password  : ");
                    String servicename = bufread.readLine().trim();

                    String[] servicenames = servicename.split(",");
                    List<String> servicesList = new ArrayList<>(Arrays.asList(servicenames));


                    for(int i = 0; i < servicesList.size();i++)
                    {
                        if (!details.containsKey(servicesList.get(i)))
                        { 
                            System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Invalid service name " + ConsoleColors.RESET + "[ " + servicesList.get(i) + " ]");
                        }
                        else
                        {
                            for(Map.Entry<String,ArrayList<String>> entry: details.entrySet())
                            {
                                String key = entry.getKey();
                                values = entry.getValue();

                                decrypt(values.get(0),values.get(1));

                                if(key.equalsIgnoreCase(servicesList.get(i)))
                                {
                                    System.out.println("\n=> " +ConsoleColors.WHITE_UNDERLINED+ Decrypteduname + ConsoleColors.RESET +" | "+ ConsoleColors.CYAN + Decryptedpass + ConsoleColors.RESET);
                                }        
                            }
                        }
                    }
                    System.out.println("-----------------------------------------------------");                                                                        // a perticular service name 
                }
            break;

            default:
                System.out.println(ConsoleColors.RED_BRIGHT + "Error : Invalid selection" + ConsoleColors.RESET);
            break;
        }
        
    }


// ADD METHOD
    private void Add() throws IOException
    {
        System.out.println("-----------------------------------------------------");
        System.out.print("Service name : ");         //servicename is the name of the service in which the username and password will be userd
        String sname = scan.nextLine().trim();                   // sname = service name
        
        if(sname == "")
        {
            System.out.println(ConsoleColors.RED_BRIGHT + "Error : Empty service name" + ConsoleColors.RESET);
        }
        else if (details.containsKey(sname))
        {
            System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Service name already exists" + ConsoleColors.RESET);
        }else
        {            
            System.out.print("Email/Username : ");
            String uname = scan.nextLine().trim();                        // uname = username 
            System.out.print("Password : ");
            String pass = scan.nextLine().trim();
            if(pass.length()<=5){
                System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Password is too short" + ConsoleColors.RESET);
            }
            else
            {
                encrypt(uname,pass);
                details.put(sname, new ArrayList<String>());         // putting sname and arraylist 
                details.get(sname).add(Encrypteduname);                       // getting the sname and adding uname to it 
                details.get(sname).add(Encryptedpass);  
                System.out.println(ConsoleColors.TEAL + "\nSuccess : Record Added" + ConsoleColors.RESET);
                save();
                System.out.println("-----------------------------------------------------");
            }
    
        }    
    }




// EXPORT METHOD
    private void export()
    {
        System.out.println("Export options :");
        System.out.println("(1) Export Encrypted");
        System.out.println("(2) Export Decrypted");
        System.out.print("Make your selection : ");
        String ex_selection = scan.nextLine();
        switch(ex_selection)
        {
            case "1":
                JFileChooser jFileChooser = new JFileChooser();
                int respose = jFileChooser.showSaveDialog(null); 
                try
                {
                    File location;
                    if(respose == JFileChooser.APPROVE_OPTION)
                    {
                        location = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                    
                        List<String> dataArray = new ArrayList<>();
                        int arraycount = 1;
                        if (details.isEmpty())
                        {
                            System.out.println("\nSystem : No records | Try 'add' or 'load'");
                        }
                        else
                        {
                            dataArray.add(0, "" + System.lineSeparator() );
                            for(Map.Entry<String,ArrayList<String>> entry: details.entrySet())
                            {
                                key = entry.getKey();                // NOTE: do not specify data type in order to access global variables 
                                values = entry.getValue();
                                dataArray.add(arraycount, System.lineSeparator() + key + " = " + values.get(0) + " | " + values.get(1) + " ");
                                arraycount++;
                            
                            }
                            dataArray.add(arraycount, System.lineSeparator()+ "" + System.lineSeparator() );
                            Files.writeString(location.toPath(), dataArray.toString());
                            System.out.println(ConsoleColors.TEAL + "\nSuccess : Data exported at " + location + ConsoleColors.RESET);
                        }
                    }
                    else
                    {
                        System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Export failed" + ConsoleColors.RESET);
                    }
                }
                catch(IOException e)
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "\nError : " + e + ConsoleColors.RESET);
                }
            break;

            case "2":
                JFileChooser jFileChooser1 = new JFileChooser();
                int respose1 = jFileChooser1.showSaveDialog(null); 
                try
                {
                    File location;
                    if(respose1 == JFileChooser.APPROVE_OPTION)
                    {
                        location = new File(jFileChooser1.getSelectedFile().getAbsolutePath());
                    
                        List<String> dataArray = new ArrayList<>();
                        int arraycount = 1;
                        if (details.isEmpty())
                        {
                            System.out.println("\nSystem : No records | Try 'add' or 'load'");
                        }
                        else
                        {
                            dataArray.add(0, "" + System.lineSeparator() );
                            for(Map.Entry<String,ArrayList<String>> entry: details.entrySet())
                            {
                                key = entry.getKey();                // NOTE: do not specify data type in order to access global variables 
                                values = entry.getValue();
                                decrypt(values.get(0),values.get(1) );
                                dataArray.add(arraycount, System.lineSeparator() + key + " = " + Decrypteduname + " | " + Decryptedpass + " ");
                                arraycount++;
                            
                            }
                            dataArray.add(arraycount, System.lineSeparator()+ "" + System.lineSeparator() );
                            Files.writeString(location.toPath(), dataArray.toString());
                            System.out.println(ConsoleColors.TEAL + "\nSuccess : Data exported at " + location + ConsoleColors.RESET);
                        }
                    }
                    else
                    {
                        System.out.println(ConsoleColors.RED_BRIGHT + "\nError : Export failed" + ConsoleColors.RESET);
                    }
                }
                catch(IOException e)
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "\nError : " + e + ConsoleColors.RESET);
                }
            break;

            default:
                System.out.println(ConsoleColors.RED_BRIGHT + "Error : Invalid selection "+ ConsoleColors.RESET + "["+ex_selection+"]");
            break;
        }       
        
    }

 
//  MAIN METHOD 
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {   
       
        Console console = System.console();
        String ch,loginpass;
        Passman passman = new Passman();
        while(true)
        {
            passman.clear();
            char[] password = console.readPassword(ConsoleColors.GREEN_BOLD_BRIGHT +"Login password: " + ConsoleColors.RESET );
            loginpass = new String (password);

            for(int i=0; i<loginpass.length();i++)
            {
                loginkey = loginkey * loginpass.charAt(i);
            }
            if(loginkey == 311345152)
            {
                System.out.println();
                System.out.println("  Welcome to");
                logo();
                help();
                passman.load();
                while(true)
                {        
                    //repeated prompt Passman> ......
                    System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT +"\nPassman> " + ConsoleColors.RESET);
                    ch = scan.nextLine();
                    switch (ch.trim().toLowerCase())
                    {
                        case "exit":
                            System.out.println(ConsoleColors.RED_BACKGROUND + "\nProgram Exited!\n" + ConsoleColors.RESET);
                            System.exit(0);
                            break;
                        case "add":
                            passman.Add();
                            break;
                        case "remove":
                            passman.remove();
                            break;
                        case "edit":
                            passman.edit();
                            break;
                        case "display":
                            passman.display();
                            break;
                        case "help":
                            help();
                            break;
                        case "save":
                            passman.save();
                            break;
                        case "load":
                            passman.load();
                            break;
                        case "clear":
                            passman.clear();
                            break;
                        case "logo":
                            logo();
                            break;
                        case "export":
                            passman.export();
                            break;
                        case "update":
                            System.out.println("\nUpdating.. ");
                            passman.getupdate();
                            break;
                        case "about":
                            System.out.println("This is a terminal based Password manager");
                            break;
                        case "copy":
                            passman.copy();
                            break;
                        default:
                            System.out.println(ConsoleColors.RED_BRIGHT + "Error : Invalid command" + ConsoleColors.RESET + "[" + ch + "]");
                            break;
                    }
                } 
            }
            else
            {
                System.out.println(ConsoleColors.RED_BRIGHT + "Error : Invalid login password" + ConsoleColors.RESET);
                loginkey = 1;
            }
        }   
    }




    
// ENCRYPTING METHOD
    private void encrypt(String uname,String pass)
    {

        int flUname = uname.length();  //length of the username
        int hlUname = flUname/2;       // half lenght of the username
        int flPass = pass.length();     // length of the pass
        int hlPass = flPass/2;          // half length of the password
        
        char[] unChar = uname.toCharArray();             //storing the passed uname and password into a character sequence to process in a loop 
        char[] pasChar = pass.toCharArray();  
//--------------------------------------------------------
// FIRST SHIFT 
        String firstShift_uname = "";                  //original strings 1st shifted string
        String firstShift_pass = ""; 

        for(char c: unChar)
        {                          //shifting each character from the char seq and storing in firstshifted 
            c-=hlUname*6;
            //shiftuname.add(c);
            firstShift_uname = firstShift_uname + c;
        }
        for(char c: pasChar)
        {
            c-=hlPass*6;
            //shiftpass.add(c);
            firstShift_pass = firstShift_pass + c;
        }
//-------------------------------------------------------
// SECOND SHIFT 
        String secondShift_uname = "";
        String secondShift_pass = "";


        for(char c: unChar)
        {                                 // shift plus 3
            c+=hlUname+3;
            secondShift_uname = secondShift_uname + c;
        }
        for (char c : pasChar) 
        {
            c+=hlPass+3;
            secondShift_pass = secondShift_pass + c;
        }
//---------------------------------------------------------------  
        Encrypteduname = "";                      //global variables to use in other functions
        Encryptedpass = "";

        Encrypteduname =  secondShift_uname + firstShift_uname;
        Encryptedpass = secondShift_pass + firstShift_pass;

    }


// DECRYPT METHOD
    private void decrypt(String uname,String pass)
    {

        int flUname = uname.length()/2;  //length of the username
        int hlUname = flUname/2;       // half lenght of the username
        int flPass = pass.length()/2;     // length of the pass
        int hlPass = flPass/2;          // half length of the password

        String tempuname = "";
        String temppass = "";

        tempuname = uname.substring(0, flUname);
        temppass = pass.substring(0, flPass);
        
        char[] unChar = tempuname.toCharArray();             //storing the passed uname and password into a character sequence to process in a loop 
        char[] pasChar = temppass.toCharArray();  
//--------------------------------------------------------
// SECOND SHIFT 
        String secondShift_uname = "";
        String secondShift_pass = "";

        for(char c: unChar)
        {                                 // shift plus 3
            c-=hlUname+3;
            secondShift_uname = secondShift_uname + c;
        }
        for (char c : pasChar)
        {
            c-=hlPass+3;
            secondShift_pass = secondShift_pass + c;
        }

//--------------------------------------------------------------
        Decrypteduname = "";                      //global variables to use in other functions
        Decryptedpass = "";

        Decrypteduname = secondShift_uname;
        Decryptedpass = secondShift_pass;
       
    }


// LOGO METHOD 
    public static void logo()
    {
        System.out.println
        (""+


                "  ██████   █████  ███████ ███████ ███    ███  █████  ███    ██ "+
              "\n  ██   ██ ██   ██ ██      ██      ████  ████ ██   ██ ████   ██ "+ 
              "\n  ██████  ███████ ███████ ███████ ██ ████ ██ ███████ ██ ██  ██ "+
              "\n  ██      ██   ██      ██      ██ ██  ██  ██ ██   ██ ██  ██ ██ "+
              "\n  ██      ██   ██ ███████ ███████ ██      ██ ██   ██ ██   ████ "+
                                                                             
                                                                             
        "");

    }






}
