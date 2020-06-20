import java.time.LocalDate;
public class Date {
    public static String months[] = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                                     "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    public static int monthOrders[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private int day;
    private int year;
    private String month;
    
    Date(){
    }      
    Date(String month, int day, int year){
        this.month = month;
        this.day = day;
        this.year = year;
    }
    public void setDate(String month, int day, int year){
        this.month = month;
        this.day = day;
        this.year = year;
    }      
    public void setMonth(String month){
        this.month = month;
    } 
    public void setYear(int year){
        this.year = year;
    }              
    public void setDay(int day){
        if(day > 31 || day < 1){
            // throw an error some where
            System.out.println("Not a valid day");
        } else{
            this.day = day;
        }
    }
    public String getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
    public int getYear(){
        return year;
    }
    public Date getDate(){
        return this;
    }
    public void makeCurrent(){
        LocalDate current = LocalDate.now();
        this.day = current.getDayOfMonth();
        this.month = current.getMonth().toString();
        this.year = current.getYear();
    }
    public static boolean compareMonthsGreaterThan(String month1, String month2){
        int mloc1 = 0;
        int mloc2 = 0;
        for(int i = 0; i < months.length; i++){
            if(months[i] == month1){
                mloc1 = i;
            }
            if(months[i] == month2){
                mloc2 = i;
            }
        }
        if(mloc1 > mloc2){
            return true;
        } else {
            return false;
        }
    }
    public static boolean compareMonthsLessThan(String month1, String month2){
        int mloc1 = 0;
        int mloc2 = 0;
        for(int i = 0; i < months.length; i++){
            if(months[i] == month1){
                mloc1 = i;
            }
            if(months[i] == month2){
                mloc2 = i;
            }
        }
        if(mloc1 < mloc2){
            return true;
        } else {
            return false;
        }
    }
}