package tedonttouchthewhitetile;

public class TeDontTouchTheWhiteTile {
    public static TeDontTouchTheWhiteTile game = new TeDontTouchTheWhiteTile();
    public void go(){
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(model, controller);
        model.addView(view);
    }
    public static void main(String[] args) {
        game.go();
    }
    
}
