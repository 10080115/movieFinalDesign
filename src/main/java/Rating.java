/**
 * Created by lishiwei on 16/12/25.
 */
public class Rating {
    private String userId;
    private String movieId;
    private String timestamp;
    private String rating;

    Rating(String userId, String movieId, String rating, String timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRating() {
        return rating;
    }
}
