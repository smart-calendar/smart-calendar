package com.sda.smartCalendar.controller;


import com.sda.smartCalendar.controller.modelDTO.EventDTO;
import com.sda.smartCalendar.domain.model.User;
import com.sda.smartCalendar.job.EmailJob;
import com.sda.smartCalendar.payload.ScheduleEmailRequest;
import com.sda.smartCalendar.payload.ScheduleEmailResponse;
import com.sda.smartCalendar.service.EventService;
import com.sda.smartCalendar.service.UserService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Controller
public class EmailJobSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(EmailJobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @ModelAttribute("loggedInUser")
    public User user(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    @GetMapping(value = "/notification/{id}") //{eventId}
    public String showForm(Model model, Principal principal, @PathVariable("id") UUID id) { //@PathVariable("eventId") String eventId
        ScheduleEmailRequest scheduleEmailRequest = new ScheduleEmailRequest();
        EventDTO eventDTO = eventService.findEventByID(id);



        model.addAttribute("schedulerEmailRequest", scheduleEmailRequest);
        scheduleEmailRequest.setEmail(principal.getName());
        scheduleEmailRequest.setDateTime(eventDTO.getEvent_start());
        scheduleEmailRequest.setSubject(eventDTO.getName());
        scheduleEmailRequest.setBody("Przypominamy o twoim wydarzeniu: " +  eventDTO.getName() +"\n"
                + eventDTO.getDescription() + "\n\n\nWiadomość wysłana z aplikacji Smart-Calendar"  );
        return "notification";
    }





    @PostMapping("/notification") //{eventId}
    public ResponseEntity<ScheduleEmailResponse> scheduleEmail(@Valid @RequestBody @ModelAttribute (value = "schedulerEmailRequest")
                                         ScheduleEmailRequest scheduleEmailRequest) { //@PathVariable("eventId") String eventId
        try {
            //EventDTO eventDTO = eventService.findEventByID(id);
            TimeZone timeZone = TimeZone.getDefault();
            scheduleEmailRequest.setTimeZone(timeZone.toZoneId());
            //System.out.println(scheduleEmailRequest.getTimeZone());
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), scheduleEmailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }

            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email", ex);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }




/*    @PostMapping("/scheduleEmail")
    public ResponseEntity<ScheduleEmailResponse> scheduleEmail(@Valid @RequestBody ScheduleEmailRequest scheduleEmailRequest) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleEmailRequest.getDateTime(), scheduleEmailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }

            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email", ex);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }*/

    private JobDetail buildJobDetail(ScheduleEmailRequest scheduleEmailRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
