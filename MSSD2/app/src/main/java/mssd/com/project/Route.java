package mssd.com.project;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;



/**
 * Created by pingpongofficial on 5/8/2559.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public LatLng endLocationdistrict;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
