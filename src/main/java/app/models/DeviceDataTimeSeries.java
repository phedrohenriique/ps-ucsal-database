package app.models;

import java.sql.Timestamp;

public class DeviceDataTimeSeries {

    Integer IdDevice;
    Timestamp TrMin;
    Float AvgTemperature;
    Float AvgWindSpeedMs;
    Float AvgEnergyMw;

    public DeviceDataTimeSeries(
            Integer IdDevice,
            Timestamp TrMin,
            Float AvgTemperature,
            Float AvgWindSpeedMs,
            Float AvgEnergyMw
    ) {
        this.IdDevice = IdDevice;
        this.TrMin = TrMin;
        this.AvgTemperature = AvgTemperature;
        this.AvgWindSpeedMs = AvgWindSpeedMs;
        this.AvgEnergyMw = AvgEnergyMw;
    }

    public Integer getIdDevice() {
        return IdDevice;
    }

    public Timestamp getTrMin(){
        return TrMin;
    }

    public Float getAvgTemperature() {
        return AvgTemperature;
    }

    public Float getAvgWindSpeedMs(){
        return AvgWindSpeedMs;
    }

    public Float getAvgEnergyMw(){
        return AvgEnergyMw;
    }




    

}
