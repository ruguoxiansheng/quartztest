package example2;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.logging.Logger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This Example will demonstrate all of the basics of scheduling capabilities of Quartz using Simple Triggers.
 *
 * @author Bill Kratzer
 */
public class SimpleTriggerExample2 {

  public void run() throws Exception {
    Logger log = Logger.getLogger(SimpleTriggerExample2.class.toString());

    log.info("------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    // jobs can be scheduled before sched.start() has been called

    // get a "nice round" time a few seconds in the future...
    Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

    // job1 will only fire once at date/time "ts"
    JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();

    SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();

    // schedule it to run!
    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
        + trigger.getRepeatInterval() / 1000 + " seconds");

    log.info("------- Starting Scheduler ----------------");

    // All of the jobs have been added to the scheduler, but none of the jobs
    // will run until the scheduler has been started
    sched.start();

    log.info("------- Waiting 30 seconds... --------------");

    try {
      // wait 33 seconds to show jobs
      Thread.sleep(30L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    // jobs can be re-scheduled...
    // job 7 will run immediately and repeat 10 times for every second
    log.info("------- Rescheduling... --------------------");
    trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

    ft = sched.rescheduleJob(trigger.getKey(), trigger);
    log.info("job7 rescheduled to run at: " + ft);

    log.info("------- Waiting five minutes... ------------");
    try {
      // wait five minutes to show jobs
      Thread.sleep(300L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    log.info("------- Shutting Down ---------------------");

    sched.shutdown(true);

    log.info("------- Shutdown Complete -----------------");

    // display some stats about the schedule that just ran
    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

  }

  public static void main(String[] args) throws Exception {

    SimpleTriggerExample2 example = new SimpleTriggerExample2();
    example.run();

  }

}
