package ptui;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import model.LasersModel;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author Denis Zhenilov
 */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    public LasersModel getModel() { return this.model; }

    /**
     * A function that constructs and prints out the string representation of a safe.
     */
    private void display(){
        String display="";
        String line1="  ";
        int xCount=0;
        int counter=this.model.getDimX();
        while(counter>0){
            if(xCount==10){
                xCount=0;
            }
            line1=line1+xCount;
            if(xCount<this.model.getDimX()-1){
                line1=line1+" ";
            }
            xCount++;
            counter--;
        }
        display=display+line1+"\n"+"  ";
        int dashes=(this.model.getDimX()*2)-1;
        while(dashes>0){
            display=display+"-";
            dashes--;
        }
        display=display+"\n";
        for(int y=0; y<this.model.getDimX();y++){
            int yCounter=y;
            if(yCounter>=10){
                yCounter=Integer.parseInt(Character.toString(Integer.toString(yCounter).charAt(Integer.toString(yCounter).length()-1)));
            }
            display=display+yCounter+"|";
            for(int x=0;x<this.model.getDimX();x++){
                display=display+this.model.getSafe()[y][x];
                if(x<this.model.getDimX()-1){
                    display=display+" ";
                }
            }
            display=display+"\n";
        }
        System.out.print(display);
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
        if(this.model.getErrorState()){
            this.model.errorHandled();
        }
        String message=(String) arg;
        if(!message.equals("a|add r c: Add laser to (r,c)\nd|display: Display safe\nh|help: Print this help message\nq|" +
                "quit: Exit program\nr|remove r c: Remove laser from (r,c)\nv|verify: Verify safe correctness")){
            display();
        }
    }
}
