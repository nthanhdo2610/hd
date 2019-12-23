package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Converter implements RequestHandler<String,String> {

    public String handleRequest(String name, Context context) {
        return String.format("Hello %s.", name);
    }
}
