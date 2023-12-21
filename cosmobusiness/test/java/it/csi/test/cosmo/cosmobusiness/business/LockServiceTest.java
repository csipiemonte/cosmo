/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import org.hibernate.StaleStateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.dto.LockPolicyConfiguration;
import it.csi.cosmo.cosmobusiness.dto.rest.AcquisizioneLockRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Lock;
import it.csi.cosmo.cosmobusiness.dto.rest.RilascioLockRequest;
import it.csi.test.cosmo.cosmobusiness.business.LockServiceTest.LockServiceTestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, LockServiceTestConfig.class})
@Transactional
public class LockServiceTest extends ParentIntegrationTest {

  @Configuration
  public static class LockServiceTestConfig {

    @Bean
    @Primary
    public LockPolicyConfiguration lockPolicyConfiguration() {
      return new LockPolicyConfiguration() {

        @Override
        public boolean synchronizeLocalThreads() {
          return false;
        }

        @Override
        public boolean synchronizeDatabaseRowOnRelease() {
          return false;
        }

      };
    }
  }

  @Autowired
  private LockService service;

  @Test
  public void testControlledSequence() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();

    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    IntFunction<Callable<AcquisizioneLockRequest>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        String ownerCode = UUID.randomUUID().toString();
        log(Thread.currentThread().getName() + " - local id " + ownerCode);
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");
        long sleepTime = 100L * num;
        log(Thread.currentThread().getName() + " - sleeping for " + sleepTime + " ms");
        sleep(sleepTime);
        log(Thread.currentThread().getName() + " - finished sleeping");

        AcquisizioneLockRequest request = new AcquisizioneLockRequest();
        request.setCodiceRisorsa(resourceCode);
        request.setCodiceOwner(ownerCode);
        request.setDurata(10 * 1000L);
        Lock lock = service.postLock(request);
        log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

        log(Thread.currentThread().getName() + " - finished task");
        return request;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // il primo thread deve aver fallito
    expect(() -> {
      try {
        t1.get();
      } catch (ExecutionException e) {
        throw ExceptionUtils.toChecked(e.getCause());
      } catch (Exception e) {
        throw ExceptionUtils.toChecked(e);
      }
    }, ConflictException.class);

