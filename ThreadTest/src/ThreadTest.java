
/* 
 * 1. Thread 클래스
 *  Thread 클래스를 구현하거나 Runnable 인터페이스를 구현하기 또는 return이 가능한 Callable 객체
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
 * 6. 쓰레드 safe
 *  static 키워드는 multi-thread 환경에서 문제가 생길 수 있다.
 *   - multi-thread 환경에서도 공유됨
 *    동기화 문제는 보통 heap 영역에서 발생 + 
 *  synchronization(동기화): 프로세스 또는 스레드들이 수행되는 시점을 조절해 서로 알고 있는 정보를 일치시키는 것
 *   - 스레드 동기화 문제를 해결해 thread-safe를 지원하는 키워드
 *     여러 스레드가 하나의 공유 자원에 접근할 때, 현재 데이터를 사용하는 스레드를 제외한 스레드들은 데이터에 접근하지 못하게 함
 *     무분별한 동기화는 프로그램의 성능 저하를 일으킴
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTest {
    public static void main(String[] args) {
        // ThreadRun t = new ThreadRun();
        // t.start();
        // Runnable r = new RunnableRun(100);
        // Runnable r2 = new RunnableRun(200);
        // new Thread(r).start();
        // new Thread(r2).start();

        // for (int i = 0; i < 100; i++) {
        // System.out.println(999 - i);
        // }

        // Runnable task = new Runnable() {
        // @Override
        // public void run() {
        // int sum = 0;
        // for (int index = 0; index < 10; index++) {
        // sum += index;
        // System.out.println(sum);
        // }
        // System.out.println(Thread.currentThread() + "최종 합 : " + sum);
        // }
        // };
        // Thread sub1 = new Thread(task);
        // Thread sub2 = new Thread(task);
        // try {
        // sub1.start();
        // sub1.join();
        // sub2.start();
        // sub2.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        // 스레드 풀 생성
        // 자동으로 스레드 수 생성
        ExecutorService executorServiceWithCached = Executors.newCachedThreadPool();

        // Runable 구현 객체 ( 익명구현객체 사용 )
        Runnable task1 = () -> {
            for (int index = 0; index < 100; index++) {
                System.out.println("working");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Callable 구현
        Callable<Boolean> task2 = () -> {
            Boolean isFinish = true;

            for (int index = 0; index < 100; index++) {
                System.out.println("working Call");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return isFinish;
        };

        // 1. 리턴 값이 없는 단순 Runnable를 처리합니다.
        executorServiceWithCached.execute(task1);

        // 2. 리턴 가능한 Callable도 넣을 수 있는 메서드 입니다.
        Future<Boolean> returnBoolean = executorServiceWithCached.submit(task2);

        // main스레드의 작업이 멈추지 않기 위해 새로운 스레드로 구성
        executorServiceWithCached.execute(() -> {
            try {
                if (returnBoolean.get()) {
                    System.out.println("working end");
                } else {
                    System.out.println("working fail");
                }
            } catch (Exception e) {
            }
        });

        // main스레드의 작업이 멈추지 않기 위해 새로운 스레드로 구성
        // executorServiceWithCached.execute(() -> {
        //     try {
        //         // 만약 특정 시간 내에 끝났는지 확인하려는 경우
        //         if (returnBoolean.get(1, TimeUnit.SECONDS)) {
        //             System.out.println("working end");
        //         }
        //     } catch (Exception e) {
        //         System.out.println("working fail");
        //     }
        // });

        // 처리할 task가 여러개일 경우 아래와 같이 한번에 처리 및 실행 (submit) 후에
        // get()을 통해 순차적으로 결과를 받으면 된다.
        // ArrayList<Future<HashMap<String,String>>> futureList = new ArrayList<>();
		// ArrayList<Callable<HashMap<String,String>>> taskList = new ArrayList<>();
        // for (int index = 0; index < taskList.size(); index++) {
        //     Callable<HashMap<String,String>> task = taskList.get(index);
        //     Future<HashMap<String,String>> returnMap = executorService.submit(task);
        //     futureList.add( returnMap );
        // }
        
        // for (int index = 0; index < futureList.size(); index++) {
        //     Future<HashMap<String,String>> returnMap = futureList.get(index);
        //     HashMap<String, String> weatherMap = returnMap.get();
        //     if ( weatherMap != null ) {
        //         resultMap.put( weatherMap.get("key"), weatherMap);
        //     } else {
        //         throw new Exception("api를 받아오지 못했습니다.");
        //     }
        // }

        //  또 Exception시 실행중인 쓰레드를 전부 죽여야 할때 아래와 같이 shutdownNow()를 호출해줘야한다.
        // catch ( Exception e ) {
		// 	if( executorService !=null ){
		// 		executorService.shutdownNow();
		// 	}
		// 	NNException nne = new NNException( e );
		// 	out.println( nne.getXml() );
		// }
        
        // 작업 큐에 대기하고 있는 모든 작업이 끝난 뒤 스레드를 종료한다.
        executorServiceWithCached.shutdown();

    }
}
