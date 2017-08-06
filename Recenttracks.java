//Java class for corresponding JSON model
//Special thanks to: jsonschema2pojo by Joe Littlejohn.
package gleambot;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recenttracks {
    @SerializedName("track")
    @Expose
    private List<Track> track = null;
    
    public List<Track> getTrack() {
        return track;
    }
    public void setTrack(List<Track> track) {
        this.track = track;
    }
}
