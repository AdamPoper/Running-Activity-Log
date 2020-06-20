public class RunActivity extends Activity {
    private Time avgPace;
    
    RunActivity(){this.typeString = Activity.typesString[0];}
    RunActivity(Time totalTime, float distance, String unit, Date date){
        this.totalTime = totalTime;
        this.totalDistance = distance;
        this.unitString = unit;
        this.date = date;
        this.typeString = Activity.typesString[0];
        calcPace();
    }
    RunActivity(Time totalTime, float distance, Unit unit, Date date){
        this.totalTime = totalTime;
        this.totalDistance = distance;
        this.date = date;
        this.unit = unit;
        switch(unit){
            case MILES: unitString = "Miles";
            break;
            case KILOMETERS: unitString = "Kilometers";
            break;
            case METERS: unitString = "Meters";
            break;
            case NONE: unitString = "None";
        }
        this.typeString = Activity.typesString[0];
        calcPace();
    }
    public String getUnit(){
        return unitString;
    }
    public void calcPace(){
        int minutes;
        float seconds;
        float avgPaceSecs = totalTime.toSeconds() / totalDistance;
        avgPaceSecs = this.unitString.equals(Activity.units[2]) ? avgPaceSecs * 1000.0f : avgPaceSecs * 1.0f;
        int avgMin = (int)avgPaceSecs / 60;
        float avgSecs = (float)(avgPaceSecs % 60.0f);
        avgPace = new Time(0, avgMin, avgSecs);
    }
    public Time getPace(){
        return avgPace;
    }
    @Override
    public String toString(){   //  neccessary ovverride of toString()
        String tempUnit = "";
        switch(this.unitString){
            case "Miles": tempUnit = "Mile";
            break;
            case "Kilometers": tempUnit = "Km";
            break;
            case "Meters": tempUnit = "Km";
            break;
        }
        String data = "Date: " + date.getMonth() + ' ' + date.getDay() + ' ' + date.getYear() + '\n'
                    + "Distance: " + totalDistance + ' ' + unitString + ' ' + '\n'
                    + "Time: " + totalTime.getHours() + " : " + totalTime.getMinutes() + " : " + totalTime.getSeconds() + '\n' 
                    + "Pace: " + avgPace.getMinutes() + " : " + avgPace.getSeconds() + " Per " + tempUnit + '\n'
                    + description;
        return data;
    }
    @Override
    public String fileOutputAsString(){
        String tempDescription = this.description.replace(' ', '_');
        String output = "" + this.activityID + '\n'
                           + this.typeString + '\n'
                           + date.getMonth() + ' ' + date.getDay() + ' ' + date.getYear() + '\n'
                           + this.totalDistance + ' ' + this.unitString + '\n'
                           + totalTime.getHours() + ' ' + totalTime.getMinutes() + ' ' + totalTime.getSeconds() + '\n'
                           + tempDescription + '\n';
        return output;

    }
}