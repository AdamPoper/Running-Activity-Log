public class Activity {
    protected float totalDistance;  // can be either miles or kilometer
    protected Time totalTime;  // total time spent running
    protected Date date;     // date on which the activity happened
    public static String units[] = {"Miles", "Kilometers", "Meters", "None"};   // units might change later
    public static String typesString[] = {"RUN", "WORKOUT"};
    protected int activityID = 0;
    private static int numEntries = 0;
    protected String description;
    protected String typeString;
    protected Type typeEnum;
    protected Unit unit;
    protected String unitString;
    public static enum Unit{
        MILES, KILOMETERS, METERS, NONE
    }
    public static enum Type{
        RUN, WORKOUT
    }
    Activity(){}

    Activity(float distance){   // for debugging
        this.totalDistance = distance;
    }
    public float getDistance(){
        return totalDistance;
    }
    public Time getTotalTime(){
        return totalTime;
    }
    public Date getDate(){
        return date;
    }
    public void addDescription(String desc){
        this.description = desc.replace('_', ' ');
    }
    public String getDescription(){
        return description;
    }
    public boolean compareToGreaterThan(Activity test){
        if(this.date.getYear() > test.getDate().getYear()){
            return true;
        } else if(this.date.getYear() == test.getDate().getYear()){
            if(Date.compareMonthsGreaterThan(this.date.getMonth(), test.getDate().getMonth())){
                return true;
            } else if(this.date.getMonth().equals(test.getDate().getMonth())){
                if(this.date.getDay() > test.getDate().getDay()){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean compareToLessThan(Activity test){
        if(this.date.getYear() < test.getDate().getYear()){
            return true;
        } else if(this.date.getYear() == test.getDate().getYear()){
            if(Date.compareMonthsLessThan(this.date.getMonth(), test.getDate().getMonth())){
                return true;
            } else if(this.date.getMonth().equals(test.getDate().getMonth())){
                if(this.date.getDay() < test.getDate().getDay()){
                    return true;
                }
            }
        }
        return false;
    }
    public static void setNumEntries(int num){
        numEntries = num;
    }
    public static int assignNewID(){
        numEntries++;
        return numEntries;
    }
    public void setID(int id){
        this.activityID = id;
    }
    public int getID(){
        return this.activityID;
    }
    public void setTypeAsString(String type){
        this.typeString = type;
        switch(typeString){
            case "RUN": this.typeEnum = Activity.Type.RUN;
            break;
            case "WORKOUT": this.typeEnum = Activity.Type.WORKOUT;
            break;
            default:
            System.out.println("Invalid Enum");
        }
    }
    public Type getType(){
        return this.typeEnum;
    }
    public String getTypeAsString(){
        return this.typeString;
    }
    public String fileOutputAsString(){return new String();}
    public void setDate(Date date){
        this.date = date;
    }
    public void setDistance(float dist){
        this.totalDistance = dist;
    }
    public void setDistanceUnit(String unit){
        this.unitString = unit;
    }
    public void setTime(Time time){
        this.totalTime = time;
    }
}