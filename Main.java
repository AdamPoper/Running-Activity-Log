import javax.swing.*;
import jdk.jfr.Description;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.time.LocalDate;
public class Main {
    static Formatter formatfile;
    static File mainFile;
    static FileWriter writer;
    static Scanner input;
    static JFrame mainFrame;
    static JPanel mainPanel;
    static JButton addButt;
    static JTextField field;
    static ArrayList<Activity> activityList = new ArrayList<>();   // list of all the activities
    static JList recentActivities;  // Jlist for displaying the activities
    static DefaultListModel<Activity> activityModel;   // model for the Activities JList
    static final String fileName     = "activityDatabase.txt";
    static final String metaDataFile = "metaData.txt";
    static int numberOfEntries = 0;
    static JTextPane viewPane;
    static JScrollPane viewJSP;
    static JLabel filterlabel;
    static JComboBox filterMonthsCBox;
    static JLabel filterYearLabel;
    static JTextField filterYearField;
    static JButton filterSearchButt;
    static JLabel sortLabel;
    static JComboBox sortBox;
    static final String sortOptions[] = {"Ascending", "Decending"};
    static JButton sortButton;
    static JButton modifyEntryButt;
    static int selectedActivityIndex[] = {-1};   // has to be an array with one element in it to be accessed by the mouseEvent
    static JButton restoreButt;
    static JButton deleteButt;
    static JScrollPane activityJSP;
    public static void main(String[] args) throws Exception {
        mainFrame = new JFrame("Running Activity Environment");
        mainPanel = new JPanel();
        addButt = new JButton("Add New Activity");
        addButt.setBounds(20, 20, 140, 25);
        addButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addActivity();
            }
        });
        viewPane = new JTextPane();
        viewJSP = new JScrollPane(viewPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        viewJSP.setBounds(700, 170, 220, 150);  // only need to set bounds for the JSP becasue it contains the textpane
        filterlabel = new JLabel("Filter by month and year");
        filterlabel.setBounds(700, 30, 160, 20);
        filterMonthsCBox = new JComboBox(Date.months);
        filterMonthsCBox.setBounds(700, 55, 120, 25);
        filterYearLabel = new JLabel("Year: ");
        filterYearLabel.setBounds(830, 55, 50, 20);
        filterYearField = new JTextField();
        filterYearField.setBounds(860, 55, 65, 25);
        filterSearchButt = new JButton("Search");
        filterSearchButt.setBounds(720, 95, 80, 25);
        sortLabel = new JLabel("Sort by: ");
        sortLabel.setBounds(300, 10, 80, 20);
        sortBox = new JComboBox(sortOptions);
        sortBox.setBounds(360, 10, 120, 25);
        sortButton = new JButton("Sort");
        sortButton.setBounds(490, 10, 70, 25);
        modifyEntryButt = new JButton("Modify Entry");
        modifyEntryButt.setBounds(700, 140, 120, 20);
        modifyEntryButt.setEnabled(true);   // set to true for debugging  change to false
        filterSearchButt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                searchDataBase(Date.months[filterMonthsCBox.getSelectedIndex()],
                Integer.parseInt(filterYearField.getText()));
            }
        });
        // sorting activities in ascending/descending order
        sortButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                switch(sortBox.getSelectedIndex()){
                    case 0: sortAscending();
                    break;
                    case 1: sortDescending();
                    break;
                }
            }
        });
        modifyEntryButt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                modifyActivity();
            }
        });
        restoreButt = new JButton("Restore All");
        restoreButt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                readDataBase();
                updateModel();
            }
        });
        restoreButt.setBounds(570, 12, 75, 20);
        deleteButt = new JButton("Delete Entry");
        deleteButt.setBounds(830, 140, 120, 20);
        deleteButt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                activityList.remove(selectedActivityIndex[0]);
                rewriteActivityData();
                updateMetaData(false);
                updateModel();
            }
        });
        readDataBase();
        displayRecents();
        initConversions();
        mainPanel.add(addButt);
        mainPanel.add(viewJSP);
        mainPanel.add(filterlabel);
        mainPanel.add(filterMonthsCBox);
        mainPanel.add(filterYearLabel);
        mainPanel.add(filterYearField);
        mainPanel.add(filterSearchButt);
        mainPanel.add(sortLabel);
        mainPanel.add(sortButton);
        mainPanel.add(sortBox);
        mainPanel.add(modifyEntryButt);
        mainPanel.add(restoreButt);
        mainPanel.add(deleteButt);
        mainPanel.setLayout(null);
        mainFrame.add(mainPanel);
        mainFrame.setSize(new Dimension(960, 540));
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void addActivity() {
        Date tempDate[] = { new Date() };
        ArrayList<Time> splitTimes = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        JDialog newActivityDialog = new JDialog(mainFrame, "Add A New Activity");
        newActivityDialog.setSize(new Dimension(310, 420));
        JButton implActivity = new JButton("Add To Log");
        JComboBox monthPicker = new JComboBox(Date.months);
        monthPicker.setBounds(10, 35, 120, 30);
        JLabel dayLabel = new JLabel("Day: ");
        dayLabel.setBounds(140, 40, 40, 20);
        JTextField dayField = new JTextField();
        dayField.setBounds(170, 40, 30, 20);
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setBounds(210, 40, 40, 20);
        JTextField yearField = new JTextField();
        yearField.setBounds(245, 40, 50, 20);
        yearField.setText(Integer.toString(currentDate.getYear()));
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(10, 80, 35, 25);
        JTextField hoursField = new JTextField();
        hoursField.setBounds(55, 85, 35, 25);
        JTextField minutesField = new JTextField();
        minutesField.setBounds(85, 85, 35, 25);
        JTextField secondsField = new JTextField();
        secondsField.setBounds(115, 85, 35, 25);
        JLabel distanceLabel = new JLabel("Distance:");
        distanceLabel.setBounds(10, 105, 65, 30);
        JTextField distanceField = new JTextField();
        distanceField.setBounds(85, 110, 65, 25);
        JComboBox unitCBox = new JComboBox(Activity.units);
        unitCBox.setBounds(195, 130, 85, 30);
        JLabel unitLabel = new JLabel("Pick Unit");
        unitLabel.setBounds(205, 110, 90, 25);
        JCheckBox runCBox = new JCheckBox("Add a Run", false);
        runCBox.setBounds(10, 60, 130, 30);
        JLabel splitDistLabel = new JLabel("Split distance:");
        splitDistLabel.setBounds(10, 160, 90, 30);
        JTextField splitDistField = new JTextField();
        splitDistField.setBounds(110, 165, 50, 25);
        JLabel numSplitsLabel = new JLabel("How Many Splits? ");
        numSplitsLabel.setBounds(10, 195, 120, 25);
        JTextField numSplitsField = new JTextField();
        numSplitsField.setBounds(130, 195, 35, 25);
        JLabel splitTimesLabel = new JLabel("Times: ");
        splitTimesLabel.setBounds(10, 220, 50, 25);
        JTextField splitMinsField = new JTextField();
        splitMinsField.setBounds(65, 220, 40, 25);
        JTextField splitSecsField = new JTextField();
        splitSecsField.setBounds(100, 220, 40, 25);
        JLabel descriptionLabel = new JLabel("Activity Description:");
        descriptionLabel.setBounds(20, 245, 130, 25);
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBounds(20, 270, 240, 80);
        descriptionArea.setLineWrap(true);
        runCBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                if(ie.getStateChange() == 1){
                    splitDistField.setEnabled(false);
                    numSplitsField.setEnabled(false);
                    splitMinsField.setEnabled(false);
                    splitSecsField.setEnabled(false);
                }
                else{
                    splitDistField.setEnabled(true);
                    numSplitsField.setEnabled(true);
                    splitMinsField.setEnabled(true);
                    splitSecsField.setEnabled(true);
                }
            }
        });
        JCheckBox workoutCBox = new JCheckBox("Add a Workout");
        workoutCBox.setBounds(10, 140, 150, 30);
        workoutCBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                if(ie.getStateChange() == 1){
                    hoursField.setEnabled(false);
                    minutesField.setEnabled(false);
                    secondsField.setEnabled(false);
                    distanceField.setEnabled(false);
                }
                else{
                    hoursField.setEnabled(true);
                    minutesField.setEnabled(true);
                    secondsField.setEnabled(true);
                    distanceField.setEnabled(true);
                }
            }
        });
        splitMinsField.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                    Time tempTime;
                    System.out.println("Enter Pressed from Mins field");
                    try{
                        int mins = Integer.parseInt(splitMinsField.getText());
                        float secs = Float.parseFloat(splitSecsField.getText());
                        tempTime = new Time(mins, secs);
                        splitTimes.add(tempTime);
                        splitMinsField.setText("");
                        splitSecsField.setText("");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }                    
                }
            }
            public void keyReleased(KeyEvent ke){}
            public void keyTyped(KeyEvent ke){}
        });
        splitSecsField.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent ke){
                Time tempTime;
                // adding the individual split times
                if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                    System.out.println("Enter Pressed from Secs field");
                    try{
                        int mins = Integer.parseInt(splitMinsField.getText());
                        float secs = Float.parseFloat(splitSecsField.getText());
                        tempTime = new Time(mins, secs);
                        splitTimes.add(tempTime);
                        splitMinsField.setText("");
                        splitSecsField.setText("");
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            public void keyReleased(KeyEvent ke){}
            public void keyTyped(KeyEvent ke){}
        });
        JPanel newActivityPanel = new JPanel();
        newActivityPanel.setLayout(null);
        JCheckBox currentDateBox = new JCheckBox("Use Current Date", false);
        currentDateBox.setBounds(10, 10, 170, 30);
        currentDateBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == 1) {
                    tempDate[0] = new Date(currentDate.getMonth().toString(), currentDate.getDayOfMonth(),
                            currentDate.getYear());
                            monthPicker.setEnabled(false);
                            dayField.setEnabled(false);
                            yearField.setEnabled(false);
                } else {
                    tempDate[0] = new Date();
                    monthPicker.setEnabled(true);
                    dayField.setEnabled(true);
                    yearField.setEnabled(true);
                }
            }
        });
        // adding a new activity button 
        implActivity.setBounds(80, 365, 100, 25);        
        implActivity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // what unit is the activity performed using
                Activity.Unit runUnit = Activity.Unit.NONE;
                String description = descriptionArea.getText().isEmpty() ? "NONE" : descriptionArea.getText();
                description = description.replace(' ', '_');    // this makes the description a spaceless string to be read as a single string
                switch(unitCBox.getSelectedIndex()){
                    case 0: runUnit = Activity.Unit.MILES;
                    break;
                    case 1: runUnit = Activity.Unit.KILOMETERS;
                    break;
                    case 2: runUnit = Activity.Unit.METERS;
                    break;
                }
                // writing data for a regular run
                if (runCBox.isSelected()){
                    // get all the info for a run
                    RunActivity run;
                    Time runTime = new Time(Integer.parseInt(hoursField.getText()), 
                    Integer.parseInt(minutesField.getText()), Float.parseFloat(secondsField.getText()));
                    if(monthPicker.isEnabled()){
                        tempDate[0] = new Date(Date.months[monthPicker.getSelectedIndex()], 
                        Integer.parseInt(dayField.getText()), Integer.parseInt(yearField.getText()));
                    } 
                    run = new RunActivity(runTime, Float.parseFloat(distanceField.getText()), runUnit, tempDate[0]);
                    // write all the data for a run
                    try {
                        mainFile = new File(fileName);
                        writer = new FileWriter(mainFile, true);                                           
                        run.addDescription(description);
                        run.setTypeAsString(Activity.typesString[0]);
                        run.setID(Activity.assignNewID());
                        writer.write(run.fileOutputAsString());
                        activityList.add(0, run);
                        updateMetaData(true);
                        writer.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }                    
                }
                // to add a workout
                else if(workoutCBox.isSelected()){
                    workoutActivity tempWO;
                    // date
                    if(monthPicker.isEnabled()){   // override the date object if the user selects a custom date
                        tempDate[0] = new Date(Date.months[monthPicker.getSelectedIndex()], 
                    Integer.parseInt(dayField.getText()), Integer.parseInt(yearField.getText()));
                    }
                    tempWO = new workoutActivity(Integer.parseInt(numSplitsField.getText()), Float.parseFloat(splitDistField.getText()), tempDate[0], splitTimes, runUnit);
                    // write all the data
                    try{
                        mainFile = new File(fileName);   // file
                        writer = new FileWriter(mainFile, true);    // file writer true is to append to the file
                        tempWO.addDescription(description);
                        tempWO.setTypeAsString(Activity.typesString[1]);
                        tempWO.setID(Activity.assignNewID());
                        writer.write(tempWO.fileOutputAsString());
                        activityList.add(0, tempWO);
                        updateMetaData(true);
                        writer.close();
                    } catch(Exception e){
                        e.printStackTrace();
                    }                   
                }
                updateRecents();
            }
        });       
        newActivityPanel.add(monthPicker);
        newActivityPanel.add(currentDateBox);
        newActivityPanel.add(implActivity);
        newActivityPanel.add(dayLabel);
        newActivityPanel.add(dayField);
        newActivityPanel.add(yearLabel);
        newActivityPanel.add(yearField);
        newActivityPanel.add(runCBox);
        newActivityPanel.add(workoutCBox);
        newActivityPanel.add(timeLabel);
        newActivityPanel.add(hoursField);
        newActivityPanel.add(minutesField);
        newActivityPanel.add(secondsField);
        newActivityPanel.add(distanceLabel);
        newActivityPanel.add(distanceField);
        newActivityPanel.add(unitCBox);
        newActivityPanel.add(unitLabel);
        newActivityPanel.add(splitDistLabel);
        newActivityPanel.add(splitDistField);
        newActivityPanel.add(numSplitsLabel);
        newActivityPanel.add(numSplitsField);
        newActivityPanel.add(splitMinsField);
        newActivityPanel.add(splitSecsField);
        newActivityPanel.add(splitTimesLabel);
        newActivityPanel.add(descriptionLabel);
        newActivityPanel.add(descriptionArea);
        newActivityDialog.add(newActivityPanel);
        newActivityDialog.setVisible(true);
    }
    public static void readDataBase(){
        if(!activityList.isEmpty()){
            activityList.clear();
        }
        try{
            mainFile = new File(fileName);
            input = new Scanner(mainFile);
        } catch(Exception e){
            e.printStackTrace();
        }
        // read all the data from a single entry and construct a new Activity object
        try{
            System.out.println("Reading Data");
            int readLimit = 0;    // might add later
            File metDatFile = new File(metaDataFile);
            Scanner metaDataScanner = new Scanner(metDatFile);
            /*number of activity entries is in a separate file is a crappy solution but easy solution*/ 
            numberOfEntries = Integer.parseInt(metaDataScanner.next());  // the number of entries is always the first thing read from the metadata file
            Activity.setNumEntries(numberOfEntries);
            System.out.println(numberOfEntries);   // debug
            metaDataScanner.close();
            while(input.hasNext()){
                String description = "";
                int activityID = Integer.parseInt(input.next());
                String type    = input.next();
                String month   = input.next();
                int day  = Integer.parseInt(input.next());
                int year = Integer.parseInt(input.next());
                switch(type){
                    case "RUN":  // add the activity to the array list if the type is RUN
                    float distance = Float.parseFloat(input.next());
                    String unit = input.next();
                    int hours   = Integer.parseInt(input.next());
                    int mins    = Integer.parseInt(input.next());
                    float secs  = Float.parseFloat(input.next());
                    description = input.next();
                    description = description.replace('_', ' '); 
                    RunActivity tempRun = new RunActivity(new Time(hours, mins, secs), distance, unit, 
                    new Date(month, day, year));
                    tempRun.addDescription(description);
                    tempRun.setID(activityID);
                    activityList.add(tempRun);
                    break;
                    // add the activity to the arrayList if it is a workout
                    case "WORKOUT":
                    int numSplits     = Integer.parseInt(input.next());
                    float splitDist   = Float.parseFloat(input.next());
                    String splitUnit  = input.next();
                    ArrayList<Time> tempSplits = new ArrayList<>();
                    for(int i = 0; i < numSplits; i++){
                        int minutes   = Integer.parseInt(input.next());
                        float seconds = Float.parseFloat(input.next());
                        tempSplits.add(new Time(minutes, seconds));
                    }
                    description = input.next();
                    description = description.replace('_', ' ');   
                    workoutActivity tempWO = new workoutActivity(numSplits, splitDist, new Date(month, day, year), tempSplits, splitUnit);
                    tempWO.addDescription(description);
                    tempWO.setID(activityID);
                    activityList.add(tempWO);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void displayData(){   // for debugging
        for(int i = 0; i < activityList.size(); i++){
            System.out.println("Date: " + activityList.get(i).toString());
        }
    }
    public static void displayRecents(){
        activityModel = new DefaultListModel<>();
        // bubble sort them into descending order by date newest activities are displayed first
        Activity tempAct;        
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i = 0; i < activityList.size() - 1; i++){
                if(activityList.get(i).compareToGreaterThan(activityList.get(i+1))){  
                    tempAct = activityList.get(i);
                    activityList.set(i, activityList.get(i+1));
                    activityList.set(i+1, tempAct);
                    sorted = false;
                }
            }
        }        
        // loop through the arrayList backwards to get the JList to display correctly
        for(int i = activityList.size()-1; i >= 0; i--){
            activityModel.addElement(activityList.get(i));
        }
        recentActivities = new JList(activityModel);
        activityJSP = new JScrollPane(recentActivities, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        recentActivities.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                int selectedIndex = recentActivities.locationToIndex(me.getPoint());
                int actualIndex = activityList.size() - 1 - selectedIndex;  // covert to correct index in the activityList because the model is backwards
                viewPane.setText(activityList.get(actualIndex).toString());
                selectedActivityIndex[0] = actualIndex;
            }
        });
        activityJSP.setBounds(300, 40, 370, 440);
        mainPanel.add(activityJSP);
    }
    public static void updateRecents(){
       activityModel.add(0, activityList.get(0));    // adds element to the beginning of the model and keeps all the other ones
       recentActivities.setModel(activityModel);
       recentActivities.updateUI();  
    }
    public static void searchDataBase(String requestedMonth, int requestedYear){
       activityList.clear();
        try{
            mainFile = new File(fileName);
            input = new Scanner(mainFile);
        } catch(Exception e){
            e.printStackTrace();
        }
        try{
            while(input.hasNext()){
                int actID = Integer.parseInt(input.next());
                String type = input.next();
                String month = input.next();
                int day = Integer.parseInt(input.next());
                int year = Integer.parseInt(input.next());
                switch(type){
                    case "RUN":
                    float distance     = Float.parseFloat(input.next());
                    String unit        = input.next();
                    String strHours    = input.next();
                    String strMinutes  = input.next();
                    String strSeconds  = input.next();
                    String description = input.next();
                    if(month.equals(requestedMonth) && year == requestedYear){
                        RunActivity tempRun = new RunActivity(new Time(Integer.parseInt(strHours), Integer.parseInt(strMinutes), Float.parseFloat(strSeconds)),
                        distance, unit, new Date(month, day, year));
                        tempRun.addDescription(description);
                        tempRun.setID(actID);
                        activityList.add(tempRun);
                    }
                    break;
                    case "WORKOUT":
                    int numSplits = Integer.parseInt(input.next());
                    String strSplitDist = input.next();
                    String WOunit = input.next();
                    ArrayList<Time> tempSplitTimes = new ArrayList<>(numSplits);
                    for(int i = 0; i < numSplits; i++){
                        int mins = Integer.parseInt(input.next());
                        float secs = Float.parseFloat(input.next());
                        tempSplitTimes.add(new Time(mins, secs));
                    }
                    String WOdesc = input.next();
                    if(month.equals(requestedMonth) && year == requestedYear){
                        workoutActivity tempWO = new workoutActivity(numSplits, Float.parseFloat(strSplitDist), 
                        new Date(month, day, year), tempSplitTimes, WOunit);
                        tempWO.addDescription(WOdesc);
                        tempWO.setID(actID);
                        activityList.add(tempWO);
                    }
                    break;
                }
                activityList.trimToSize();
                updateModel();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    // bubble sort to sort them newest to oldest
    public static void sortDescending(){
        Activity tempAct;
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i = 0; i < activityList.size() - 1; i++){
                if(activityList.get(i).compareToGreaterThan(activityList.get(i+1))){  
                    tempAct = activityList.get(i);
                    activityList.set(i, activityList.get(i+1));
                    activityList.set(i+1, tempAct);
                    sorted = false;
                }
            }
        }     
       updateModel();
    }
    // bubble sort to sort them oldest to newest
    public static void sortAscending(){
        Activity tempAct;
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for(int i = 0; i < activityList.size() - 1; i++){
                if(activityList.get(i).compareToLessThan(activityList.get(i+1))){  
                    tempAct = activityList.get(i);
                    activityList.set(i, activityList.get(i+1));
                    activityList.set(i+1, tempAct);
                    sorted = false;
                }
            }
        }  
       updateModel(); 
    }
    public static void updateMetaData(Boolean action){         // update the meta data to the meta data file
        numberOfEntries = action ? numberOfEntries + 1 : numberOfEntries - 1; 
        // true for increase false for decrease
        Activity.setNumEntries(numberOfEntries);
        try{
            File tempFile = new File(metaDataFile);
            FileWriter tempWriter = new FileWriter(tempFile);
            tempWriter.write("" + numberOfEntries);
            tempWriter.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void rewriteActivityData(){  
        try{
            mainFile = new File(fileName);
            writer = new FileWriter(mainFile);
            for(int i = 0; i < activityList.size(); i++){
               writer.write(activityList.get(i).fileOutputAsString());
            }            
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void modifyActivity(){
        JDialog modDialog = new JDialog(mainFrame);
        JPanel modPanel = new JPanel();
        modPanel.setLayout(null);       
        modDialog.setSize(new Dimension(400, 300));
        JLabel dateLabel;
        JComboBox monthsCBox;
        JTextField dayField;
        JTextField yearField;
        JTextArea descArea;
        JButton saveButt;
        JComboBox unitsCBox = new JComboBox(Activity.units);
        // the layout will be different depending on the type of activity
        System.out.println("Selected Index: " + selectedActivityIndex[0]);
        System.out.println("Activity: " + activityList.get(selectedActivityIndex[0]).toString());
        switch(activityList.get(selectedActivityIndex[0]).getTypeAsString()){
            case "RUN": 
                RunActivity activeRun = (RunActivity)activityList.get(selectedActivityIndex[0]);
                JLabel distLabel = new JLabel("Distance");
                distLabel.setBounds(10, 10, 60, 20);
                JTextField distField = new JTextField();
                distField.setBounds(80, 10, 50, 20);
                distField.setText("" + activeRun.getDistance());
                unitsCBox.setBounds(130, 10, 120, 25);
                for(int i = 0; i < Activity.units.length; i++){
                    if(activeRun.getUnit().equals(Activity.units[i])){
                        unitsCBox.setSelectedIndex(i);
                    }
                }
                JLabel timeLabel = new JLabel("Time");
                timeLabel.setBounds(10, 35, 45, 20);
                JTextField hoursField = new JTextField();
                hoursField.setBounds(55, 35, 30, 20);
                hoursField.setText("" + activeRun.getTotalTime().getHours());
                JTextField minsField = new JTextField();
                minsField.setBounds(90, 35, 30, 20);
                minsField.setText("" + activeRun.getTotalTime().getMinutes());
                JTextField secsField = new JTextField();
                secsField.setBounds(125, 35, 40, 20);
                secsField.setText("" + activeRun.getTotalTime().getSeconds());
                dateLabel = new JLabel("Date");
                dateLabel.setBounds(10, 60, 35, 20);
                monthsCBox = new JComboBox(Date.months);
                monthsCBox.setBounds(55, 60, 120, 25);
                for(int i = 0; i < Date.months.length; i++){
                    if(activeRun.getDate().getMonth().equals(Date.months[i])){
                        monthsCBox.setSelectedIndex(i);
                    }
                }
                dayField = new JTextField();
                dayField.setBounds(185, 60, 30, 20);
                dayField.setText("" + activeRun.getDate().getDay());
                yearField = new JTextField();
                yearField.setBounds(225, 60, 50, 20);
                yearField.setText("" + activeRun.getDate().getYear());
                descArea = new JTextArea();
                descArea.setBounds(50, 100, 150, 80);
                descArea.setText(activeRun.getDescription());
                descArea.setLineWrap(true);
                saveButt = new JButton("Save Changes");
                saveButt.setBounds(80, modDialog.getHeight() - 50, 120, 25);
                saveButt.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        activeRun.setDate(new Date(Date.months[monthsCBox.getSelectedIndex()], Integer.parseInt(dayField.getText()), 
                        Integer.parseInt(yearField.getText())));
                        activeRun.setDistance(Float.parseFloat(distField.getText()));
                        activeRun.setDistanceUnit(Activity.units[unitsCBox.getSelectedIndex()]);
                        activeRun.addDescription(descArea.getText());
                        activeRun.setTime(new Time(Integer.parseInt(hoursField.getText()), Integer.parseInt(minsField.getText()),
                        Float.parseFloat(secsField.getText())));
                        activeRun.calcPace();
                        activityList.set(selectedActivityIndex[0], activeRun);
                        // update the activity display screen
                        activityModel.clear();
                        for(int i = activityList.size()-1; i >=0 ; i--){  // they have to be added backwards to get them to show in the correct order
                            activityModel.addElement(activityList.get(i));
                        }
                        recentActivities.setModel(activityModel);
                        recentActivities.updateUI();  
                        // rewrite the data to the database
                        rewriteActivityData();
                    }
                });
                modPanel.add(distLabel);
                modPanel.add(distField);
                modPanel.add(unitsCBox);
                modPanel.add(timeLabel);
                modPanel.add(hoursField);
                modPanel.add(minsField);
                modPanel.add(secsField);
                modPanel.add(dateLabel);
                modPanel.add(monthsCBox);
                modPanel.add(dayField);
                modPanel.add(yearField);
                modPanel.add(descArea);
                modPanel.add(saveButt);
                modDialog.add(modPanel);
                modDialog.setVisible(true);
                break;
            case "WORKOUT":
            workoutActivity activeWO = (workoutActivity)activityList.get(selectedActivityIndex[0]);
            dateLabel = new JLabel("Date");
            dateLabel.setBounds(10, 60, 35, 20);
            monthsCBox = new JComboBox(Date.months);
            monthsCBox.setBounds(55, 60, 120, 25);
            for(int i = 0; i < Date.months.length; i++){
                if(activeWO.getDate().getMonth().equals(Date.months[i])){
                    monthsCBox.setSelectedIndex(i);
                }
            }
            JLabel splitsLabel = new JLabel("Splits");
            splitsLabel.setBounds(10, 10, 40, 20);
            JTextField splitsField = new JTextField();
            splitsField.setBounds(60, 10, 30, 20);
            splitsField.setText("" + activeWO.getNumSplits());
            JLabel xLabel = new JLabel("x");
            xLabel.setBounds(95, 13, 10, 15);
            JTextField splitDistField = new JTextField();
            splitDistField.setBounds(110, 10, 50, 20);
            splitDistField.setText("" + activeWO.getSplitDistance());
            unitsCBox.setBounds(160, 10, 120, 25);
            for(int i = 0; i < Activity.units.length; i++){
                if(activeWO.getUnit().equals(Activity.units[i])){
                    unitsCBox.setSelectedIndex(i);
                }
            }
            dayField = new JTextField();
            dayField.setBounds(185, 60, 30, 20);
            dayField.setText("" + activeWO.getDate().getDay());
            yearField = new JTextField();
            yearField.setBounds(225, 60, 50, 20);
            yearField.setText("" + activeWO.getDate().getYear());
            descArea = new JTextArea();
            descArea.setBounds(50, 100, 150, 100);
            descArea.setText(activeWO.getDescription());
            descArea.setLineWrap(true);
            JTextArea splitArea = new JTextArea();
            splitArea.setLineWrap(true);
            JScrollPane splitPane = new JScrollPane(splitArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            splitPane.setBounds(220, 100, 85, 100);  // only need to do it for the JSP because it contains the text area
            splitPane.setEnabled(true);
            for(int i = 0; i < activeWO.getNumSplits(); i++){
                Time split = activeWO.getActualSplits().get(i);
                splitArea.append("" + split.getMinutes() + ' ' + split.getSeconds() + '\n');                
            }
            saveButt = new JButton("Save Changes");
            saveButt.setBounds(80, modDialog.getHeight() - 50, 120, 25);
            saveButt.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    activeWO.setDate(new Date(Date.months[monthsCBox.getSelectedIndex()], Integer.parseInt(dayField.getText()),
                    Integer.parseInt(yearField.getText())));  
                    activeWO.setNumSplits(Integer.parseInt(splitsField.getText()));
                    activeWO.setSplitDistance(Float.parseFloat(splitDistField.getText()));                    
                    activeWO.setDistanceUnit(Activity.units[unitsCBox.getSelectedIndex()]);
                    System.out.println("Data: " + splitArea.getText());
                    activeWO.parseSplits(splitArea.getText());
                    activeWO.calcAvg();
                    activityList.set(selectedActivityIndex[0], activeWO);
                    // update the activity display screen
                    activityModel.clear();
                    for(int i = activityList.size()-1; i >=0 ; i--){  // they have to be added backwards to get them to show in the correct order
                        activityModel.addElement(activityList.get(i));
                    }
                    recentActivities.setModel(activityModel);
                    recentActivities.updateUI();  
                    // rewrite the data to the database
                    rewriteActivityData();
                }                
            });
            modPanel.add(splitsField);
            modPanel.add(splitsLabel);
            modPanel.add(xLabel);
            modPanel.add(splitDistField);
            modPanel.add(dateLabel);
            modPanel.add(unitsCBox);
            modPanel.add(monthsCBox);
            modPanel.add(dayField);
            modPanel.add(yearField);
            modPanel.add(descArea);
            modPanel.add(saveButt);
            modPanel.add(splitPane);  // only need to add the JSP because it already contains the text area
            modDialog.add(modPanel);    
            modDialog.setVisible(true);        
            break;
        }        
    }
    public static void updateModel(){
        activityModel.clear();
        for(int i = activityList.size()-1; i >=0 ; i--){  // they have to be added backwards to get them to show in the correct order
            activityModel.addElement(activityList.get(i));
        }
        recentActivities.setModel(activityModel);
        recentActivities.updateUI();  
    }
    public static void initConversions(){
        JLabel distLabel = new JLabel("Distance");
        distLabel.setBounds(10, 200, 70, 20);
        JTextField distField = new JTextField();
        distField.setBounds(80, 200, 65, 20);
        JComboBox unit1Box = new JComboBox(Activity.units);
        unit1Box.setBounds(10, 230, 105, 25);
        JLabel to = new JLabel("To");
        to.setBounds(125, 230, 20, 20);
        JComboBox unit2Box = new JComboBox(Activity.units);
        unit2Box.setBounds(150, 230, 105, 25);
        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(160, 260, 70, 20);
        outputArea.setLineWrap(true);
        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(10, 255, 70, 20);
        convertButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                float origDistance = Float.parseFloat(distField.getText());
                float newDist = 0.0f;
                final float conversionFactor = 1.609f;   // because 1 mile is 1609 meters
                // nested switch statements to account for all the possibilities
                switch(Activity.units[unit1Box.getSelectedIndex()]){
                    case "Miles":
                        switch(Activity.units[unit2Box.getSelectedIndex()]){
                            case "Miles":
                                newDist = origDistance * 1.0f;
                                break;
                            case "Kilometers":
                                newDist = origDistance * conversionFactor;
                                break;
                            case "Meters":
                                newDist = origDistance * conversionFactor * 1000.0f;
                                break;
                            default:
                                System.out.println("Unsupported");
                        }
                        break;
                    case "Kilometers":
                        switch(Activity.units[unit2Box.getSelectedIndex()]){
                            case "Miles":
                                newDist = origDistance / conversionFactor;
                                break;
                            case "Kilometers":
                                newDist = origDistance * 1.0f;
                                break;
                            case "Meters":
                            newDist = origDistance * 1000.0f;
                                break;
                            default:
                                System.out.println("Unsupported");
                        }
                        break;
                    case "Meters":
                        switch(Activity.units[unit2Box.getSelectedIndex()]){
                            case "Miles":
                                newDist = origDistance/1000.0f/conversionFactor;
                                break;
                            case "Kilometers":
                                newDist = origDistance/1000.0f;
                                break;
                            case "Meters":
                                newDist = origDistance * 1.0f;
                                break;
                            default:
                            System.out.println("Unsupported");
                        }
                        break;
                    default:
                        System.out.println("Unsupported error");
                }
                outputArea.setText("" + newDist);
            }
        });
        mainPanel.add(outputArea);
        mainPanel.add(convertButton);
        mainPanel.add(unit2Box);
        mainPanel.add(to);
        mainPanel.add(unit1Box);
        mainPanel.add(distLabel);
        mainPanel.add(distField);
        mainFrame.add(mainPanel);
    }
}