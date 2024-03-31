import java.util.*;

public class Driver {
    int Time = 0;
    static List<PCB> q1 = new ArrayList<>();
    static List<PCB> q2 = new ArrayList<>();
    static List<PCB> allProcesses = new ArrayList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {

            System.out.println("1- Enter process information");
            System.out.println("2 -Report detailed information about each process and different scheduling criteria");
            System.out.println("3 -Exit program");
            System.out.print("Enter your choice 1 or 2 or 3: ");

            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the number of processes: ");
                    int num = input.nextInt();

                    for (int i = 1; i <= num; i++) {
                        System.out.println("process " + i + ":");
                        System.out.print("priority 1 or 2: ");
                        int priority = input.nextInt();
                        System.out.print("arrival Time: ");
                        int arrivalTime = input.nextInt();
                        System.out.print("CPU burst: ");
                        int cpuBurst = input.nextInt();
                        PCB p = new PCB("P" + i, priority, arrivalTime, cpuBurst);
                        allProcesses.add(p);
                    }

                    Collections.sort(allProcesses);// sort the array based on arival time
                    System.out.println(scheduleProcess()); // temporary
                    break;

                case 2:
                    // reportInfo();
                    break;
                case 3:
                    exit = true;
                    System.out.println("exited from program");
                    break;
                default:
                    System.out.println("invalid choice");
            }

        }

    }

    static void addProcesses(int time) {

        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(0).getArrivalTime() == time) {
                if (allProcesses.get(0).getPriority() == 1)
                    q1.add(allProcesses.remove(0));

                else
                    q2.add(allProcesses.remove(0));

            }

            else
                break;

        }

    }

    public static String scheduleProcess() {
        String schedule = "";

        int time = 0;
        while (!allProcesses.isEmpty() || !q1.isEmpty() || !q2.isEmpty()) {
            addProcesses(time);
            while (!q1.isEmpty()) {

                PCB process = q1.remove(0);
                if (process.isNew())
                    process.setResponseTime(time - process.getArrivalTime());

                schedule = schedule + "| " + process.getProcessId();

                int burstTime = process.getBurstTime();
                int i;

                for (i = 0; i < 3 && i < burstTime; i++) {
                    time++;
                    process.setBurstTime(process.getBurstTime() - 1);
                    addProcesses(time);
                }

                if (i == 3 && (process.getBurstTime() != 0)) {
                    q1.add(process);
                }

            }

            time++;

        }

        return schedule;

    }

}
