import java.util.List;

public class Driver{
int Time = 0;
static List<PCB> q1 = new ArrayList<>();
static List<PCB> q2= new ArrayList<>();

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
                    PCB p = new PCB(i, priority, arrivalTime, cpuBurst);
                    if(addProcess(p))
                    System.out.println("process added");}
                     //print statement just to check
                    break;
              
                case 2:
                    reportInfo();
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




public static boolean addProcess(PCB proc){

if (proc.getPriority() ==1)
q1.add(proc);
else
q2.add(proc);
}

   
}

    
