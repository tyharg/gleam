//Java class for corresponding JSON model
//Special thanks to: jsonschema2pojo by Joe Littlejohn.
package gleambot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("mbid")
    @Expose
    private String mbid;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

}
