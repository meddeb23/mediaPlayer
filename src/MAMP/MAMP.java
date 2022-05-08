package MAMP;

import java.io.Serializable;

public class MAMP implements Serializable {
    private String resource;
    private String data;
    private Object body;

    public MAMP(String resource, String data, Object body) {
        this.resource = resource;
        this.data = data;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}
