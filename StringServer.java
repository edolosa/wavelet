import java.io.IOException;
import java.net.URI;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    int size = 0;
    String[] list = new String[5];

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            if (size == 0) {
                return "Start adding messages and they will appear here!";
            }
            else {
                String output = "";
                for (int i = 0; i < size; i++) {
                    output += list[i] + "\n";
                }
                return output;
            }
            
        } else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add-message")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    incrementList();
                    list[size] = parameters[1];
                    size++;
                    String output = "";
                    for (int i = 0; i < size; i++) {
                        output += list[i] + "\n";
                    }
                    return output;
                }
            }
            return "404 Not Found!";
        }
    }
    public void incrementList() {
        if (size >= list.length/1.25) {
            String[] copy = new String[size*2];
            for (int i = 0; i < size; i++) {
                copy[i] = list[i];
            }
            list = copy;
        }
        else {
            return;
        }
    }
}

public class StringServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
