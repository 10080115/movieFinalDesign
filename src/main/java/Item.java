import java.util.List;

/**
 * Created by lishiwei on 16/12/24.
 */
public class Item {

    public Item(String id, String movieName, List<String> genersList) {
        this.id = id;
        this.movieName = movieName;
        this.genersList = genersList;
    }

    public String getMovieName() {
        return movieName;
    }


    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getId() {
        return id;
    }

    public List<String> genersList() {
        return genersList;
    }

    private String id;
    private String movieName;
    private int hot;
    private List<String> genersList;
}
