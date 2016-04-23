package eu.isdc.contrails.test;

/**
 * Created by Raluca.Stroia on 4/23/2016.
 */
public class TestRest {

    private final long id;
    private final String content;

    public TestRest(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}