/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
package com.cepmuvakkit.times.posAlgo;

/**
 *
 * @author mehmetrg
 */
public class Ecliptic {

    public double λ; //λ the ecliptic longitude
    public double β; //β the ecliptic latitude
    public double Δ; //distance  in km

    Ecliptic() {
    }

    Ecliptic(double longitude, double latitude) {
        this.λ = longitude;
        this.β = latitude;
    }

    Ecliptic(double longitude, double latitude, double radius) {
        this.λ = longitude;
        this.β = latitude;
        this.Δ = radius;
    }
}


