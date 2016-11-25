package eyesatop.pipeapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import dji.sdk.sdkmanager.DJISDKManager;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by ben on 11/24/16.
 */
public abstract class DroneControllerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        final DroneController controller = createController();


        new NanoHTTPD("0.0.0.0", 8080) {
            {
                try {
                    start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Response serve(IHTTPSession session) {
                try {
                    switch (session.getUri()) {
                        case "/latitude":
                            switch (session.getMethod()) {
                                case GET: return newFixedLengthResponse(mapper.writeValueAsString(controller.getLatitude()));
                                case PUT:
                                    try {
                                        controller.setLatitude(mapper.readValue(session.getInputStream(), SetLatitudeRequest.class));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return newFixedLengthResponse("OK");
                            }
                        case "/functions/takeOff":
                            switch (session.getMethod()) {
                                case POST:
                                    controller.takeOff(mapper.readValue(session.getInputStream(), TakeOffRequest.class));
                            }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.serve(session);
            }
        };

        return START_STICKY;
    }

    public abstract DroneController createController();
}
