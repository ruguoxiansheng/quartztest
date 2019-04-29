package example1;

/**
 * @ Author     ：Jet.wu [jet.wu@kpmg.com]
 * @ Date       ：Created in 10:57 上午 23/04/2019
 * @ Description：${description}
 * @since jdk1.8
 */


import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This Example will demonstrate how to start and shutdown the Quartz scheduler and how to schedule a job to run in
 * Quartz.
 *
 * @author Bill Kratzer
 */
public class SimpleExample2 {

  public void run() throws Exception {
    Logger log = Logger.getLogger(SimpleExample2.class.toString());
    log.setLevel(Level.INFO);

   log.info("------- Initializing ----------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

   log.info("------- Initialization Complete -----------");

    // computer a time that is on the next round minute
//    Date runTime = evenMinuteDate(new Date());

    Date runTime = DateBuilder.evenSecondDateAfterNow();
    Date endTime = DateBuilder.evenMinuteDate(new Date());

   log.info("------- Scheduling Job  -------------------");

    // define the job and tie it to our HelloJob class
    JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();

    JobDetail job1 = newJob(HelloJob.class).withIdentity("job1", "group1").build();

    // Trigger the job to run on the next round minute
    Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().startAt(runTime).withSchedule(
        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(100)).build();

    // Tell quartz to schedule the job using our trigger
    sched.scheduleJob(job, trigger);
   log.info(job.getKey() + " will run at: " + runTime);

    // Start up the scheduler (nothing can actually run until the
    // scheduler has been started)
    sched.start();

   log.info("------- Started Scheduler -----------------");

    // wait long enough so that the scheduler as an opportunity to
    // run the job!
   log.info("------- Waiting 65 seconds... -------------");
//    try {
//      // wait 65 seconds to show job
//      Thread.sleep(65L * 1000L);
//      // executing...
//    } catch (Exception e) {
//      //
//    }

    // shut down the scheduler
   log.info("------- Shutting Down ---------------------");
//    sched.shutdown(true);
   log.info("------- Shutdown Complete -----------------");
  }

  public static void main(String[] args) throws Exception {

    SimpleExample2 example = new SimpleExample2();
    example.run();

  }


}
