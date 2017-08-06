//Java class for corresponding JSON model
//Special thanks to: jsonschema2pojo by Joe Littlejohn.
package gleambot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastFM {

    @SerializedName("recenttracks")
    @Expose
    private Recenttracks recenttracks;

    public Recenttracks getRecenttracks() {
        return recenttracks;
    }

    public void setRecenttracks(Recenttracks recenttracks) {
        this.recenttracks = recenttracks;
    }

}
