package ChatShare;

import java.io.Serializable;

public class Request implements Serializable {

    public RequestType requestType;

    public String Message;

    public String username;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public Request(RequestType requestType,String msg,String username) {
        this.requestType = requestType;
        this.Message = msg;
        this.username = username;
    }

    public Request(RequestType requestType,String username) {
        this.requestType = requestType;
        this.username = username;
    }

}
