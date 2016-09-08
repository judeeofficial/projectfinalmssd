package mssd.com.project;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pingpongofficial on 5/8/2559.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
