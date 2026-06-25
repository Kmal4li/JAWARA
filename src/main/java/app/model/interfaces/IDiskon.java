package app.model.interfaces;


public interface IDiskon {
    int hitungDiskon(int totalBelanja);
    boolean isValid();
    boolean getStatus();
    void setStatus(boolean status);
}
