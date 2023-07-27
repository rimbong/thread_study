
/* 
 * 1. Thread 클래스
 *  Thread 클래스를 구현하거나 Runnable 인터페이스를 구현하기
 * 
 * 2. 순서 및 쓰레드의 완료 제어
 *  start()를 통해 해당 쓰레드를 실행 시킬 수 있고 join() 함수를 통해 끝날때까지 대기를 걸어둘 수 있다.
 * 
 * 3. 공유 데이터(객체)
 *  Runnable 인터페이스로 구현된 객체의 생성자에 공유 데이터를 주입하여 사용함
 * 
 * 4. 쓰레드 풀
 *  1. 자동으로 스레드 수 생성
 *      ExecutorService executorServiceWithCached = Executors.newCachedThreadPool();
 *  2. 원하는 개수만큼 생성
 *      ExecutorService executorServiceWithNum = Executors.newFixedThreadPool(2);
 *  3. 최대치로 생성
 *      ExecutorService executorServiceWithMax = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
 *  4. 완전 수동
 *      ThreadPoolExecutor( 코어 스레드 수, 최대 스레드 개수, 놀고 있는 시간, 놀고있는 시간 단위, 작업 큐 )
 *      ExecutorService executorServiceWithCustom = new ThreadPoolExecutor(3, 100, 120L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
 * 
 * 5. 쓰레드풀 종류
 * 
 *  1. 작업 큐에 대기하고 있는 모든 작업이 끝난 뒤 스레드를 종료한다. 
 *      executorServiceWithCached.shutdown();*  
 *  2. 당장 중지한다. 리턴값은 작업큐에 남아있는 작업의 목록이다.
 *      List<Runnable> runable = executorServiceWithCached.shutdownNow();
 *  3. 작업은 대기 하지만 모든 작업처리를 특정 시간안에 하지 못하면 작업중인 스레드를 중지하고 false를 리턴한다. 아래는 100초 설정
 *      try {
 *          boolean isFinish = executorServiceWithCached.awaitTermination(100, TimeUnit.SECONDS);
 *      } catch (InterruptedException e) {
 *          // TODO Auto-generated catch block
 *          e.printStackTrace();
 *      }
 * 
 * 
 */
public class ThreadTest {
    public static void main(String[] args) {
        // ThreadRun t = new ThreadRun();
        // t.start();
//        Runnable r = new RunnableRun(100);
//        Runnable r2 = new RunnableRun(200);
//        new Thread(r).start();
//        new Thread(r2).start();
//        
//        for (int i = 0; i < 100; i++) {
//            System.out.println(999-i);
//        }
    	
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for (int index = 0; index < 10; index++) {
                    sum += index;
                    System.out.println(sum);
                }
                System.out.println( Thread.currentThread() + "최종 합 : " + sum);
            }
        };
    	Thread sub1 =new Thread(task);
    	Thread sub2 =new Thread(task);
        sub1.start();
        sub2.start();
        
    }
}
