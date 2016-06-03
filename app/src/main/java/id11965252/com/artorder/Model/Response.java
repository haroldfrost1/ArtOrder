package id11965252.com.artorder.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Response class
 * Simulates a http response from REST server
 */
@Root
public class Response {

    @Element
    private int result;

    @Element
    private String message;

    public Response(){

    }

    public Response(int result, String message){
        this.result = result;
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public int getResult(){
        return this.result;
    }

    public void setResult(int result){
        this.result = result;
    }
}
