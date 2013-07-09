/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
package com.cepmuvakkit.times.posAlgo;

/**
 *
 * @author mgeden
 */
public class EarthHeading {
    
    private double mHeading;
    private long mMetres;

    public EarthHeading(double heading, long metres) {
        mHeading = heading;
        mMetres = metres;
    }
    
    public double getHeading() {
        return mHeading;
    }
    
    public long getKiloMetres() {
        return mMetres/1000;
    }
}
