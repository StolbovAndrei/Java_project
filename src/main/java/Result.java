import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
    @JsonProperty("artist_names")
    private String artistNames;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(String artistNames) {
        this.artistNames = artistNames;
    }
    @JsonProperty("title")
    private String title;

}
