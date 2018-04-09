package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * @author Denis Zhenilov
 * @author Austin Richards
 */
public class LasersModel extends Observable {

    private int dimX;
    private int dimY;
    private String[][] safe;

    private int Ynotifyer;
    private int Xnotifyer;
    private boolean errorState=false;

    /**
     * A function used to process the file with safe configuration.
     * @param filename the name of the file containing safe configuration
     * @throws FileNotFoundException
     */


    public LasersModel(String filename) throws FileNotFoundException {
        Scanner in=new Scanner(new File(filename));
        String lineRead=in.nextLine();
        String[]line=lineRead.split("\\s+");
        this.safe=new String[Integer.parseInt(line[0])][Integer.parseInt(line[1])];
        this.dimY=Integer.parseInt(line[0]);
        this.dimX=Integer.parseInt(line[1]);
        int yCount=0;
        int xCount=0;
        while(in.hasNextLine() && yCount<this.dimY){
            lineRead=in.nextLine();
            line=lineRead.split("\\s+");
            for(String s:line){
                this.safe[yCount][xCount]=s;
                xCount+=1;
            }
            xCount=0;
            yCount+=1;
        }
        setChanged();
        notifyObservers();
    }

    public LasersModel(LasersModel old){
        this.safe=new String[old.getDimY()][old.getDimX()];
        this.dimY=old.getDimY();
        this.dimX=old.getDimX();
        int yCount=0;
        while(yCount<old.getDimY()){
            int xCount=0;
            while (xCount<old.getDimX()){
                this.safe[yCount][xCount]=old.getSafe()[yCount][xCount];
                xCount++;
            }
            yCount++;
        }
        setChanged();
        notifyObservers();
    }

    public void errorHandled(){
        this.errorState=false;
    }

    public boolean getErrorState(){
        return this.errorState;
    }

    public int getProblematicCoordY(){
        return this.Ynotifyer;
    }

    public int getProblematicCoordX(){
        return this.Xnotifyer;
    }

    public String findDiff(LasersModel other){
        int yCount=0;
        while (yCount<other.getDimY()){
            int xCount=0;
            while (xCount<other.getDimX()){
                if(!this.safe[yCount][xCount].equals(other.getSafe()[yCount][xCount])){
                    if(other.getSafe()[yCount][xCount].equals("L")){
                        return "added laser to ("+yCount+", "+xCount+")";
                    }
                    else if(other.getSafe()[yCount][xCount].equals(".")){
                        return "removed laser at ("+yCount+", "+xCount+")";
                    }
                }
                xCount++;
            }
            yCount++;
        }
        return "solved!";
    }

