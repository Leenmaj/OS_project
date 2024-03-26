public class PCB implements Comparable<PCB> {
    private int processId;
    private int priority;
    private int arrivalTime;
    private int burstTime;
    private int startTime;
    private int terminationTime;
    private int turnaroundTime;
    private int waitingTime;
    private int responseTime;

    public PCB(int processId, int priority, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

        this.startTime = -1;
        this.terminationTime = -1;
        this.turnaroundTime = -1;
        this.waitingTime = -1;
        this.responseTime = -1;
    }

    // getters and setters
    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getTerminationTime() {
        return terminationTime;
    }

    public void setTerminationTime(int terminationTime) {
        this.terminationTime = terminationTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public int compareTo(PCB o) {
        return Integer.compare(this.arrivalTime, o.arrivalTime);
    }

    @Override
    public String toString() {
        return "PCB{" +
                "processId=" + processId +
                ", priority=" + priority +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", startTime=" + startTime +
                ", terminationTime=" + terminationTime +
                ", turnaroundTime=" + turnaroundTime +
                ", waitingTime=" + waitingTime +
                ", responseTime=" + responseTime +
    
                '}';
    }

}