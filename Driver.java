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

        // outer loop for multilevel queue
        while (!allProcesses.isEmpty() || !q1.isEmpty() || !q2.isEmpty()) {
            addProcesses(time);

            // Round robin
            while (!q1.isEmpty()) {

                PCB process = q1.remove(0);
                if (process.isNew()) {
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
                process.setTerminationTime(time);
                process.setTurnaroundTime(process.getTerminationTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
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

    // Bubble sort on q2 based on burst time of previously sorted arival time processes ^ .
    // sort is stable in case 2 equal burst time appears (first arrived scheudled first)
    // && case were 2 equal burst time and equal arival time (first intered by user is choosen)
    for (int i = 0; i < q2.size() - 1; i++) {
        for (int j = 0; j < q2.size() - i - 1; j++) {
            if (q2.get(j).getBurstTime() > q2.get(j + 1).getBurstTime()) {
                PCB temp = q2.get(j);
                q2.set(j, q2.get(j + 1));
                q2.set(j + 1, temp);
            }
        }
    }

// in case process was preemted by MLQ due to q1 higher priorty we save actual RT by marking a flag for new processes and reallocated ones
    PCB process = q2.remove(0);
      if (process.isNew()) {
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
               if (process.getBurstTime()!=0)
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

    process.setWaitingTime(process.getTerminationTime() - process.getArrivalTime() - process.getBurstTime());



    // Calculate the turnaround time for the process

    process.setTurnaroundTime(process.getTerminationTime() - process.getArrivalTime());

}
            //time++;

        }

        return schedule;

    }

}
