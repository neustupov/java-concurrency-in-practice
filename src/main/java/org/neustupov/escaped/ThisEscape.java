package org.neustupov.escaped;

import java.awt.*;
import java.util.EventListener;

public class ThisEscape {
    public ThisEscape(EventSource source){
        source.registerListener(new EventListener(){
            public void onEvent(Event e){
                System.out.println(e);
            }
        });
    }
}
