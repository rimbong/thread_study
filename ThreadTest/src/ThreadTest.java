
/* 
 * 1. Thread 클래스
 *  Thread 클래스를 구현하거나 Runnable 인터페이스를 구현하기 또는 return이 가능한 Callable<T> 객체 (T는 반환형)
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
 *  5. 단일 쓰레드
 *      ExecutorService executorServiceSingle = Executors.newSingleThreadExecutor():
 *      이 메서드는 단일 스레드로 작업을 실행합니다. 
 *      하나의 작업이 완료되기 전까지 다른 작업이 실행되지 않습니다. 주로 작업 순서를 보장해야 할 때 사용됩니다.
 * 
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
 * 
 * 언제 스레드 안전한가? 
 *  스레드 안전성 기준
 *  공유 자원 없음: 메서드가 로컬 변수만 사용하면 안전.
 *      로컬 변수 → 안전.
 *  동기화된 공유 자원: 공유 자원(인스턴스 변수 등)이 동기화되어 있으면 안전.
 *      Vector 동기화됨 → 안전.
 *  원자성 보장: 여러 작업이 하나의 트랜잭션처럼 실행돼야 할 때 동기화 필요.
        단일 add만 수행 → 추가 동기화 불필요.
 *  외부 상호작용: 외부 리소스(파일, DB 등)에 접근하면 해당 리소스의 스레드 안전성 확인 필요.
 * 
 * 
 * 7. 쓰레드 실행 
 * 
 *  - execute(Runnable task):*
 *      execute() 메서드는 Runnable 객체를 인자로 받아 즉시 실행합니다.
 *      반환값이 없습니다. 작업의 완료 상태나 결과를 확인할 방법이 없습니다.
 *      예외가 발생하면 실행 스레드에서 예외가 발생하고 호출자에게 전파되지 않습니다.
 * 
 *  - submit(Callable<T> task) 및 submit(Runnable task):
 *      submit() 메서드는 Runnable 또는 Callable 객체를 인자로 받아 실행합니다.
 *      Future 객체를 반환합니다. 이 Future 객체를 통해 작업의 완료 상태를 확인하거나 결과를 얻을 수 있습니다.
 *      예외가 발생하면 Future.get() 메서드를 호출할 때 예외가 발생합니다.
 *      
 *      submit() 메서드는 작업을 실행 큐에 넣고, 그 작업의 결과를 나타내는 Future 객체를 반환합니다. 
 *      이 Future 객체를 통해 작업의 상태를 확인하거나 결과를 얻을 수 있습니다. future.get()을 호출하면 다음과 같은 동작이 발생합니다:
 *       작업이 실행 중인 경우: future.get()은 해당 작업이 완료될 때까지 블록(대기)합니다. 작업이 완료되면 결과를 반환합니다.
 *       작업이 이미 완료된 경우: future.get()은 즉시 작업의 결과를 반환합니다.
 *       작업이 예외를 발생시킨 경우: future.get()은 ExecutionException을 발생시킵니다.
 * 
 * 
 * 
 * 
 * 
 * 
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
		// }
        
        // 작업 큐에 대기하고 있는 모든 작업이 끝난 뒤 스레드를 종료한다.
        // 더 이상 새로운 작업을 받지 않음.
        // 이미 큐에 있는 작업들은 끝날 때까지 실행함.
        executorServiceWithCached.shutdown();


        // executorService.shutdown();
        // 
        //     try {
            // 현재 실행 중인 작업들( 큐에 남은 작업들 )이 60초 내에 끝나기를 기다림.
        //         if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            // 끝나지 않으면... (false 리턴)
        //             executorService.shutdownNow();
        //         }
        //     } catch (InterruptedException e) {
        //         executorService.shutdownNow();
        //     }
    }
}
/* 
CompletableFuture 을 이용해 아래와 같이 짬

public class XFDeliveryDelayListWorker {

	private static LogWriter _logger = LogManager.getWriter("batch");

	public static void main(String[] args) throws Exception {
        boolean isRunMode = false;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
		try {
            // 현재 상태를 체크 한다.
			if(XFSchWorkManager.isLock("XFDeliveryDelayListWorker")) {
				throw new IOException("XFDeliveryDelayListWorker 가 이미 실행중입니다.");
			} else {
				// LOCK 설정
				XFSchWorkManager.setLock("XFDeliveryDelayListWorker");
				_logger.info("XFDeliveryDelayListWorker 작업을 시작 합니다.");
				isRunMode = true;
			}
            XFDeliveryDelayListManager manager = XFDeliveryDelayListManager.getInstance();

            CompletableFuture<List<Map<String, Object>>> failFuture = runSearchAsync(manager::searchListAboutFail, executorService, "fail list");
            CompletableFuture<List<Map<String, Object>>> resendFuture = runSearchAsync(manager::searchListAboutResend, executorService, "resend list");
            CompletableFuture<List<Map<String, Object>>> notSendFuture = runSearchAsync(manager::searchListAboutNotSend, executorService, "not send list");
            
            // CompletableFuture.allOf(failFuture, resendFuture, notSendFuture).join();
            // List<Map<String, Object>> searchListAboutFail = failFuture.join();
            // List<Map<String, Object>> searchListAboutResend = resendFuture.join();
            // List<Map<String, Object>> searchListAboutNotSend = notSendFuture.join();
           
            Map<String, List<Map<String, Object>>> results = CompletableFuture.allOf(failFuture, resendFuture, notSendFuture)
            .thenApply(v -> {
                Map<String, List<Map<String, Object>>> map = new HashMap<>();
                map.put("fail", failFuture.join());
                map.put("resend", resendFuture.join());
                map.put("notSend", notSendFuture.join());
                return map;
            })
            .join();

            List<Map<String, Object>> searchListAboutFail = results.get("fail");
            List<Map<String, Object>> searchListAboutResend = results.get("resend");
            List<Map<String, Object>> searchListAboutNotSend = results.get("notSend");
    
            if ( searchListAboutFail.isEmpty() && searchListAboutResend.isEmpty() && searchListAboutNotSend.isEmpty() ) {
                _logger.info("XFDeliveryDelayListWorker 작업을 종료합니다.( 보낼 리스트 존재하지 않음 )");
                return;
            }

            ISmartCaller smartCaller = new SmartCaller();

            CompletableFuture<Void> failAlertFuture = sendAlertsAsync(searchListAboutFail, "3일이 지난 발송 실패 문서들이 존재합니다. 확인 바랍니다.", smartCaller, executorService);
            CompletableFuture<Void> resendAlertFuture = sendAlertsAsync(searchListAboutResend, "3일이 지난 재발송 요청 문서들이 존재합니다. 확인 바랍니다.", smartCaller, executorService);
            CompletableFuture<Void> notSendAlertFuture = sendAlertsAsync(searchListAboutNotSend, "3일이 지난 미수신 문서들이 존재합니다. 확인 바랍니다.", smartCaller, executorService);

            // 모든 알림 전송 완료 대기
            CompletableFuture.allOf(failAlertFuture, resendAlertFuture, notSendAlertFuture).join();

            smartCaller.AlertStart();
		} catch (CompletionException e) {			
			_logger.error("비동기작업 에러 발생");
        } catch(Exception e) {
			_logger.error(e.getMessage());
		} finally {
            if (isRunMode) {
                XFSchWorkManager.setUnLock("XFDeliveryDelayListWorker");
            }
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
            _logger.info("XFDeliveryDelayListWorker 작업을 종료합니다.");
        }
		return;
	}

    private static CompletableFuture<List<Map<String, Object>>> runSearchAsync(Callable<List<Map<String, Object>>> searchMethod, ExecutorService executorService, String methodName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return searchMethod.call();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch " + methodName + ": SQL Error - " + e.getMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch " + methodName + ": Unexpected error - " + e.getMessage(), e);
            }
        }, executorService).exceptionally(throwable -> {
            _logger.error(throwable.getMessage());
            return new ArrayList<>();
        });
    }

    private static CompletableFuture<Void> sendAlertsAsync(List<Map<String, Object>> list, String message, ISmartCaller smartCaller,  ExecutorService executorService) {
        List<CompletableFuture<Void>> futures = list.stream().map(map -> CompletableFuture.runAsync(() -> {
            String userId = (String) map.get("user_id");
            String title = (String) map.get("title");
            try {
                if (userId != null) {
                    smartCaller.movePageAlert(userId, "[" + truncateSize(title) + "] " + message, ISmartCaller.XFALERT_OPEN_PAGE_FUNC, "sancsendcomp");
                } else {
                    _logger.info("XFDeliveryDelayListWorker 작업중 userId 값이 존재하지 않는 부분이 존재함");
                    _logger.info("===========info==============");
                    _logger.info((String)map.get("docid") + "/" + (String)map.get("user_kid") + "/" + (String)map.get("user_name"));
                    _logger.info("===========end===============");
                }
            } catch (Exception e) {
                _logger.error("XFDeliveryDelayListWorker 작업중 에러가 발생함");
                _logger.error(e.getMessage());
            }
        }, executorService)).collect(Collectors.toList());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
    
    public static String truncateSize( String s ) throws NullPointerException, Exception {
		int limitSize = 10;
		
		if(s == null) {
			return "";
		}
		
		if(s.length() < limitSize) {
			return s;
		} else {
			return s.substring(0, limitSize ) + "...";
		}
	}
}
 */

