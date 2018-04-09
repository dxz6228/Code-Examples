package backtracking;

import model.LasersModel;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Denis Zhenilov
 * @author Austin Richards
 */
public class SafeConfig implements Configuration {

    private LasersModel model;

    public SafeConfig(String filename) {
        try{
            this.model=new LasersModel(filename);
        }
        catch (FileNotFoundException e){
            System.out.println("Error: file '"+filename+"' not found.");
        }
    }

    public SafeConfig(LasersModel copy){
        this.model=copy;
    }

    public LasersModel accessModel(){
        return this.model;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        LinkedList<Configuration> successors = new LinkedList<>();
        int yCounter=0;
        while (yCounter<this.model.getDimY()){
            int xCounter=0;
            while (xCounter<this.model.getDimX()){
                if(this.model.getSafe()[yCounter][xCounter].equals(".")){
                    LasersModel copy=new LasersModel(this.model);
                    copy.add(yCounter,xCounter);
                    SafeConfig successor=new SafeConfig(copy);
                    successors.add(successor);
                }
                xCounter++;
            }
            yCounter++;
        }
        return successors;
    }

    private boolean canHaveLasers(int y, int x){
        int empty=0;
        int lasers=0;
        if(y>0){
            if (this.model.getSafe()[y-1][x].equals("L")){
                lasers+=1;
            }
            else if(this.model.getSafe()[y-1][x].equals(".")){
                empty+=1;
            }
        }
        if(y<this.model.getDimY()-1){
            if(this.model.getSafe()[y+1][x].equals("L")){
                lasers+=1;
            }
            else if(this.model.getSafe()[y+1][x].equals(".")){
                empty+=1;
            }
        }
        if(x>0){
            if(this.model.getSafe()[y][x-1].equals("L")){
                lasers+=1;
            }
            else if(this.model.getSafe()[y][x-1].equals(".")){
                empty+=1;
            }
        }
        if(x<this.model.getDimX()-1){
            if(this.model.getSafe()[y][x+1].equals("L")){
                lasers+=1;
            }
            else if(this.model.getSafe()[y][x+1].equals(".")){
                empty+=1;
            }
        }
        switch (this.model.getSafe()[y][x]){
            case "0":
                if(lasers>0){
                    return false;
                }
                return true;
            case "1":
                if(lasers>1){
                    return false;
                }
                else if (lasers+empty<1){
                    return false;
                }
                return true;
            case "2":
                if(lasers>2){
                    return false;
                }
                else if(lasers+empty<2){
                    return false;
                }
                return true;
            case "3":
                if(lasers>3){
                    return false;
                }
                else if(lasers+empty<3){
                    return false;
                }
                return true;
            case "4":
                if(lasers>4){
                    return false;
                }
                else if(lasers+empty<4){
                    return false;
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        boolean freeSpace=false;
        int yCounter=0;
        while (yCounter<this.model.getDimY()){
            int xCounter=0;
            while (xCounter<this.model.getDimX()){
                switch (this.model.getSafe()[yCounter][xCounter]){
                    case "0":
                        return canHaveLasers(yCounter,xCounter);
                    case "1":
                        return canHaveLasers(yCounter,xCounter);
                    case "2":
                        return canHaveLasers(yCounter,xCounter);
                    case "3":
                        return canHaveLasers(yCounter,xCounter);
                    case "4":
                        return canHaveLasers(yCounter,xCounter);
                    case ".":
                        freeSpace=true;
                }
                xCounter++;
            }
         yCounter++;
        }
        if(this.isGoal()){
            return true;
        }
        return freeSpace;
    }

    @Override
    public boolean isGoal() {
        return this.model.verifyModel();
    }

    public String toString(){
        return this.model.toString();
    }
}