import java.util.List;

public class Response {
    public List<Hits> getHits() {
        return hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }

    private List<Hits> hits;

}