/* 
2번쨰 예시

질문하신 코드와 설명을 바탕으로 `findPrices` 메서드의 동작을 단계별로 쉽게 풀어서 설명드리겠습니다. 이 코드는 Java의 `CompletableFuture`를 사용하여 여러 상점에서 제품 가격을 비동기적으로 조회하고, 할인된 최종 가격을 계산하는 예제입니다. 설명이 이해하기 어려웠다고 하셨으니, 핵심 개념을 명확히 하고 코드의 각 부분이 무엇을 하는지, 왜 그렇게 설계되었는지 자세히 설명하겠습니다.

---

### 코드의 목적
`findPrices` 메서드는 주어진 제품(`product`)의 가격을 여러 상점(`shops`)에서 조회한 뒤, 각 가격에 할인율을 적용하여 최종 가격 리스트를 반환합니다. 이 과정은 **비동기적으로** 실행되며, `CompletableFuture`를 활용해 효율적으로 처리합니다. 최종 결과는 `List<String>` 형태로, 각 상점의 할인된 가격 정보를 문자열로 반환합니다.

---

### 주요 개념
코드를 이해하려면 몇 가지 핵심 개념을 알아야 합니다:

1. **비동기 프로그래밍**:
   - 비동기 작업은 메인 스레드가 작업 완료를 기다리지 않고 다른 작업을 수행할 수 있도록 합니다.
   - `CompletableFuture`는 비동기 작업을 관리하고 결과를 처리하는 강력한 도구입니다.

2. **`CompletableFuture`**:
   - 비동기 작업의 결과를 나타내는 객체로, 작업이 완료되면 결과를 제공하거나 추가 작업을 연결할 수 있습니다.
   - 주요 메서드:
     - `supplyAsync`: 비동기 작업을 시작하고 결과를 반환.
     - `thenApply`: 이전 작업의 결과를 받아 동기적으로 변환.
     - `thenCompose`: 이전 작업의 결과를 받아 새로운 비동기 작업을 연결.
     - `join`: 작업이 완료될 때까지 기다리고 결과를 반환.

3. **스트림과 비동기 작업의 조합**:
   - 이 코드에서는 `shops` 리스트를 스트림으로 처리하며, 각 상점에 대해 비동기 작업(`CompletableFuture`)을 생성합니다.
   - 결과는 `List<CompletableFuture<String>>` 형태로, 각 `CompletableFuture`는 상점별 최종 가격을 계산하는 작업을 나타냅니다.

---

### 코드 분석 (단계별 설명)

코드를 하나씩 뜯어보며 각 줄이 무엇을 하는지, 설명과 연결하여 설명하겠습니다.

#### 1. `shops.stream()`
- **설명**: `shops`는 상점 객체(`Shop`)의 리스트로 가정됩니다. `stream()`을 호출해 이를 스트림으로 변환하여 각 상점을 순회하며 처리합니다.
- **역할**: 각 상점에 대해 가격 조회 및 할인 적용 작업을 수행할 준비를 합니다.

#### 2. 첫 번째 `map`: 가격 정보 얻기
```java
.map(shop -> CompletableFuture.supplyAsync(
    () -> shop.getPrice(product), executor))
```
- **코드 동작**:
  - 각 상점(`shop`)에 대해 `shop.getPrice(product)`를 호출하여 제품의 가격 정보를 얻습니다.
  - 이 작업은 **비동기적으로** 실행되며, `CompletableFuture.supplyAsync`를 사용해 별도 스레드에서 수행됩니다.
  - `executor`는 작업을 실행할 스레드 풀(예: `ExecutorService`)로, 커스텀 스레드 풀을 사용해 성능을 최적화합니다.
  - `shop.getPrice(product)`는 문자열(예: `"ShopName:100.00"`)을 반환한다고 가정합니다.
- **결과**: `Stream<CompletableFuture<String>>`
  - 각 `CompletableFuture<String>`은 상점에서 반환한 가격 문자열을 비동기적으로 제공합니다.
- **설명과의 연결**:
  - 설명에서 "팩토리 메서드 `supplyAsync`에 람다 표현식을 전달해서 비동기적으로 상점에서 정보를 조회했다"고 한 부분입니다.
  - "커스텀 Executor"는 `executor`를 의미하며, 작업을 병렬로 효율적으로 실행하기 위해 사용됩니다.
- **왜 비동기인가?**:
  - `shop.getPrice(product)`는 원격 서비스 호출이나 I/O 작업(예: 1초 지연)을 포함할 수 있으므로, 동기적으로 호출하면 시간이 오래 걸립니다.
  - 비동기 호출로 각 상점의 가격 조회를 병렬로 처리해 전체 실행 시간을 줄입니다.

#### 3. 두 번째 `map`: `Quote` 파싱
```java
.map(future -> future.thenApply(Quote::parse))
```
- **코드 동작**:
  - 첫 번째 단계에서 생성된 `CompletableFuture<String>`(가격 문자열)을 받아 `Quote.parse` 메서드로 변환합니다.
  - `Quote.parse`는 문자열(예: `"ShopName:100.00"`)을 `Quote` 객체로 파싱한다고 가정합니다. `Quote`는 상점 이름과 가격을 포함하는 데이터 클래스일 가능성이 높습니다.
  - `thenApply`는 **동기적 변환**을 수행합니다. 즉, `CompletableFuture`의 결과가 준비되면 해당 스레드에서 `Quote::parse`를 호출합니다.
  - 이 작업은 I/O가 없으므로 빠르게 실행됩니다.
- **결과**: `Stream<CompletableFuture<Quote>>`
  - 각 `CompletableFuture<Quote>`는 상점의 가격 정보를 `Quote` 객체로 변환한 결과를 제공합니다.
- **설명과의 연결**:
  - 설명에서 "첫 번째 결과 문자열을 `Quote`로 변환한다. 파싱 동작에서는 원격 서비스나 I/O가 없으므로 지연 없이 수행할 수 있다"고 한 부분입니다.
  - "`thenApply`는 `CompletableFuture`가 끝날 때까지 블록하지 않는다"는 말은, `thenApply`가 비동기적으로 결과가 준비된 후에 호출된다는 의미입니다.
  - 결과적으로 `CompletableFuture<String>`이 `CompletableFuture<Quote>`로 변환됩니다.
- **왜 `thenApply`인가?**:
  - `Quote.parse`는 단순한 데이터 변환 작업이므로 별도 스레드에서 실행할 필요가 없습니다.
  - `thenApply`는 이전 작업의 결과를 받아 즉시 처리하므로 적합합니다.

#### 4. 세 번째 `map`: 할인된 가격 계산
```java
.map(future -> future.thenCompose(quote ->
    CompletableFuture.supplyAsync(
        () -> Discount.applyDiscount(quote), executor)))
```
- **코드 동작**:
  - 두 번째 단계에서 생성된 `CompletableFuture<Quote>`를 받아, `Quote` 객체를 `Discount.applyDiscount` 메서드에 전달해 할인된 가격을 계산합니다.
  - `Discount.applyDiscount(quote)`는 원격 서비스 호출을 포함한다고 가정하며, 문자열(예: `"ShopName:90.00"`)을 반환합니다.
  - 이 작업은 **비동기적으로** 실행되며, `supplyAsync`를 사용해 `executor` 스레드 풀에서 수행됩니다.
  - `thenCompose`는 두 비동기 작업을 연결합니다:
    - 첫 번째 작업: `Quote` 객체를 얻는 작업(`CompletableFuture<Quote>`).
    - 두 번째 작업: `Quote`를 사용해 할인된 가격을 계산하는 작업(`CompletableFuture<String>`).
  - `thenCompose`는 첫 번째 작업의 결과(`Quote`)를 두 번째 작업의 입력으로 사용하며, 최종적으로 `CompletableFuture<String>`를 반환합니다.
- **결과**: `Stream<CompletableFuture<String>>`
  - 각 `CompletableFuture<String>`는 상점의 할인된 가격 문자열을 제공합니다.
- **설명과의 연결**:
  - 설명에서 "상점에서 받은 할인전 가격에 원격 Discount 서비스에서 제공하는 할인율을 적용해야 한다. 이번에는 원격 실행이 포함되므로 비동기적으로 작업을 수행해야 한다"고 한 부분입니다.
  - "`thenCompose`는 첫 번째 연산의 결과를 두 번째 연산으로 전달한다"는 말은, `Quote`를 받아 `Discount.applyDiscount`를 비동기적으로 호출하는 과정을 의미합니다.
  - 두 비동기 작업(가격 조회 → 할인 적용)을 파이프라인으로 연결해 효율적으로 처리합니다.
- **왜 `thenCompose`인가?**:
  - `Discount.applyDiscount`는 원격 호출이므로 시간이 걸립니다. 따라서 새로운 비동기 작업으로 처리해야 합니다.
  - `thenCompose`는 첫 번째 `CompletableFuture`의 결과(`Quote`)를 두 번째 비동기 작업의 입력으로 자연스럽게 연결합니다.
  - 만약 `thenApply`를 사용했다면, `Discount.applyDiscount`가 동기적으로 실행되어 비효율적일 수 있습니다.

#### 5. `collect(toList())`: `CompletableFuture` 리스트 수집
```java
.collect(toList());
```
- **코드 동작**:
  - 세 번째 `map` 연산까지의 스트림(`Stream<CompletableFuture<String>>`)을 리스트로 수집합니다.
- **결과**: `List<CompletableFuture<String>>`
  - `priceFutures`는 각 상점의 할인된 가격을 계산하는 `CompletableFuture` 객체들의 리스트입니다.
- **설명과의 연결**:
  - 설명에서 "세 개의 map 연산 결과 스트림의 요소를 리스트로 수집하면 `List<CompletableFuture<String>>` 형식의 자료를 얻을 수 있다"고 한 부분입니다.
- **왜 리스트로 수집?**:
  - 각 상점의 작업은 독립적인 `CompletableFuture`로 처리되며, 이들을 모아 최종 결과를 얻기 위해 리스트로 저장합니다.

#### 6. 최종 결과 처리
```java
return priceFutures.stream()
    .map(CompletableFuture::join)
    .collect(toList());
```
- **코드 동작**:
  - `priceFutures` 리스트를 다시 스트림으로 변환하고, 각 `CompletableFuture`에 대해 `join()`을 호출해 작업이 완료될 때까지 기다립니다.
  - `join()`은 `CompletableFuture`의 결과를 반환하며, 완료되지 않았다면 블록킹(대기)합니다.
  - 최종적으로 각 `CompletableFuture<String>`의 결과(할인된 가격 문자열)를 리스트로 수집해 반환합니다.
- **결과**: `List<String>`
  - 예: `["BestPrice price is 110.93", "LetsSaveBig price is 135.58", ...]`
- **설명과의 연결**:
  - 설명에서 "마지막으로 `CompletableFuture`가 완료되기를 기다렸다가 `join`으로 값을 추출할 수 있다"고 한 부분입니다.
  - `join()`은 모든 비동기 작업이 끝난 후 결과를 모아 최종 리스트를 만듭니다.
- **왜 `join()`인가?**:
  - `join()`은 `Future.get()`과 유사하지만, 예외를 `CompletionException`으로 래핑하지 않고 그대로 던집니다.
  - 여기서는 모든 작업이 완료된 결과를 수집하므로 적합합니다.

---

### 설명에서 이해가 어려웠던 부분 풀어보기
설명에서 몇 가지 개념이 복잡하거나 모호하게 느껴졌을 수 있습니다. 아래에서 주요 포인트를 간단히 정리하고, 질문에서 "이해가 잘되지 않는다"고 하신 점을 고려해 쉽게 풀어보겠습니다.

1. **"비동기적으로 상점에서 정보를 조회했다"**:
   - `shop.getPrice(product)`는 시간이 오래 걸리는 작업(예: 원격 서버 호출)이라고 가정합니다.
   - `supplyAsync`를 사용하면 이 작업을 메인 스레드가 아닌 `executor` 스레드 풀에서 실행해, 메인 스레드는 다른 일을 할 수 있습니다.
   - 예: 상점 5곳에서 가격을 조회한다고 하면, 동기적으로 호출하면 5초(1초 × 5)가 걸리지만, 비동기적으로 병렬 실행하면 약 1초 만에 완료될 수 있습니다.

2. **"`thenApply`는 블록하지 않는다"**:
   - `thenApply(Quote::parse)`는 `shop.getPrice`가 완료된 후에 호출됩니다. 이 작업은 단순한 문자열 파싱이므로 빠르게 끝납니다.
   - "블록하지 않는다"는 말은, `thenApply`가 별도 스레드를 차단하지 않고 이전 작업의 결과를 받아 즉시 처리한다는 뜻입니다.
   - 비유: 우체부가 편지를 배달(`shop.getPrice`)한 후, 당신이 편지를 열어 내용을 확인(`Quote.parse`)하는 과정은 빠르게 이루어집니다.

3. **"`thenCompose`로 두 비동기 작업을 파이프라인으로 연결"**:
   - `thenCompose`는 두 번째 비동기 작업(`Discount.applyDiscount`)을 첫 번째 작업(`Quote` 얻기)의 결과에 의존하도록 연결합니다.
   - 예: 상점에서 가격(`Quote`)을 받은 후, 이를 할인 서비스에 보내 최종 가격을 계산하는 과정은 두 단계로 나뉩니다. 둘 다 비동기적이어야 효율적입니다.
   - `thenCompose`는 이 두 단계를 자연스럽게 이어줍니다. 만약 `thenApply`를 사용하면 할인 계산이 동기적으로 실행되어 시간이 더 걸릴 수 있습니다.
   - 비유: 친구가 물건 가격을 알아보고(`Quote`), 그 정보를 할인점에 보내 할인된 가격을 확인하는 과정(`Discount.applyDiscount`)을 생각해보세요. 두 단계 모두 시간이 걸리니 병렬로 처리하는 게 효율적입니다.

4. **"`thenCompose` vs `thenComposeAsync`"**:
   - 설명에서 `thenCompose`와 `thenComposeAsync`의 차이를 언급했는데, 이 부분이 혼란스러울 수 있습니다.
   - `thenCompose`: 이전 작업과 같은 스레드에서 두 번째 작업을 실행합니다. 스레드 전환 오버헤드가 적습니다.
   - `thenComposeAsync`: 두 번째 작업을 새로운 스레드(여기서는 `executor`)에서 실행합니다. 스레드 전환이 필요해 약간의 오버헤드가 있습니다.
   - 이 코드에서는 `thenCompose` 안에 `supplyAsync(..., executor)`를 사용했으므로, 두 번째 작업(`Discount.applyDiscount`)은 이미 `executor` 스레드 풀에서 비동기적으로 실행됩니다. 따라서 `thenComposeAsync`를 사용할 필요가 없습니다.
   - 결과적으로 `thenCompose`를 사용해도 성능상 큰 차이가 없으며, 오버헤드를 줄이는 선택을 한 것입니다.

5. **"메인 스레드는 UI 이벤트에 반응하는 등 유용한 작업을 수행할 수 있다"**:
   - 비동기 작업 덕분에 메인 스레드는 가격 조회나 할인 계산을 기다리지 않고 다른 일을 할 수 있습니다.
   - 예: 웹 애플리케이션이라면, 사용자가 버튼을 누르는 등의 UI 이벤트를 처리할 수 있습니다.

---

### 코드의 전체 흐름 (비유로 쉽게 이해)
이 코드를 비유로 설명하면, 여러 상점에서 물건 가격을 알아보는 상황을 생각해보세요:
1. **가격 조회** (`supplyAsync`): 당신은 5개 상점에 전화(`shop.getPrice`)를 걸어 가격을 물어보라고 직원(`executor`)들에게 지시합니다. 각 직원은 동시에 전화를 걸어 결과를 기다립니다.
2. **Quote 파싱** (`thenApply`): 직원이 전화로 받은 가격 정보(문자열)를 정리해 깔끔한 견적서(`Quote`)로 작성합니다. 이건 간단한 작업이라 바로 끝납니다.
3. **할인 적용** (`thenCompose` + `supplyAsync`): 견적서를 받은 직원이 할인점(`Discount.applyDiscount`)에 연락해 할인된 가격을 알아옵니다. 이 과정도 시간이 걸리므로 다른 직원이 비동기적으로 처리합니다.
4. **결과 수집** (`join`): 모든 직원이 할인된 가격을 알아오면, 당신은 그 결과를 한데 모아 리스트로 정리합니다.

이 과정에서 당신(메인 스레드)은 전화 걸기나 할인 계산을 직접 하지 않고, 직원들이 알아서 처리하도록 맡긴 뒤 최종 결과만 받습니다. 이렇게 하면 시간이 훨씬 절약됩니다!

---

### 코드 실행 예시 (가정)
가정:
- `shops`: `[BestPrice, LetsSaveBig, MyFavoriteShop, BuyItAll, ShopEasy]`
- `product`: `"iPhone"`
- `shop.getPrice`: 상점 이름과 가격을 문자열로 반환 (예: `"BestPrice:123.25"`)
- `Quote.parse`: 문자열을 `Quote` 객체로 변환 (예: `Quote(shop="BestPrice", price=123.25)`)
- `Discount.applyDiscount`: `Quote`를 받아 할인된 가격 문자열 반환 (예: `"BestPrice:110.93"`)

실행 결과:
```java
[
    "BestPrice price is 110.93",
    "LetsSaveBig price is 135.58",
    "MyFavoriteShop price is 192.72",
    "BuyItAll price is 184.74",
    "ShopEasy price is 167.28"
]
```

시간: 약 2초 (각 작업이 1초 지연이라면, 병렬 실행으로 약 2초만 걸림).

---

### 개선 제안
코드는 잘 작성되었지만, 몇 가지 개선할 여지가 있습니다:
1. **예외 처리 추가**:
   - `shop.getPrice`, `Quote.parse`, `Discount.applyDiscount`에서 예외가 발생할 수 있습니다.
   - `CompletableFuture`의 `exceptionally`나 `handle` 메서드로 예외를 처리하면 더 안정적입니다.
   ```java
   .map(future -> future.thenApply(Quote::parse)
       .exceptionally(t -> new Quote("Error", 0.0)))
   ```

2. **Executor 관리**:
   - `executor`가 외부에서 주입된다고 가정하지만, 코드 내에서 `executor`를 닫지 않습니다.
   - 호출자가 `executor`를 관리해야 하지만, 명시적으로 문서화하거나 메서드 내에서 처리하는 게 좋습니다.

3. **스트림 병렬화**:
   - 현재는 `shops.stream()`을 사용하지만, `shops.parallelStream()`을 사용하면 스트림 자체도 병렬로 처리될 수 있습니다. 다만, `executor`를 이미 사용하므로 큰 차이는 없을 수 있습니다.

---

### 결론
`findPrices` 메서드는 `CompletableFuture`를 활용해 여러 상점의 가격을 비동기적으로 조회하고, `Quote`로 파싱한 뒤, 할인된 가격을 계산하는 효율적인 코드입니다. 설명이 복잡하게 느껴질 수 있었던 이유는 `CompletableFuture`의 메서드(`supplyAsync`, `thenApply`, `thenCompose`)와 비동기 작업의 파이프라인이 익숙하지 않았기 때문일 가능성이 큽니다.

- **핵심 요약**:
  - 첫 번째 `map`: 상점에서 가격을 비동기적으로 조회 (`CompletableFuture<String>`).
  - 두 번째 `map`: 가격 문자열을 `Quote`로 동기적으로 변환 (`thenApply`).
  - 세 번째 `map`: `Quote`를 사용해 할인된 가격을 비동기적으로 계산 (`thenCompose` + `supplyAsync`).
  - 최종: 모든 작업이 완료된 결과를 리스트로 수집 (`join`).
- **비동기의 이점**: 모든 상점의 작업이 병렬로 실행되어 전체 실행 시간이 단축됩니다.
- **설명의 어려운 부분**: `thenCompose`와 `thenComposeAsync`의 차이, 비동기 작업의 파이프라인 개념이 복잡했을 수 있습니다.

이제 코드와 설명이 좀 더 명확해졌기를 바랍니다! 추가로 궁금한 점이나 특정 부분을 더 깊이 파고 싶으시면 말씀해주세요. 예를 들어, `Quote`나 `Discount` 클래스의 구현을 가정해 더 구체적인 예제를 드릴 수도 있습니다!

 */