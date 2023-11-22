import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class TimeTableSchedulerProjct {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<ClassSchedule> classes = new ArrayList<>();

        System.out.println("Welcome to the Timetable Scheduler!");
        printConstraints();

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Class");
            System.out.println("2. Delete Class");
            System.out.println("3. View Timetable");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addClass(scanner, classes);
                    Map<String, List<ClassSchedule>> timetable = scheduleClasses(classes);
                    break;
                case 2:
                    deleteClass(scanner, classes);
                    break;
                case 3:
                    viewTimetable(classes);
                    break;
                case 4:
                    System.out.println("Exiting the Timetable Scheduler. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void printConstraints() {
        System.out.println("\nConstraints and Preferences:");
        System.out.println("1. One subject can have only 1 class in a day.");
        System.out.println("2. Tutorials can have 2 slots: 1st and 6th if required.");
        System.out.println("3. Lab takes up a minimum of 3 slots (3 hours, same as the institute).");
        System.out.println("4. Lectures should be scheduled in the 7th to 12th slots.");
        System.out.println("Slot 1 :- 6:00-6:50");
        System.out.println("Slot 2 :- 7:00-7:50");
        System.out.println("Slot 3 :- 8:00-8:50");
        System.out.println("Slot 4 :- 9:00-9:50");
        System.out.println("Slot 5 :- 10:00-10:50");
        System.out.println("Slot 6 :- 11:00-11:50");
        System.out.println("Slot 7 :- 12:00-12:50");
        System.out.println("Slot 8 :- 13:00-13:50");
        System.out.println("Slot 9 :- 14:00-14:50");
        System.out.println("Slot 10 :- 15:00-15:50");
        System.out.println("Slot 11 :- 16:00-16:50");
        System.out.println("Slot 12 :- 17:00-17:50");

    }

    private static void addClass(Scanner scanner, List<ClassSchedule> classes) {
        System.out.print("Enter class name: ");
        String className = scanner.nextLine();

        System.out.print("Enter class type (lecture/tutorial/lab): ");
        String classType = scanner.nextLine();

        System.out.print("Enter day (Monday-Friday): ");
        String day = scanner.nextLine();

        // Check if the subject already has a class scheduled on the same day
        if (hasClassOnDay(classes, className, day)) {
            System.out.println("Error: One subject can have only 1 class in a day. Please choose a different day.");
            return;
        }

        int startTime;
        int endTime;

        if (classType.equalsIgnoreCase("tutorial")) {
            System.out.println("Available slots for tutorials: 1st, 6th");
            System.out.print("Enter slot (1 or 6): ");
            int slot = Integer.parseInt(scanner.nextLine());
            startTime = slot;
            endTime = slot + 1;
        } else if (classType.equalsIgnoreCase("lab")) {
            // Lab should have a minimum duration of 3 hours
            do {
                System.out.print("Enter start time (1-12): ");
                startTime = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end time (1-12): ");
                endTime = Integer.parseInt(scanner.nextLine());

                if (endTime - startTime < 3) {
                    System.out.println("Error: Lab should have a minimum duration of 3 hours.");
                }

            } while (endTime - startTime < 3);
        } else {
            // Lectures should be scheduled in the 7th to 12th slots
            do {
                System.out.print("Enter start time (7-12): ");
                startTime = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter end time (7-12): ");
                endTime = Integer.parseInt(scanner.nextLine());

                if (startTime < 7 || endTime > 12) {
                    System.out.println("Error: Lectures should be scheduled in the 7th to 12th slots.");
                }

            } while (startTime < 7 || endTime > 12);
        }

 

        classes.add(new ClassSchedule(className, classType, day, startTime, endTime));
        System.out.println("Class added successfully!");
    }
    public static Map<String, List<ClassSchedule>> scheduleClasses(List<ClassSchedule> classes) {
        // Sort classes based on day and start time
        classes.sort(Comparator.comparing(ClassSchedule::getDay).thenComparing(ClassSchedule::getStartTime));

        // Use a HashMap to store the schedule, where the key is the day
        Map<String, List<ClassSchedule>> timetable = new HashMap<>();

        for (ClassSchedule classSchedule : classes) {
            if (!hasOverlap(timetable.getOrDefault(classSchedule.getDay(), new ArrayList<>()), classSchedule)) {
                timetable.computeIfAbsent(classSchedule.getDay(), k -> new ArrayList<>()).add(classSchedule);
            }
        }

        return timetable;
    }
     public static boolean hasOverlap(List<ClassSchedule> classes, ClassSchedule newClass) {
        for (ClassSchedule existingClass : classes) {
            if (existingClass.getEndTime() > newClass.getStartTime() && existingClass.getStartTime() < newClass.getEndTime()) {
                System.out.println("Error: Class overlap detected - " + existingClass + " and " + newClass);
                return true;
            }
            else{
                System.out.println("Class added successfully!");
            }
        }
        return false;
    }


    private static boolean hasClassOnDay(List<ClassSchedule> classes, String className, String day) {
        for (ClassSchedule classSchedule : classes) {
            if (classSchedule.className.equals(className) && classSchedule.day.equals(day)) {
                return true;
            }
        }
        return false;
    }

    private static void deleteClass(Scanner scanner, List<ClassSchedule> classes) {
        viewTimetable(classes);

        System.out.print("Enter the index of the class to delete: ");
        int index = Integer.parseInt(scanner.nextLine());

        if (index >= 0 && index < classes.size()) {
            ClassSchedule deletedClass = classes.remove(index);
            System.out.println("Class deleted: " + deletedClass);
        } else {
            System.out.println("Invalid index. No class deleted.");
        }
    }

    private static void viewTimetable(List<ClassSchedule> classes) {
        if (classes.isEmpty()) {
            System.out.println("Timetable is empty. Add classes to view the timetable.");
        } else {
            System.out.println("\nTimetable:");
            System.out.printf("%-15s%-15s%-15s%-15s%-15s\n", "Day", "Class Type", "Class Name", "Start Time", "End Time");

            for (ClassSchedule classSchedule : classes) {
                System.out.printf("%-15s%-15s%-15s%-15d%-15d\n", classSchedule.getDay(), classSchedule.getClassType(),
                        classSchedule.className, classSchedule.formatStartTime(), classSchedule.formatEndTime());
            }
        }
    }

    private static int parseTimeInput(String timeInput) {
        try {
            Date date = timeFormat.parse(timeInput);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            return hours * 60 + minutes;
        } catch (ParseException e) {
            System.out.println("Error parsing time input. Using default value.");
            return 0;
        }
    }
}
class ClassSchedule {
    String className;
    String classType;
    String day;
    int startTime;
    int endTime;

    public ClassSchedule(String className, String classType, String day, int startTime, int endTime) {
        this.className = className;
        this.classType = classType;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getClassType() {
        return classType;
    }

    public int formatStartTime() {
        return startTime;
    }

    public int formatEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return className + " (" + classType + ", " + day + "): " + formatStartTime() + "-" + formatEndTime();
    }

    private String formatTime(int time) {
        int hours = time / 60;
        int minutes = time % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
