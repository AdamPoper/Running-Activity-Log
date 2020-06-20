public class Time {
    private int minutes = 0;
    private int hours = 0;
    private float seconds = 0.0f;
    Time(){}
    Time(int hours, int minutes, float seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    Time(int minutes, float seconds){
        this.minutes = minutes;
        this.seconds = seconds;
    }
    Time(float seconds){
        this.seconds = seconds;
    }
    public void setTime(int hours, int minutes, float seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public void setTime(int minutes, float seconds){
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public void setTime(float seconds){
        this.seconds = seconds;
    }
    public Time getTime(){
        return this;
    }
    public int getHours(){
        return hours;
    }
    public int getMinutes(){
        return minutes;
    }
    public float getSeconds(){
        return seconds;
    }
    public float toSeconds(){
        float hoursAsSeconds = hours * 60.0f * 60.0f;
        float minutesAsSeconds = minutes * 60.0f;
        float total = hoursAsSeconds + minutesAsSeconds + seconds;
        return total;
    }
    @Override
    public String toString(){
        String data = "" + hours + ' ' + minutes + ' ' + seconds;
        return data;
    }
}