    public void copy (LasersModel old){
        this.safe=new String[old.getDimY()][old.getDimX()];
        this.dimY=old.getDimY();
        this.dimX=old.getDimX();
        int yCount=0;
        while(yCount<old.getDimY()){
            int xCount=0;
            while (xCount<old.getDimX()){
                this.safe[yCount][xCount]=old.getSafe()[yCount][xCount];
                xCount++;
            }
            yCount++;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * A function that places a laser beam at a specified spot, if possible, and creates laser beams. It does not
     * check whether or not a laser would have power or would be flashed at by another laser, but it does check if the
     * space is available (i.e. empty)
     * @param r row where the laser is to be placed
     * @param c column where the laser is to be placed
     */
    public void add(int r, int c){
        if((r<0 || r>this.dimY-1) || (c<0 || c>this.dimX-1)){
            this.Xnotifyer=c;
            this.Ynotifyer=r;
            this.errorState=true;
            setChanged();
            notifyObservers("Error adding laser at: ("+r+", "+c+")");
        }
        else{
            if(this.safe[r][c].equals(".") || this.safe[r][c].equals("*")){
                this.safe[r][c]="L";
                int x=c;
                if(c!=0){
                    while(x>=0){
                        if(this.safe[r][x].equals(".")){
                            this.safe[r][x]="*";
                        }
                        if(this.safe[r][x].equals("X")
                                || this.safe[r][x].equals("0")
                                || this.safe[r][x].equals("1")
                                || this.safe[r][x].equals("2")
                                || this.safe[r][x].equals("3")
                                || this.safe[r][x].equals("4")){
                            break;
                        }
                        x--;
                    }
                }
                x=c;
                if(c<dimX-1){
                    while(x<dimX){
                        if(this.safe[r][x].equals(".")){
                            this.safe[r][x]="*";
                        }
                        if(this.safe[r][x].equals("X")
                                || this.safe[r][x].equals("0")
                                || this.safe[r][x].equals("1")
                                || this.safe[r][x].equals("2")
                                || this.safe[r][x].equals("3")
                                || this.safe[r][x].equals("4")){
                            break;
                        }
                        x++;
                    }
                }
                int y=r;
                if(r!=0){
                    while(y>=0){
                        if(this.safe[y][c].equals(".")){
                            this.safe[y][c]="*";
                        }
                        if(this.safe[y][c].equals("X")
                                || this.safe[y][c].equals("0")
                                || this.safe[y][c].equals("1")
                                || this.safe[y][c].equals("2")
                                || this.safe[y][c].equals("3")
                                || this.safe[y][c].equals("4")){
                            break;
                        }
                        y--;
                    }
                }
                y=r;
                if(r<this.dimY-1){
                    while(y<dimY){
                        if(this.safe[y][c].equals(".")){
                            this.safe[y][c]="*";
                        }
                        if(this.safe[y][c].equals("X")
                                || this.safe[y][c].equals("0")
                                || this.safe[y][c].equals("1")
                                || this.safe[y][c].equals("2")
                                || this.safe[y][c].equals("3")
                                || this.safe[y][c].equals("4")){
                            break;
                        }
                        y++;
                    }
                }
                setChanged();
                notifyObservers("Laser added at: ("+r+", "+c+")");
            }
            else {
                this.Xnotifyer=c;
                this.Ynotifyer=r;
                this.errorState=true;
                setChanged();
                notifyObservers("Error adding laser at: ("+r+", "+c+")");
            }
        }
    }




    /**
     * A function that prints out a help message.
     */
    public void help(){
        setChanged();
        notifyObservers("a|add r c: Add laser to (r,c)\nd|display: Display safe\nh|help: Print this help message\nq|" +
                "quit: Exit program\nr|remove r c: Remove laser from (r,c)\nv|verify: Verify safe correctness");
    }

    /**
     * A simple function to quit the program.
     */

    /**
     * One of several helper functions, it checks whether the cell is  being flashed at from the top.
     * @param r row coordinate of the cell checked
     * @param c column coordinate of the cell checked
     * @return true if there is a laser at the top that can reach the cell, false otherwise
     */
    private boolean checkTop(int r, int c){
        if(r==0){
            return false;
        }
        for(int y=r-1;y>=0;y--){
            if(this.safe[y][c].equals("L")){
                return true;
            }
            if(this.safe[y][c].equals("X")
                    || this.safe[y][c].equals("0")
                    || this.safe[y][c].equals("1")
                    || this.safe[y][c].equals("2")
                    || this.safe[y][c].equals("3")
                    || this.safe[y][c].equals("4")){
                return false;
            }
        }
        return false;
    }

    /**
     * One of several helper functions, checks if the cell is being flashed at from the bottom by a laser.
     * @param r row coord. of a cell
     * @param c column coord. of a cell
     * @return true if there is a laser that can reach the cell at the bottom, false otherwise.
     */
    private boolean checkBottom(int r, int c){
        if(r==this.dimY-1){
            return false;
        }
        for(int y=r+1;y<dimY;y++){
            if(this.safe[y][c].equals("L")){
                return true;
            }
            if(this.safe[y][c].equals("X")
                    || this.safe[y][c].equals("0")
                    || this.safe[y][c].equals("1")
                    || this.safe[y][c].equals("2")
                    || this.safe[y][c].equals("3")
                    || this.safe[y][c].equals("4")){
                return false;
            }
        }
        return false;
    }

    /**
     * One of several helper functions, checks if the cell is being flashed at from the right by a laser.
     * @param r row coord. of a cell
     * @param c column coord. of a cell
     * @return true if there is a laser that can reach the cell at the right, false otherwise.
     */
    private boolean checkRight(int r, int c){
        if(c==this.dimX-1){
            return false;
        }
        for(int x=c+1;x<dimX;x++){
            if(this.safe[r][x].equals("L")){
                return true;
            }
            if(this.safe[r][x].equals("X")
                    || this.safe[r][x].equals("0")
                    || this.safe[r][x].equals("1")
                    || this.safe[r][x].equals("2")
                    || this.safe[r][x].equals("3")
                    || this.safe[r][x].equals("4")){
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if there is a laser flashing at the cell from the left.
     * @param r row coord. of a cell
     * @param c column coord. of a cell
     * @return true if there is a laser that can reach the cell from the left, false otherwise.
     */
    private boolean checkLeft (int r, int c){
        if(c==0){
            return false;
        }
        for(int x=c-1;x>=0;x--){
            if(this.safe[r][x].equals("L")){
                return true;
            }
            if(this.safe[r][x].equals("X")
                    || this.safe[r][x].equals("0")
                    || this.safe[r][x].equals("1")
                    || this.safe[r][x].equals("2")
                    || this.safe[r][x].equals("3")
                    || this.safe[r][x].equals("4")){
                return false;
            }
        }
        return false;
    }

    /**
     * A function that checks, via helpers, if the cell in question can be reached by another laser. It is used to
     * correctly remove laser beams after a laser emitter is removed.
     * @param r row coord. of a cell
     * @param c column coord. of a cell
     * @return true if there is a laser that can reach the cell, false otherwise.
     */
    private boolean checkCell(int r, int c){
        if (!checkTop(r,c)
                && !checkBottom(r,c)
                && !checkLeft(r,c)
                && !checkRight(r,c)){
            return false;
        }
        return true;
    }

    /**
     * A function to remove a laser emitter from the specified cell. It also checks all of the cells that that laser
     * could reach and replaces them with empty spaces if no other laser flashes on them.
     * @param r row coord. of a cell
     * @param c column coord. of a cell
     */
    public void remove(int r, int c){
        if((r<0 || r>this.dimY-1) || (c<0 || c>this.dimX-1)){
            this.Xnotifyer=c;
            this.Ynotifyer=r;
            this.errorState=true;
            setChanged();
            notifyObservers("Error removing laser at: ("+r+", "+c+")");
        }
        else{
            if(this.safe[r][c].equals("L")){
                if(!this.checkCell(r,c)){
                    this.safe[r][c]=".";
                }
                else{
                    this.safe[r][c]="*";
                }
                int x=c;
                int y=r;
                while(x>=0){
                    if(this.safe[r][x].equals("X")||this.safe[r][x].equals("L")||this.safe[r][x].equals("0")
                            || this.safe[r][x].equals("1")
                            || this.safe[r][x].equals("2")
                            || this.safe[r][x].equals("3")
                            || this.safe[r][x].equals("4")){
                        break;
                    }
                    if(!this.checkCell(r,x)){
                        this.safe[r][x]=".";
                    }
                    else{
                        this.safe[r][x]="*";
                    }
                    x--;
                }
                x=c;
                while (x<this.dimX){
                    if(this.safe[r][x].equals("X")||this.safe[r][x].equals("L")||this.safe[r][x].equals("0")
                            || this.safe[r][x].equals("1")
                            || this.safe[r][x].equals("2")
                            || this.safe[r][x].equals("3")
                            || this.safe[r][x].equals("4")){
                        break;
                    }
                    if(!this.checkCell(r,x)){
                        this.safe[r][x]=".";
                    }
                    else{
                        this.safe[r][x]="*";
                    }
                    x++;
                }
                while(y>=0){
                    if(this.safe[y][c].equals("X")||this.safe[y][c].equals("L")||this.safe[y][c].equals("0")
                            || this.safe[y][c].equals("1")
                            || this.safe[y][c].equals("2")
                            || this.safe[y][c].equals("3")
                            || this.safe[y][c].equals("4")){
                        break;
                    }
                    if(!this.checkCell(y,c)){
                        this.safe[y][c]=".";
                    }
                    else{
                        this.safe[y][c]="*";
                    }
                    y--;
                }
                y=r;
                while(y<this.dimY){
                    if(this.safe[y][c].equals("X")||this.safe[y][c].equals("L")||this.safe[y][c].equals("0")
                            || this.safe[y][c].equals("1")
                            || this.safe[y][c].equals("2")
                            || this.safe[y][c].equals("3")
                            || this.safe[y][c].equals("4")){
                        break;
                    }
                    if(!this.checkCell(y,c)){
                        this.safe[y][c]=".";
                    }
                    else{
                        this.safe[y][c]="*";
                    }
                    y++;
                }
                setChanged();
                notifyObservers("Laser removed at: ("+r+", "+c+")");
            }
            else {
                this.Xnotifyer=c;
                this.Ynotifyer=r;
                this.errorState=true;
                setChanged();
                notifyObservers("Error removing laser at: ("+r+", "+c+")");
            }
        }
    }

    public boolean verifyModel(){
        for(int y=0;y<dimY;y++){
            for(int x=0;x<dimX;x++){
                String cell=this.safe[y][x];
                switch (cell){
                    case "L": if(this.checkCell(y,x)){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case "0": if(lasersPresent(y,x)!=0){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case "1": if(lasersPresent(y,x)!=1){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case "2": if(lasersPresent(y,x)!=2){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case "3": if(lasersPresent(y,x)!=3){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case "4": if(lasersPresent(y,x)!=4){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                    }
                        break;
                    case ".":
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * A stub function that calls the helper, printing out a message of the result of attempting to verify th safe.
     */
    public void verify(){
        setChanged();
        notifyObservers(verifyHelper());
    }

    /**
     * A simple helper intended to count how many lasers are actually around the pillar.
     * @param r row coord. of the pillar
     * @param c column coord. of the pillar
     * @return number of lasers around the pillar.
     */
    private int lasersPresent(int r,int c){
        int lasers=0;
        if(r>0){
            if (this.safe[r-1][c].equals("L")){
                lasers+=1;
            }
        }
        if(r<this.dimY-1){
            if(this.safe[r+1][c].equals("L")){
                lasers+=1;
            }
        }
        if(c>0){
            if(this.safe[r][c-1].equals("L")){
                lasers+=1;
            }
        }
        if(c<this.dimX-1){
            if(this.safe[r][c+1].equals("L")){
                lasers+=1;
            }
        }
        return lasers;
    }

    /**
     * The "helper' of the verify function that actually does most of the work. Verifies if no lasers are being
     * exposed to other lasers and if all pillars have the correct number of lasers connected to them.
     * @return a String message with the result of verification.
     */
    private String verifyHelper(){
        for(int y=0;y<dimY;y++){
            for(int x=0;x<dimX;x++){
                String cell=this.safe[y][x];
                switch (cell){
                    case "L": if(this.checkCell(y,x)){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case "0": if(lasersPresent(y,x)!=0){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case "1": if(lasersPresent(y,x)!=1){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case "2": if(lasersPresent(y,x)!=2){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case "3": if(lasersPresent(y,x)!=3){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case "4": if(lasersPresent(y,x)!=4){
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                    }
                        break;
                    case ".":
                        this.Xnotifyer=x;
                        this.Ynotifyer=y;
                        this.errorState=true;
                        return "Error verifying at: ("+y+", "+x+")";
                }
            }
        }
        return "Safe is fully verified!";
    }

    public int getDimX(){
        return this.dimX;
    }

    public int getDimY(){
        return this.dimY;
    }

    public String[][] getSafe(){
        return this.safe;
    }

    public void display(){
        String display="";
        String line1="  ";
        int xCount=0;
        int counter=this.dimX;
        while(counter>0){
            if(xCount==10){
                xCount=0;
            }
            line1=line1+xCount;
            if(xCount<this.dimX-1){
                line1=line1+" ";
            }
            xCount++;
            counter--;
        }
        display=display+line1+"\n"+"  ";
        int dashes=(this.dimX*2)-1;
        while(dashes>0){
            display=display+"-";
            dashes--;
        }
        display=display+"\n";
        for(int y=0; y<this.dimY;y++){
            int yCounter=y;
            if(yCounter>=10){
                yCounter=Integer.parseInt(Character.toString(Integer.toString(yCounter).charAt(Integer.toString(yCounter).length()-1)));
            }
            display=display+yCounter+"|";
            for(int x=0;x<this.dimX;x++){
                display=display+this.safe[y][x];
                if(x<dimX-1){
                    display=display+" ";
                }
            }
            display=display+"\n";
        }
        System.out.print(display);
    }

    public String toString(){
        String display="";
        String line1="  ";
        int xCount=0;
        int counter=this.dimX;
        while(counter>0){
            if(xCount==10){
                xCount=0;
            }
            line1=line1+xCount;
            if(xCount<this.dimX-1){
                line1=line1+" ";
            }
            xCount++;
            counter--;
        }
        display=display+line1+"\n"+"  ";
        int dashes=(this.dimX*2)-1;
        while(dashes>0){
            display=display+"-";
            dashes--;
        }
        display=display+"\n";
        for(int y=0; y<this.dimY;y++){
            int yCounter=y;
            if(yCounter>=10){
                yCounter=Integer.parseInt(Character.toString(Integer.toString(yCounter).charAt(Integer.toString(yCounter).length()-1)));
            }
            display=display+yCounter+"|";
            for(int x=0;x<this.dimX;x++){
                display=display+this.safe[y][x];
                if(x<dimX-1){
                    display=display+" ";
                }
            }
            display=display+"\n";
        }
        return (display);
    }

    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    private void announceChange() {
        setChanged();
        notifyObservers();
    }
}
