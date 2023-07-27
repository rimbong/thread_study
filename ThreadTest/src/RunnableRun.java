public class RunnableRun implements Runnable {

    private int temp;

    public RunnableRun(int t){
        temp = t;    
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(temp+i);
        }
    }
}
