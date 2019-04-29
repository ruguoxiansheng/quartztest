package example4;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.logging.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This Example will demonstrate how job parameters can be passed into jobs and how state can be maintained
 *
 * @author Bill Kratzer
 */
public class JobStateExample1 {

  public void run() throws Exception {
    Logger log = Logger.getLogger(JobStateExample1.class.toString());

    log.info("------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    // get a "nice round" time a few seconds in the future....
    Date startTime = nextGivenSecondDate(null, 10);

    // job1 will only run 5 times (at start time, plus 4 repeats), every 10 seconds
    JobDetail job1 = newJob(ColorJob.class).withIdentity("job1", "group1").build();

    SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
        .withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

    // pass initialization parameters into the job
    job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Green");
    job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

    // schedule the job to run
    Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
    log.info(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount()
        + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");


    log.info("------- Starting Scheduler ----------------");

    // All of the jobs have been added to the scheduler, but none of the jobs
    // will run until the scheduler has been started
    sched.start();

    log.info("------- Started Scheduler -----------------");

    log.info("------- Waiting 60 seconds... -------------");
    try {
      // wait five minutes to show jobs
      Thread.sleep(3L * 1000L);
      // executing...
    } catch (Exception e) {
      //
    }

    log.info("------- Shutting Down ---------------------");

    sched.shutdown(true);

    log.info("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

  }

  public static void main(String[] args) throws Exception {

    JobStateExample1 example = new JobStateExample1();
    example.run();
  }

}
