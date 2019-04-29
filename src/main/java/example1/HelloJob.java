package example1;

/**
 * @ Author     ：Jet.wu [jet.wu@kpmg.com]
 * @ Date       ：Created in 10:59 上午 23/04/2019
 * @ Description：${description}
 * @since jdk1.8
 */

import java.util.Date;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * This is just a simple job that says "Hello" to the world.
 * </p>
 *
 * @author Bill Kratzer
 */
public class HelloJob implements Job {

  private static Logger _log = Logger.getLogger(HelloJob.class.toString());

  /**
   * <p>
   * Empty constructor for job initilization
   * </p>
   * <p>
   * Quartz requires a public empty constructor so that the
   * scheduler can instantiate the class whenever it needs.
   * </p>
   */
  public HelloJob() {
  }

  /**
   * <p>
   * Called by the <code>{@link org.quartz.Scheduler}</code> when a
   * <code>{@link org.quartz.Trigger}</code> fires that is associated with
   * the <code>Job</code>.
   * </p>
   *
   * @throws JobExecutionException
   *             if there is an exception while executing the job.
   */
  public void execute(JobExecutionContext context)
      throws JobExecutionException {

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Say Hello to the World and display the date/time
    _log.info("Hello World! - " + new Date());
  }


}