    // il secondo thread deve aver acquisito il lock
    AcquisizioneLockRequest result = (AcquisizioneLockRequest) t2.get();
    assertNotNull(result);
    assertNotNull(result.getCodiceOwner());
    assertNotNull(result.getCodiceRisorsa());
    assertNotNull(result.getDurata());
    assertEquals(resourceCode, result.getCodiceRisorsa());
  }

  @Test
  public void testContemporaryAcquisitionWithNoLockFailsWithDataIntegrityViolation()
      throws Exception {
    service.reconfigure(new LockPolicyConfiguration() {

      @Override
      public boolean synchronizeLocalThreads() {
        return false;
      }

      @Override
      public boolean synchronizeDatabaseRowOnRelease() {
        return false;
      }

      @Override
      public long delayBeforeAcquisitionFlush() {
        return 200L;
      }
    });

    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();
    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    IntFunction<Callable<AcquisizioneLockRequest>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        String ownerCode = UUID.randomUUID().toString();
        log(Thread.currentThread().getName() + " - local id " + ownerCode);
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");

        AcquisizioneLockRequest request = new AcquisizioneLockRequest();
        request.setCodiceRisorsa(resourceCode);
        request.setCodiceOwner(ownerCode);
        request.setDurata(10 * 1000L);
        Lock lock = service.postLock(request);
        log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

        log(Thread.currentThread().getName() + " - finished task");
        return request;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // un thread deve aver fallito, un altro deve aver acquisito il lock
    List<AcquisizioneLockRequest> results = new ArrayList<>();
    List<Exception> exceptions = new ArrayList<>();

    try {
      results.add((AcquisizioneLockRequest) t1.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    try {
      results.add((AcquisizioneLockRequest) t2.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    assertEquals(2, results.size());
    assertEquals(0, exceptions.size());

    AcquisizioneLockRequest result = results.get(0);
    assertNotNull(result);
    assertNotNull(result.getCodiceOwner());
    assertNotNull(result.getCodiceRisorsa());
    assertNotNull(result.getDurata());
    assertEquals(resourceCode, result.getCodiceRisorsa());
  }

  @Test
  public void testContemporaryAcquisitionWithLocalLockFailsWithoutDataIntegrityViolation()
      throws Exception {
    service.reconfigure(new LockPolicyConfiguration() {

      @Override
      public boolean synchronizeLocalThreads() {
        return true;
      }

      @Override
      public boolean synchronizeDatabaseRowOnRelease() {
        return false;
      }

      @Override
      public long delayBeforeAcquisitionFlush() {
        return 200L;
      }
    });

    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();
    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    IntFunction<Callable<AcquisizioneLockRequest>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        String ownerCode = UUID.randomUUID().toString();
        log(Thread.currentThread().getName() + " - local id " + ownerCode);
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");

        AcquisizioneLockRequest request = new AcquisizioneLockRequest();
        request.setCodiceRisorsa(resourceCode);
        request.setCodiceOwner(ownerCode);
        request.setDurata(10 * 1000L);
        Lock lock = service.postLock(request);
        log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

        log(Thread.currentThread().getName() + " - finished task");
        return request;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // un thread deve aver fallito, un altro deve aver acquisito il lock
    List<AcquisizioneLockRequest> results = new ArrayList<>();
    List<Exception> exceptions = new ArrayList<>();

    try {
      results.add((AcquisizioneLockRequest) t1.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    try {
      results.add((AcquisizioneLockRequest) t2.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    assertEquals(1, results.size());
    assertEquals(1, exceptions.size());

    assertTrue(exceptions.get(0).getCause() instanceof ConflictException);
    assertFalse(exceptions.get(0).getCause().getCause() instanceof DataIntegrityViolationException);

    AcquisizioneLockRequest result = results.get(0);
    assertNotNull(result);
    assertNotNull(result.getCodiceOwner());
    assertNotNull(result.getCodiceRisorsa());
    assertNotNull(result.getDurata());
    assertEquals(resourceCode, result.getCodiceRisorsa());
  }

  @Test
  public void testContemporaryReleaseWithoutLockFailsOnFlush() throws Exception {
    service.reconfigure(new LockPolicyConfiguration() {

      @Override
      public boolean synchronizeLocalThreads() {
        return false;
      }

      @Override
      public boolean synchronizeDatabaseRowOnRelease() {
        return false;
      }

      @Override
      public long delayBeforeRelease() {
        return 200L;
      }
    });

    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();
    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    String ownerCode = UUID.randomUUID().toString();

    AcquisizioneLockRequest request = new AcquisizioneLockRequest();
    request.setCodiceRisorsa(resourceCode);
    request.setCodiceOwner(ownerCode);
    request.setDurata(10 * 1000L);
    final Lock lock = service.postLock(request);
    log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

    IntFunction<Callable<Boolean>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");

        RilascioLockRequest releaseRequest = new RilascioLockRequest();
        releaseRequest.setCodiceOwner(ownerCode);
        releaseRequest.setCodiceRisorsa(resourceCode);
        boolean result = service.deleteLock(releaseRequest);

        log(Thread.currentThread().getName() + " - lock release result: " + lock);

        log(Thread.currentThread().getName() + " - finished task");
        return result;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // un thread deve aver fallito, un altro deve aver rilasciato il lock
    List<Boolean> results = new ArrayList<>();
    List<Exception> exceptions = new ArrayList<>();

    try {
      results.add((Boolean) t1.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    try {
      results.add((Boolean) t2.get());
    } catch (Exception e) {
      exceptions.add(e);
    }

    assertEquals(1, results.size());
    assertEquals(1, exceptions.size());

    assertTrue(exceptions.get(0).getCause() instanceof ObjectOptimisticLockingFailureException);
    assertTrue(exceptions.get(0).getCause().getCause() instanceof StaleStateException);

    Boolean result = results.get(0);
    assertNotNull(result);
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testContemporaryReleaseWithLocalLockDoesNotFailButReturnsFalse() throws Exception {
    service.reconfigure(new LockPolicyConfiguration() {

      @Override
      public boolean synchronizeLocalThreads() {
        return true;
      }

      @Override
      public boolean synchronizeDatabaseRowOnRelease() {
        return false;
      }

      @Override
      public long delayBeforeRelease() {
        return 200L;
      }
    });

    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();
    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    String ownerCode = UUID.randomUUID().toString();

    AcquisizioneLockRequest request = new AcquisizioneLockRequest();
    request.setCodiceRisorsa(resourceCode);
    request.setCodiceOwner(ownerCode);
    request.setDurata(10 * 1000L);
    final Lock lock = service.postLock(request);
    log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

    IntFunction<Callable<Boolean>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");

        RilascioLockRequest releaseRequest = new RilascioLockRequest();
        releaseRequest.setCodiceOwner(ownerCode);
        releaseRequest.setCodiceRisorsa(resourceCode);
        boolean result = service.deleteLock(releaseRequest);

        log(Thread.currentThread().getName() + " - lock release result: " + result);

        log(Thread.currentThread().getName() + " - finished task");
        return result;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // un thread deve aver fallito, un altro deve aver rilasciato il lock
    List<Boolean> results = new ArrayList<>();

    results.add((Boolean) t1.get());
    results.add((Boolean) t2.get());

    assertFalse(results.get(0).equals(results.get(1)));
  }

  @Test
  public void testContemporaryReleaseWithDatabaseLockDoesNotFailButReturnsFalse() throws Exception {
    service.reconfigure(new LockPolicyConfiguration() {

      @Override
      public boolean synchronizeLocalThreads() {
        return false;
      }

      @Override
      public boolean synchronizeDatabaseRowOnRelease() {
        return true;
      }

      @Override
      public long delayBeforeRelease() {
        return 200L;
      }
    });

    ExecutorService executor = Executors.newFixedThreadPool(2);

    String resourceCode = "testResource-" + UUID.randomUUID().toString();
    final CyclicBarrier startGun = new CyclicBarrier(3, () -> log("cyclic barrier tripped"));

    String ownerCode = UUID.randomUUID().toString();

    AcquisizioneLockRequest request = new AcquisizioneLockRequest();
    request.setCodiceRisorsa(resourceCode);
    request.setCodiceOwner(ownerCode);
    request.setDurata(10 * 1000L);
    final Lock lock = service.postLock(request);
    log(Thread.currentThread().getName() + " - lock acquisition result: " + lock);

    IntFunction<Callable<Boolean>> taskSupplier = (int num) -> {
      return () -> {
        log(Thread.currentThread().getName() + " - starting task");
        log(Thread.currentThread().getName() + " - waiting for start signal");
        startGun.await();
        log(Thread.currentThread().getName() + " - received start signal");

        RilascioLockRequest releaseRequest = new RilascioLockRequest();
        releaseRequest.setCodiceOwner(ownerCode);
        releaseRequest.setCodiceRisorsa(resourceCode);
        boolean result = service.deleteLock(releaseRequest);

        log(Thread.currentThread().getName() + " - lock release result: " + result);

        log(Thread.currentThread().getName() + " - finished task");
        return result;
      };
    };

    Future<?> t1 = executor.submit(taskSupplier.apply(3));
    Future<?> t2 = executor.submit(taskSupplier.apply(1));

    log(Thread.currentThread().getName() + " - sleeping");
    sleep(100);
    log(Thread.currentThread().getName() + " - starting all");
    startGun.await();
    log(Thread.currentThread().getName() + " - started all");

    // attendo che i task terminino
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    // un thread deve aver fallito, un altro deve aver rilasciato il lock
    List<Boolean> results = new ArrayList<>();

    results.add((Boolean) t1.get());
    results.add((Boolean) t2.get());

    assertFalse(results.get(0).equals(results.get(1)));
  }

}
