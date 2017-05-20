package tedonttouchthewhitetile;

public class Controller {
    Model model;

    public Controller(Model model) {
        this.model = model;
    }
    
    public void click(int x, int y){
        System.out.println("MouseClick");
        model.click(x, y);
    }
    
    public void newGame(){
        model.prepareNewGame();
    }
    
    public void printInfo(){
        model.printInfo();
    }
}
