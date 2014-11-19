package de.fhb.uebung1.controller.actions.get;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fhb.uebung1.commons.HttpRequestActionBase;
import de.fhb.uebung1.helper.PollJob;

/**
 * Created by Max on 18.11.14.
 */
public class ActivateQueue extends HttpRequestActionBase {

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            JobDetail job = JobBuilder.newJob(PollJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(40)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            resp.getOutputStream().print("QuartzJob started");
        } catch (SchedulerException se) {
            resp.getOutputStream().print("Starting QuartzJob failed");
        }
    }
}
