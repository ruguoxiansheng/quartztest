/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package chainexample;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.logging.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.listeners.JobChainingJobListener;

/**
 * Demonstrates the behavior of <code>JobListener</code>s. In particular, this example will use a job listener to
 * trigger another job after one job succesfully executes.
 */
public class ListenerExample2 {

  public void run() throws Exception {
    Logger log = Logger.getLogger(ListenerExample2.class.toString());

    log.info("------- Initializing ----------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");

    log.info("------- Scheduling Jobs -------------------");

    // schedule a job to run immediately

    JobDetail job1 = newJob(SimpleJob1.class).withIdentity("job1").build();
    JobDetail job2 = newJob(SimpleJob2.class).withIdentity("job2").build();
    JobDetail job3 = newJob(SimpleJob3.class).withIdentity("job3").build();
    Trigger trigger1 = newTrigger().withIdentity("trigger1").startNow().build();

    // Set up the listener
    JobListener listener = new JobChainingJobListener("chainTest");
    ((JobChainingJobListener) listener).addJobChainLink(job1.getKey(),job2.getKey());
    ((JobChainingJobListener) listener).addJobChainLink(job2.getKey(),job3.getKey());
    sched.getListenerManager().addJobListener(listener);
    // schedule the job to run
    sched.scheduleJob(job1, trigger1);
    sched.addJob(job2,false,true);
    sched.addJob(job3,false,true);

    // All of the jobs have been added to the scheduler, but none of the jobs
    // will run until the scheduler has been started
    log.info("------- Starting Scheduler ----------------");
    sched.start();

    // wait 30 seconds:
    // note: nothing will run
    log.info("------- Waiting 30 seconds... --------------");
    try {
      // wait 30 seconds to show jobs
      Thread.sleep(1000L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    // shut down the scheduler
    log.info("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

  }

  public static void main(String[] args) throws Exception {

    ListenerExample2 example = new ListenerExample2();
    example.run();
  }

}
