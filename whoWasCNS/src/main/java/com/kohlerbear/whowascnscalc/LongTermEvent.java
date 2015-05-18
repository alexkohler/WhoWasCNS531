package com.kohlerbear.whowascnscalc;

/**
 * Created by alex on 5/12/15.
 */


public class LongTermEvent {
    private String m_liftDate     = null;
    private String m_cycle        = null;
    private String m_liftType     = null;
    private String m_liftName     = null; //may be the compound or accessory
    private String m_frequency    = null;
    private double m_weight       = 0.0;
    private int m_reps            = 0 ;
    private double m_theoreticalOneRepMax = 0.0;
    private boolean m_lbsFlag     = true;



    public String getLiftName() {
        return m_liftName;
    }
    public void setLiftName(String n) {m_liftName = n;}

    public boolean getLbsFlag() {
        return m_lbsFlag;
    }
    public void setLbsFlag(boolean b) {m_lbsFlag = b;}

    public String getFrequency() {
        return m_frequency;
    }
    public void setFrequency(String f) {m_frequency = f;}

    public String getCycle() {
        return m_cycle;
    }
    public void setCycle (String cycle) { m_cycle = cycle;}



    public String getLiftDate() {return m_liftDate;}
    public void setLiftDate(String d) {m_liftDate = d;}



    public String getLiftType() {
        return m_liftType;
    }
    public void setLiftType (String t) {m_liftType = t;}

    public double getWeight() {
        return m_weight;
    }
    public void setWeight(double w) { m_weight= w;}

    public int getReps() {
        return m_reps;
    }
    public void setReps(int reps) { m_reps = reps;}

    public double getTheoreticalOneRepMax() {
        return m_theoreticalOneRepMax;
    }


    public LongTermEvent(){
        //for cleanliness in getLongTermEvents (In the SQL helper)
    }

    public LongTermEvent(String liftDate, String cycle, String liftType, String liftName, String frequency, double weight, int reps, boolean lbs)
    {
        m_liftDate = liftDate;
        m_cycle = cycle;
        m_liftType = liftType;
        m_liftName = liftName;
        m_frequency = frequency;
        m_weight = weight;
        m_reps = reps;
        m_theoreticalOneRepMax = reps * weight; //TODO make this calculation correct.
        m_lbsFlag = lbs;
    }

    @Override
    public String toString() {
        return m_liftDate + "- [" /*+ m_liftType +*/ + m_liftName + "-" + m_frequency + "]";
    }
}
