import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Driver {
    int Time = 0;
    static String order;
    static List<PCB> q1 = new ArrayList<>();
    static List<PCB> q2 = new ArrayList<>();
    static List<PCB> allProcesses = new ArrayList<>();
    static List<PCB> Processes = new ArrayList<>();

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
                        Processes.add(p);

                    }

                    Collections.sort(allProcesses);// sort the array based on arival time
                    order = scheduleProcess();
                    break;

                case 2:
                    System.out.println("Scheduling Order:");
                    System.out.println(order);
                    System.out.print("\n");
                    System.out.println("\nProcess Details:");
                    for (PCB process : Processes) {
                        System.out.println(process.toString() + "\n");
                    }
                    System.out.println("\nScheduling Criteria:");
                    System.out.printf("Average Turnaround Time: %.2f\n", calculateAvgTurnaround(Processes));
                    System.out.printf("Average Waiting Time: %.2f\n", calculateAvgWaiting(Processes));
                    System.out.printf("Average Response Time: %.2f\n", calculateAvgResponse(Processes));
                    writeReportToFile(Processes);
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

    static boolean addProcesses(int time) {
        boolean processAdded = false;
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(0).getArrivalTime() == time) {
                processAdded = true;
                if (allProcesses.get(0).getPriority() == 1)
                    q1.add(allProcesses.remove(0));

                else
                    q2.add(allProcesses.remove(0));

            }

            else
                break;

        }
        return processAdded;

    }

    public static String scheduleProcess() {
        String schedule = "";

        int time = 0;

        // outer loop for multilevel queue
        while (!allProcesses.isEmpty() || !q1.isEmpty() || !q2.isEmpty()) {
            boolean added = addProcesses(time);

            // if no processes are added and both queues are empty increment time
            if (!added && q1.isEmpty() && q2.isEmpty()) {
                time++; // time increments when no processes can be scheduled
                continue; // skip the rest of the loop
            }
            // Round robin
            while (!q1.isEmpty()) {

                PCB process = q1.remove(0);
                if (process.isNew()) {
                    process.setStartTime(time);
                    process.setResponseTime(time - process.getArrivalTime());

                }

                process.setNew(false);

                schedule = schedule + "| " + process.getProcessId();

                int burstTime = process.getBurstTime();
                int i;

                // terminate when i = TQ or the process is terminated(burst time = 0)
                for (i = 0; i < 3 && i < burstTime; i++) {
                    time++;
                    process.setBurstTime(process.getBurstTime() - 1);
                    addProcesses(time);
                }
                // add non completed processes to the queue again
                if (i == 3 && (process.getBurstTime() != 0)) {
                    q1.add(process);
                }

                else {
                    process.setTerminationTime(time);
                    process.setTurnaroundTime(process.getTerminationTime() - process.getArrivalTime());
                    process.setWaitingTime(process.getTurnaroundTime() - process.getCpuBurst());
                }
                /*
                 * we can add and else here to caluclate turn around time( the process is
                 * complete )
                 * 
                 * else {
                 * 
                 * write in here the turn around time and wating time calculations
                 * }
                 * 
                 */

            }

            // add q2 scheduling algorithm here
            while (!q2.isEmpty() && q1.isEmpty()) {

                // Bubble sort on q2 based on burst time of previously sorted arival time
                // processes ^ .
                // sort is stable in case 2 equal burst time appears (first arrived scheudled
                // first)
                // && case were 2 equal burst time and equal arival time (first intered by user
                // is choosen)
                for (int i = 0; i < q2.size() - 1; i++) {
                    for (int j = 0; j < q2.size() - i - 1; j++) {
                        if (q2.get(j).getBurstTime() > q2.get(j + 1).getBurstTime()) {
                            PCB temp = q2.get(j);
                            q2.set(j, q2.get(j + 1));
                            q2.set(j + 1, temp);
                        }
                    }
                }

                // in case process was preemted by MLQ due to q1 higher priorty we save actual
                // RT by marking a flag for new processes and reallocated ones
                PCB process = q2.remove(0);
                if (process.isNew()) {
                    process.setStartTime(time);
                    process.setResponseTime(time - process.getArrivalTime());

                }

                process.setNew(false);

                schedule = schedule + "| " + process.getProcessId();

                // same in here we save actual BT to reset it later
                int burstTime = process.getBurstTime();

                // Execute the process for its burst time

                for (int i = 0; i < burstTime; i++) {

                    // check if q1 is empty due to its higher priorty
                    if (!q1.isEmpty()) {
                        if (process.getBurstTime() != 0)
                            q2.add(process);
                        break;
                    }
                    time++;

                    process.setBurstTime(process.getBurstTime() - 1);

                    addProcesses(time);

                }

                // Update the termination time for the process

                process.setTerminationTime(time);

                // Calculate the waiting time for the process
                process.setBurstTime(burstTime);

                process.setWaitingTime(
                        process.getTerminationTime() - process.getArrivalTime() - process.getCpuBurst());

                // Calculate the turnaround time for the process

                process.setTurnaroundTime(process.getTerminationTime() - process.getArrivalTime());

            }
            // time++;

        }

        return schedule;

    }

    private static void writeReportToFile(List<PCB> processes) {
        try (FileWriter writer = new FileWriter("Report.txt")) {
            writer.write("Scheduling Order:\n");
            writer.write(order);
            writer.write("\n\nProcess Details:\n");
            for (PCB process : processes) {
                writer.write(process.toString() + "\n");
            }
            writer.write("\nScheduling Criteria:\n");
            writer.write("Average Turnaround Time: " + calculateAvgTurnaround(Processes) + "\n");
            writer.write("Average Waiting Time: " + calculateAvgWaiting(Processes) + "\n");
            writer.write("Average Response Time: " + calculateAvgResponse(Processes) + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static double calculateAvgTurnaround(List<PCB> processes) {
        double avg = 0;
        double sum = 0;
        for (PCB process : processes) {
            sum += process.getTurnaroundTime();
        }
        avg = sum / processes.size();
        return avg;
    }

    public static double calculateAvgWaiting(List<PCB> processes) {
        double avg = 0;
        double sum = 0;
        for (PCB process : processes) {
            sum += process.getWaitingTime();
        }
        avg = sum / processes.size();
        return avg;
    }

    public static double calculateAvgResponse(List<PCB> processes) {
        double avg = 0;
        double sum = 0;
        for (PCB process : processes) {
            sum += process.getResponseTime();
        }
        avg = sum / processes.size();
        return avg;
    }

}
