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
            return String.format("Ethan's Number: %d", num);
        } else if (url.getPath().equals("/increment")) {
            num += 1;
            return String.format("Number incremented!");
        } else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("count")) {
                    num += Integer.parseInt(parameters[1]);
                    return String.format("Number increased by %s! It's now %d", parameters[1], num);
                }
                else if (parameters[0].equals("s")) {
                    incrementList();
                    list[size] = parameters[1];
                    size++;
                    return String.format(parameters[1] + " has been added to the list!");
                }
            }
            else if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    String results = "";
                    for (int i = 0; i < size; i++) {
                        if (i > 0) {
                            results += ", ";
                        }
                        if (list[i].contains(parameters[1])) {
                             results += list[i];
                            }
                        }
                        return String.format(results);
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

public class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
