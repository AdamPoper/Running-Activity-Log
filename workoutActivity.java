import java.util.ArrayList;

public class workoutActivity extends Activity {
   private int numSplits = 0;
   private float distancePerSplit = 0.0f;
   private ArrayList<Time> splits;
   private Time avgSplitTime;
   //Unit unit;
   //String unitString;
   // default
   workoutActivity(){ splits  = new ArrayList<>(); this.typeString = Activity.typesString[1];}
   // constructor
   workoutActivity(int numSplits, float distance, Date date){
        this.numSplits = numSplits;
        this.date = date;
        this.distancePerSplit = distance;
        this.splits = new ArrayList(this.numSplits);
   }
   // other constructor
   workoutActivity(int numSplits, float distance, Date date, ArrayList<Time> splits, Unit unit){
      this.numSplits = numSplits;
      this.date = date;
      this.distancePerSplit = distance;
      //this.splits = new ArrayList(this.numSplits);
      this.splits = splits;
      this.unit = unit;
      switch(unit){
         case MILES: unitString = "Miles";
         break;
         case KILOMETERS: unitString = "Kilometers";
         break;
         case METERS: unitString = "Meters";
         break;
      }
      this.typeString = Activity.typesString[1];
      calcAvg();
   }
   // other other constructor
   workoutActivity(int numSplits, float distance, Date date, ArrayList<Time> splits, String unit){
      this.numSplits = numSplits;
      this.date = date;
      this.distancePerSplit = distance;
      //this.splits = new ArrayList(this.numSplits);
      this.splits = splits;
      this.unitString = unit;
      switch(unitString){
         case "Miles": this.unit = Unit.MILES;
         break;
         case "Kilometers": this.unit = Unit.KILOMETERS;
         break;
         case "Meters": this.unit = Unit.METERS;
         break;
      }
      this.typeString = Activity.typesString[1];
      calcAvg();
   }
   @Override                  // toString override
   public String toString(){
      String tempUnit = "";
      switch(this.unitString){
         case "Miles": tempUnit = "Mile";
         break;
         case "Kilometers": tempUnit = "Km";
         break;
         case "Meters": tempUnit = "Meters";
      }
      // implement a string to return
      String data = "Date: " + this.date.getMonth() + ' ' + this.date.getDay() + ' ' + this.date.getYear() + '\n'
      + ' ' + numSplits + " x " + distancePerSplit + ' ' + unitString + '\n' 
      + " avg: " + avgSplitTime.getMinutes() + ':' + avgSplitTime.getSeconds() + " Per " + distancePerSplit + ' ' + tempUnit
      + '\n' + description + '\n';
      for(int i = 0; i < splits.size(); i++){
         data += "" + splits.get(i).getMinutes() + ' ' + splits.get(i).getSeconds() + '\n';
      }
      return data;
   }
   public void calcAvg(){
      float totalSecs = 0.0f;
      for(int i = 0; i < splits.size(); i++){
         totalSecs += splits.get(i).toSeconds();
      }
      float avgTotalSecs = totalSecs / (float)numSplits;
      int avgMins = (int)avgTotalSecs / 60;
      float avgSecs = avgTotalSecs % 60;
      avgSplitTime = new Time(avgMins, avgSecs);
   }
   public Time getAveragePace(){
      return avgSplitTime;
   }
   public int getNumSplits(){
      return numSplits;
   }
   public float getSplitDistance(){
      return distancePerSplit;
   }
   public String getUnit(){
      return unitString;
   }
   public void calcTotalDist(){
      totalDistance = distancePerSplit * numSplits;
   }
   @Override
   public String fileOutputAsString(){
      String tempDescription = this.description.replace(' ', '_');
      String output = "" + activityID + '\n' 
                         + typeString + '\n'
                         + date.getMonth() + ' ' + date.getDay() + ' ' + date.getYear() + '\n'
                         + numSplits + ' ' + distancePerSplit + ' ' + unitString + '\n';
      for(int i = 0; i < splits.size(); i++){
         output += "" + splits.get(i).getMinutes() + ' ' + splits.get(i).getSeconds() + ' ';
      }
      output += '\n' + tempDescription + '\n';
      return output;        // debug tomorrow sleepy time
   }
   public void setNumSplits(int num){
      this.numSplits = num;
   }
   public void setSplitDistance(float dist){
      this.distancePerSplit = dist;
   }
   public ArrayList<Time> getActualSplits(){
      return splits;
   }
   public void parseSplits(String rawData){
      String tempData[] = rawData.split("\n");
      System.out.println("parsed data:");
      this.splits.clear();
      for(int i = 0; i < tempData.length; i++){
        // System.out.println(tempData[i]);
         String tempSplitData[] = tempData[i].split(" ");
         int mins = Integer.parseInt(tempSplitData[0]);
         float secs = Float.parseFloat(tempSplitData[1]);
         this.splits.add(new Time(mins, secs));
      }
      for(int i = 0; i < splits.size(); i++){
         System.out.println(splits.get(i).toString());
      }
   }
}