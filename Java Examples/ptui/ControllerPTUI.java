package ptui;

import model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author Denis Zhenilov
 */
public class ControllerPTUI  {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) throws FileNotFoundException {
        Scanner in=new Scanner(System.in);
        boolean quit=false;
        if(inputFile==null){
            System.out.println("No input file specified! Crashing to the command prompt.");
            System.out.print("> ");
            while (in.hasNextLine()){
                String[] input=in.nextLine().split("\\s+");
                if(input.length>0 && !input[0].equals("")){
                    String command=Character.toString(input[0].charAt(0));
                    switch(command){
                        case "a": if(input.length!=3){
                            System.out.println("Incorrect coordinates");
                        }
                        else {
                            this.model.add(Integer.parseInt(input[1]),Integer.parseInt(input[2]));
                        }
                            System.out.print("> ");
                            break;
                        case "d": this.model.display();
                            System.out.print("> ");
                            break;
                        case "h": this.model.help();
                            System.out.print("> ");
                            break;
                        case "q": quit=true;
                            break;
                        case "r": if(input.length!=3){
                            System.out.println("Incorrect coordinates");
                        }
                        else {
                            this.model.remove(Integer.parseInt(input[1]),Integer.parseInt(input[2]));
                        }
                            System.out.print("> ");
                            break;
                        case "v": this.model.verify();
                            System.out.print("> ");
                            break;
                        default:
                            System.out.println("Unrecognized command: "+input[0]);
                            System.out.print("> ");
                            break;
                    }
                }
                else {
                    System.out.print("> ");
                }
                if(quit){
                    break;
                }
            }
        }
        else {
            in=new Scanner(new File(inputFile));
            while(in.hasNextLine()){
                String[]line=in.nextLine().split("\\s+");
                if(line.length>0 && !line[0].equals("")){
                    String command=Character.toString(line[0].charAt(0));
                    switch(command){
                        case "a": if(line.length!=3){
                            System.out.println("Incorrect coordinates");
                        }
                        else {
                            System.out.println("> "+line[0]+" "+line[1]+" "+line[2]);
                            this.model.add(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                        }
                            break;
                        case "d": this.model.display();
                            break;
                        case "h": this.model.help();
                            break;
                        case "q": quit=true;
                            break;
                        case "r": if(line.length!=3){
                            System.out.println("Incorrect coordinates");
                        }
                        else {
                            System.out.println("> "+line[0]+" "+line[1]+" "+line[2]);
                            this.model.remove(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
                        }
                            break;
                        case "v": this.model.verify();
                            break;
                        default:
                            System.out.println("Unrecognized command: "+line[0]);
                            break;
                    }
                }
                if(quit){
                    break;
                }
            }
            if(!quit){
                in=new Scanner(System.in);
                System.out.print("> ");
                while (in.hasNextLine()){
                    String[] input=in.nextLine().split("\\s+");
                    if(input.length>0 && !input[0].equals("")){
                        String command=Character.toString(input[0].charAt(0));
                        switch(command){
                            case "a": if(input.length!=3){
                                System.out.println("Incorrect coordinates");
                            }
                            else {
                                this.model.add(Integer.parseInt(input[1]),Integer.parseInt(input[2]));
                            }
                                System.out.print("> ");
                                break;
                            case "d": this.model.display();
                                System.out.print("> ");
                                break;
                            case "h": this.model.help();
                                System.out.print("> ");
                                break;
                            case "q": quit=true;
                                //System.out.print("> ");
                                break;
                            case "r": if(input.length!=3){
                                System.out.println("Incorrect coordinates");
                            }
                            else {
                                this.model.remove(Integer.parseInt(input[1]),Integer.parseInt(input[2]));
                            }
                                System.out.print("> ");
                                break;
                            case "v": this.model.verify();
                                System.out.print("> ");
                                break;
                            default:
                                System.out.println("Unrecognized command: "+input[0]);
                                System.out.print("> ");
                                break;
                        }
                    }
                    else{
                        System.out.print("> ");
                    }
                    if(quit){
                        break;
                    }
                }
            }
        }
    }
}
