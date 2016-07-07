package com.aiyiqi.lib.map;

/**
 * Created by hubing on 16/3/17.
 */
public class MapFactory {
    private static  Object objClock = new Object();
    private static MapFactory mapFactory;
    private IMap iMap;
    private MapFactory(){
        iMap = new AmapImpl();
    };
    public static MapFactory getInstance(){
        synchronized (objClock){
            if(mapFactory == null){
                mapFactory = new MapFactory();
            }
            return mapFactory;
        }
    }

    public IMap getMapInstance(){
        return iMap;
    }
}
