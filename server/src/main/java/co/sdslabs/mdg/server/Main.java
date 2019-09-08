package co.sdslabs.mdg.server;

public class Main {
    public static void main(String[] argv) throws Exception {

        System.out.println("Start!!");
        Driver.setup();
        Global.setup();
        RegisterNames.register();

        // Loop calling processEvents until a callback sets enabled[0] = false.

    }


}