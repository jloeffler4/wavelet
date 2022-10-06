import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    //Tracks all data added to the searchable results
    ArrayList<String> searchableData = new ArrayList<String>();

    private String getQueryParameter(URI url, String key) {
        //Get the URL query
        String query = url.getQuery();
        if (query == null) return null;

        //Check each key-value pair
        String[] kvPairs = query.split("&");
        for (String kvPair : kvPairs) {
            String[] splitPair = kvPair.split("=");

            //Split pair into its components
            String pairKey = splitPair[0];
            String pairValue = splitPair[1];

            //Return the value for the matching key
            if (pairKey.equals(key)) return pairValue;
        }

        //Can return null if the key wasn't found (or no query component)
        return null;
    }

    public String handleRequest(URI url) {
        switch (url.getPath()) {
            case "/": {
                return "Data: " + searchableData;
            }
            case "/add": {
                //Get the s parameter
                String s = getQueryParameter(url, "s");
                if (s == null) return "400 Bad Request";

                //Add the data to the list of searchable items
                searchableData.add(s);

                return "Data added successfully";
            }
            case "/search": {
                //Get the s parameter
                String s = getQueryParameter(url, "s");
                if (s == null) return "400 Bad Request";

                //Search for results
                ArrayList<String> results = new ArrayList<String>();
                for (String data : searchableData) {
                    if (data.contains(s)) results.add(data);
                }

                //Return results
                return "Found results: " + results;
            }
            default: {
                return "404 Not Found";
            }
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}