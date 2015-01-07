package example.api.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Greeting {

    private long id;
    private String content;
    private String description;

    public String getDescription() {
		return description;
	}

	public Greeting()
    {

    }

    public Greeting(long id, String content, String description) {
    	this.id = id;
    	this.content = content;
    	this.description = description;
    }

    public Greeting( String content, String description) {
        this(0,content,description);
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String toAdd)
    {
    	content=toAdd;
    }
}
