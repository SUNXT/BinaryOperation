package utils;

public class Log {

    public static void d(String msg){
        System.out.println(msg);
    }

    public static void println(){System.out.println();}

    public static void print(String s){System.out.print(s);}

    public static void d(String name, int[] arrays){
        print(name + ": ");
        for (int i : arrays){
            print(i + "");
        }
        println();
    }

}
